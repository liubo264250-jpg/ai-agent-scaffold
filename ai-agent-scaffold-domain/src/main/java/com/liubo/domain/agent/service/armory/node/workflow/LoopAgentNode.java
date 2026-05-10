package com.liubo.domain.agent.service.armory.node.workflow;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LoopAgent;
import com.liubo.domain.agent.model.entity.ArmoryCommandEntity;
import com.liubo.domain.agent.model.valobj.AiAgentConfigTableVO;
import com.liubo.domain.agent.model.valobj.AiAgentRegisterVO;
import com.liubo.domain.agent.model.valobj.enums.AgentTypeEnum;
import com.liubo.domain.agent.service.armory.AbstractArmorySupport;
import com.liubo.domain.agent.service.armory.factory.DefaultArmoryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 68
 * 2026/5/9 22:56
 */
@Service
@Slf4j
public class LoopAgentNode extends AbstractArmorySupport {
    @Override
    protected AiAgentRegisterVO doApply(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        log.info("execute LoopAgentNode");
        List<AiAgentConfigTableVO.Module.AgentWorkflow> agentWorkflows = dynamicContext.getAgentWorkflows();
        AiAgentConfigTableVO.Module.AgentWorkflow agentWorkflow = agentWorkflows.remove(0);
        List<BaseAgent> subAgents = dynamicContext.queryAgentList(agentWorkflow.getSubAgents());
        LoopAgent loopAgent = LoopAgent.builder()
                .name(agentWorkflow.getName())
                .description(agentWorkflow.getDescription())
                .subAgents(subAgents)
                .maxIterations(agentWorkflow.getMaxIterations())
                .build();
        dynamicContext.getAgentGroup().put(agentWorkflow.getName(), loopAgent);
        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> get(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        List<AiAgentConfigTableVO.Module.AgentWorkflow> agentWorkflows = dynamicContext.getAgentWorkflows();
        if (null == agentWorkflows || agentWorkflows.isEmpty()) {
            return defaultStrategyHandler;
        }

        AiAgentConfigTableVO.Module.AgentWorkflow agentWorkflow = agentWorkflows.get(0);

        String type = agentWorkflow.getType();
        AgentTypeEnum agentTypeEnum = AgentTypeEnum.fromType(type);

        if (null == agentTypeEnum) {
            throw new RuntimeException("agentWorkflow type is error!");
        }

        String node = agentTypeEnum.getNode();

        return switch (node) {
            case "parallelAgentNode" -> getBean("parallelAgentNode");
            case "sequentialAgentNode" -> getBean("sequentialAgentNode");
            default -> defaultStrategyHandler;
        };
    }
}
