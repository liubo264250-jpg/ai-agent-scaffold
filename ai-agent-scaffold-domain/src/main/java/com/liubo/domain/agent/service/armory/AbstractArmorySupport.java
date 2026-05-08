package com.liubo.domain.agent.service.armory;

import cn.bugstack.wrench.design.framework.tree.AbstractMultiThreadStrategyRouter;
import com.liubo.domain.agent.model.entity.ArmoryCommandEntity;
import com.liubo.domain.agent.model.valobj.AiAgentRegisterVO;
import com.liubo.domain.agent.service.armory.factory.DefaultArmoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author 68
 * 2026/5/7 09:45
 */
public abstract class AbstractArmorySupport extends AbstractMultiThreadStrategyRouter<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> {
    private final Logger log = LoggerFactory.getLogger(AbstractArmorySupport.class);

    @Override
    protected void multiThread(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {

    }
}
