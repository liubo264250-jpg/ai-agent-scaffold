package com.liubo.domain.agent.service.armory.factory;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.SequentialAgent;
import com.liubo.domain.agent.model.entity.ArmoryCommandEntity;
import com.liubo.domain.agent.model.valobj.AiAgentConfigTableVO;
import com.liubo.domain.agent.model.valobj.AiAgentRegisterVO;
import com.liubo.domain.agent.service.armory.node.RootNode;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author 68
 * 2026/5/7 09:39
 */
@Service
public class DefaultArmoryFactory {

    @Resource
    private RootNode rootNode;

    @Resource
    private ApplicationContext applicationContext;


    public StrategyHandler<ArmoryCommandEntity, DynamicContext, AiAgentRegisterVO> armoryStrategyHandler() {
        return rootNode;
    }

    public AiAgentRegisterVO getAiAgentRegisterVO(String agentId) {
        return applicationContext.getBean(agentId, AiAgentRegisterVO.class);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {
        /**
         * LLM API
         */
        private OpenAiApi openAiApi;
        /**
         * 对话模型
         */
        private ChatModel chatModel;
        /**
         * 原子安全的递进步骤
         */
        private AiAgentConfigTableVO.Module.AgentWorkflow currentAgentWorkflow;
        /**
         * 原子安全的递进步骤
         */
        private AtomicInteger currentStepIndex = new AtomicInteger(0);
        /**
         * 智能体组
         */
        private Map<String, BaseAgent> agentGroup = new HashMap<>();

        private Map<String, Object> dataObjects = new HashMap<>();

        private SequentialAgent sequentialAgent;

        public <T> void setValue(String key, T value) {
            dataObjects.put(key, value);
        }

        public <T> T getValue(String key) {
            return (T) dataObjects.get(key);
        }

        public List<BaseAgent> queryAgentList(List<String> subAgents) {
            return Optional.ofNullable(subAgents)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(name -> this.agentGroup.get(name))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        public int getCurrentStepIndex() {
            return currentStepIndex.get();
        }

        public void addCurrentStepIndex() {
            currentStepIndex.incrementAndGet();
        }
    }
}
