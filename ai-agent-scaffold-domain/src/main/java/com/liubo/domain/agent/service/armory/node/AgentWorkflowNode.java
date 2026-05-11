package com.liubo.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.liubo.domain.agent.model.entity.ArmoryCommandEntity;
import com.liubo.domain.agent.model.valobj.AiAgentConfigTableVO;
import com.liubo.domain.agent.model.valobj.AiAgentRegisterVO;
import com.liubo.domain.agent.model.valobj.enums.AgentTypeEnum;
import com.liubo.domain.agent.service.armory.AbstractArmorySupport;
import com.liubo.domain.agent.service.armory.factory.DefaultArmoryFactory;
import com.liubo.domain.agent.service.armory.node.workflow.LoopAgentNode;
import com.liubo.domain.agent.service.armory.node.workflow.ParallelAgentNode;
import com.liubo.domain.agent.service.armory.node.workflow.SequentialAgentNode;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author 68
 * 2026/5/9 22:56
 */
@Service
@Slf4j
public class AgentWorkflowNode extends AbstractArmorySupport {
    @Resource
    private LoopAgentNode loopAgentNode;
    @Resource
    private ParallelAgentNode parallelAgentNode;
    @Resource
    private SequentialAgentNode sequentialAgentNode;
    @Resource
    private RunnerNode runnerNode;
    @Override
    protected AiAgentRegisterVO doApply(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        log.info("execute AgentWorkflowNode");
        AiAgentConfigTableVO aiAgentConfigTableVO = requestParameter.getAiAgentConfigTableVO();
        List<AiAgentConfigTableVO.Module.AgentWorkflow> agentWorkflows = aiAgentConfigTableVO.getModule().getAgentWorkflows();

        if (null == agentWorkflows || agentWorkflows.isEmpty()) {
            router(requestParameter, dynamicContext);
        }
        dynamicContext.setAgentWorkflows(agentWorkflows);
        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> get(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        List<AiAgentConfigTableVO.Module.AgentWorkflow> agentWorkflows = dynamicContext.getAgentWorkflows();
        if (CollectionUtils.isEmpty(agentWorkflows)) {
            return runnerNode;
        }
        String type = agentWorkflows.get(0).getType();
        AgentTypeEnum agentTypeEnum = AgentTypeEnum.fromType(type);

        if (null == agentTypeEnum) {
            throw new RuntimeException("agentWorkflow type is error!");
        }

        String node = agentTypeEnum.getNode();

        return switch (node) {
            case "loopAgentNode" -> loopAgentNode;
            case "parallelAgentNode" -> parallelAgentNode;
            case "sequentialAgentNode" -> sequentialAgentNode;
            default -> defaultStrategyHandler;
        };
    }
}
