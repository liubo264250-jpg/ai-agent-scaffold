package com.liubo.domain.agent.service;

import com.google.adk.events.Event;
import com.liubo.domain.agent.model.entity.ChatCommandEntity;
import com.liubo.domain.agent.model.valobj.AiAgentConfigTableVO;
import io.reactivex.rxjava3.core.Flowable;

import java.util.List;

/**
 * @author 68
 * 2026/5/7 09:24
 */
public interface IChatService {
    List<AiAgentConfigTableVO.Agent> queryAiAgentConfigList();

    String createSession(String agentId, String userId);

    List<String> handleMessage(ChatCommandEntity chatCommandEntity);

    List<String> handleMessage(String agentId, String userId, String message);

    List<String> handleMessage(String agentId, String userId, String sessionId, String message);

    Flowable<Event> handleMessageStream(String agentId, String userId, String sessionId, String message);
}
