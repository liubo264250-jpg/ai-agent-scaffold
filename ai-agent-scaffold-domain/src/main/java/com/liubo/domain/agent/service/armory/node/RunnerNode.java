package com.liubo.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.runner.InMemoryRunner;
import com.liubo.domain.agent.model.entity.ArmoryCommandEntity;
import com.liubo.domain.agent.model.valobj.AiAgentConfigTableVO;
import com.liubo.domain.agent.model.valobj.AiAgentRegisterVO;
import com.liubo.domain.agent.service.armory.AbstractArmorySupport;
import com.liubo.domain.agent.service.armory.factory.DefaultArmoryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 68
 * 2026/5/10 16:13
 */
@Service
@Slf4j
public class RunnerNode extends AbstractArmorySupport {
    @Override
    protected AiAgentRegisterVO doApply(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        log.info("execute RunnerNode");
        AiAgentConfigTableVO aiAgentConfigTableVO = requestParameter.getAiAgentConfigTableVO();
        String appName = aiAgentConfigTableVO.getAppName();
        String agentId = aiAgentConfigTableVO.getAgent().getAgentId();
        String agentName = aiAgentConfigTableVO.getAgent().getAgentName();
        String agentDesc = aiAgentConfigTableVO.getAgent().getAgentDesc();
        // 获取上下文对象
        SequentialAgent sequentialAgent = dynamicContext.getSequentialAgent();

        // 会话运行节点
        InMemoryRunner runner = new InMemoryRunner(sequentialAgent, appName);
        // 构建注册对象
        AiAgentRegisterVO aiAgentRegisterVO = AiAgentRegisterVO.builder()
                .agentId(agentId)
                .appName(appName)
                .agentName(agentName)
                .agentDesc(agentDesc)
                .runner(runner)
                .build();
        // 注册到 Spring 容器
        registerBean(agentId, AiAgentRegisterVO.class, aiAgentRegisterVO);
        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> get(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        return defaultStrategyHandler;
    }
}
