package com.liubo.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.plugins.BasePlugin;
import com.google.adk.runner.InMemoryRunner;
import com.liubo.domain.agent.model.entity.ArmoryCommandEntity;
import com.liubo.domain.agent.model.valobj.AiAgentConfigTableVO;
import com.liubo.domain.agent.model.valobj.AiAgentRegisterVO;
import com.liubo.domain.agent.service.armory.AbstractArmorySupport;
import com.liubo.domain.agent.service.armory.factory.DefaultArmoryFactory;
import com.liubo.types.enums.ResponseCode;
import com.liubo.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        // 会话运行节点
        InMemoryRunner runner = createRunner(requestParameter,dynamicContext,appName);
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

    private InMemoryRunner createRunner(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext, String appName) {
        AiAgentConfigTableVO.Module.Runner runnerConfig = requestParameter.getRunnerConfig();
        if (StringUtils.isBlank(appName)){
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(),ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        // 获取智能体（用这个智能体装配 InMemoryRunner）
        BaseAgent baseAgent = dynamicContext.getAgentGroup().get(runnerConfig.getAgentName());
        List<BasePlugin> basePluginList = Optional.ofNullable(runnerConfig.getPluginNameList())
                .orElse(new ArrayList<>())
                .stream()
                .map(this::<BasePlugin>getBean).collect(Collectors.toList());
        // 会话运行节点
        return new InMemoryRunner(baseAgent, appName,basePluginList);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> get(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        return defaultStrategyHandler;
    }
}
