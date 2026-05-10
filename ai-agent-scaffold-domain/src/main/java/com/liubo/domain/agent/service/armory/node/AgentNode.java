package com.liubo.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.google.adk.agents.LlmAgent;
import com.google.adk.models.springai.SpringAI;
import com.liubo.domain.agent.model.entity.ArmoryCommandEntity;
import com.liubo.domain.agent.model.valobj.AiAgentConfigTableVO;
import com.liubo.domain.agent.model.valobj.AiAgentRegisterVO;
import com.liubo.domain.agent.service.armory.AbstractArmorySupport;
import com.liubo.domain.agent.service.armory.factory.DefaultArmoryFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 68
 * 2026/5/8 09:46
 */
@Service
@Slf4j
public class AgentNode extends AbstractArmorySupport {

    @Resource
    private AgentWorkflowNode agentWorkflowNode;

    @Override
    protected AiAgentRegisterVO doApply(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        log.info("execute AgentNode");
        ChatModel chatModel = dynamicContext.getChatModel();
        List<AiAgentConfigTableVO.Module.Agent> agentsConfig = requestParameter.getAgentsConfig();
        for (AiAgentConfigTableVO.Module.Agent agent : agentsConfig) {
            LlmAgent llmAgent = LlmAgent.builder()
                    .name(agent.getName())
                    .description(agent.getDescription())
                    .instruction(agent.getInstruction())
                    .outputKey(agent.getOutputKey())
                    .model(new SpringAI(chatModel))
                    .build();
            dynamicContext.getAgentGroup().put(agent.getName(), llmAgent);
        }
        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> get(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        return agentWorkflowNode;
    }
}
