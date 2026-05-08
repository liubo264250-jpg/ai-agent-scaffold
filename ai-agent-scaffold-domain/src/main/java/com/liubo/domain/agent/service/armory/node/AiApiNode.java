package com.liubo.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.liubo.domain.agent.model.entity.ArmoryCommandEntity;
import com.liubo.domain.agent.model.valobj.AiAgentRegisterVO;
import com.liubo.domain.agent.service.armory.AbstractArmorySupport;
import com.liubo.domain.agent.service.armory.factory.DefaultArmoryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 68
 * 2026/5/7 09:40
 */
@Service
@Slf4j
public class AiApiNode extends AbstractArmorySupport {

    @Override
    protected AiAgentRegisterVO doApply(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> get(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        return defaultStrategyHandler;
    }
}
