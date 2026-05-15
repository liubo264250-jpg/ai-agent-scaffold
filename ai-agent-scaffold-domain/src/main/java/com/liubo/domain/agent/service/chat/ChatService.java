package com.liubo.domain.agent.service.chat;

import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import com.liubo.domain.agent.model.entity.ChatCommandEntity;
import com.liubo.domain.agent.model.valobj.AiAgentConfigTableVO;
import com.liubo.domain.agent.model.valobj.AiAgentRegisterVO;
import com.liubo.domain.agent.model.valobj.properties.AiAgentAutoConfigProperties;
import com.liubo.domain.agent.service.IChatService;
import com.liubo.domain.agent.service.armory.factory.DefaultArmoryFactory;
import com.liubo.types.enums.ResponseCode;
import com.liubo.types.exception.AppException;
import io.reactivex.rxjava3.core.Flowable;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author 68
 * 2026/5/7 09:36
 */
@Slf4j
@Service
public class ChatService implements IChatService {
    @Resource
    private DefaultArmoryFactory defaultArmoryFactory;

    @Resource
    private AiAgentAutoConfigProperties aiAgentAutoConfigProperties;

    private final Map<String, String> userSessions = new ConcurrentHashMap<>();

    @Override
    public List<AiAgentConfigTableVO.Agent> queryAiAgentConfigList() {
        Map<String, AiAgentConfigTableVO> tables = aiAgentAutoConfigProperties.getTables();
        return Optional.ofNullable(tables)
                .map(Map::values)
                .orElse(new ArrayList<>())
                .stream()
                .filter(Objects::nonNull)
                .map(AiAgentConfigTableVO::getAgent)
                .collect(Collectors.toList());
    }

    @Override
    public String createSession(String agentId, String userId) {
        // 获取对象
        AiAgentRegisterVO aiAgentRegisterVO = defaultArmoryFactory.getAiAgentRegisterVO(agentId);

        if (null == aiAgentRegisterVO) {
            throw new AppException(ResponseCode.E0001.getCode());
        }

        String appName = aiAgentRegisterVO.getAppName();
        InMemoryRunner runner = aiAgentRegisterVO.getRunner();
        return userSessions.computeIfAbsent(userId, uid -> {
            Session session = runner.sessionService()
                    .createSession(appName, uid)
                    .blockingGet();
            return session.id();
        });
    }

    @Override
    public List<String> handleMessage(ChatCommandEntity chatCommandEntity) {
        // 获取对象
        AiAgentRegisterVO aiAgentRegisterVO = defaultArmoryFactory.getAiAgentRegisterVO(chatCommandEntity.getAgentId());

        if (null == aiAgentRegisterVO) {
            throw new AppException(ResponseCode.E0001.getCode());
        }
        // 构建参数
        List<Part> partList = new ArrayList<>(Optional.ofNullable(chatCommandEntity.getTexts())
                .orElse(new ArrayList<>())
                .stream()
                .map(text -> Part.fromText(text.getMessage()))
                .toList());

        partList.addAll(Optional.ofNullable(chatCommandEntity.getFiles())
                .orElse(new ArrayList<>())
                .stream()
                .map(file -> Part.fromUri(file.getFileUri(), file.getMimeType()))
                .toList());

        partList.addAll(Optional.ofNullable(chatCommandEntity.getInlineDatas())
                .orElse(new ArrayList<>())
                .stream()
                .map(inlineData -> Part.fromBytes(inlineData.getBytes(), inlineData.getMimeType()))
                .toList());
        Content content = Content.builder().role("user").parts(partList).build();
        InMemoryRunner runner = aiAgentRegisterVO.getRunner();
        Flowable<Event> events = runner.runAsync(chatCommandEntity.getUserId(), chatCommandEntity.getSessionId(), content);
        List<String> outputs = new ArrayList<>();
        events.blockingForEach(event -> outputs.add(event.stringifyContent()));
        return outputs;
    }

    @Override
    public List<String> handleMessage(String agentId, String userId, String message) {
        // 获取对象
        AiAgentRegisterVO aiAgentRegisterVO = defaultArmoryFactory.getAiAgentRegisterVO(agentId);

        if (null == aiAgentRegisterVO) {
            throw new AppException(ResponseCode.E0001.getCode());
        }

        String sessionId = createSession(agentId, userId);

        return handleMessage(agentId, userId, sessionId, message);
    }

    @Override
    public List<String> handleMessage(String agentId, String userId, String sessionId, String message) {
        // 获取对象
        AiAgentRegisterVO aiAgentRegisterVO = defaultArmoryFactory.getAiAgentRegisterVO(agentId);

        if (null == aiAgentRegisterVO) {
            throw new AppException(ResponseCode.E0001.getCode());
        }
        InMemoryRunner runner = aiAgentRegisterVO.getRunner();
        Content content = Content.fromParts(Part.fromText(message));
        Flowable<Event> events = runner.runAsync(userId, sessionId, content);
        List<String> outputs = new ArrayList<>();
        events.blockingForEach(event -> outputs.add(event.stringifyContent()));
        return outputs;
    }

    @Override
    public Flowable<Event> handleMessageStream(String agentId, String userId, String sessionId, String message) {
        // 获取对象
        AiAgentRegisterVO aiAgentRegisterVO = defaultArmoryFactory.getAiAgentRegisterVO(agentId);

        if (null == aiAgentRegisterVO) {
            throw new AppException(ResponseCode.E0001.getCode());
        }
        InMemoryRunner runner = aiAgentRegisterVO.getRunner();
        Content content = Content.fromParts(Part.fromText(message));
        return runner.runAsync(userId, sessionId, content);
    }
}
