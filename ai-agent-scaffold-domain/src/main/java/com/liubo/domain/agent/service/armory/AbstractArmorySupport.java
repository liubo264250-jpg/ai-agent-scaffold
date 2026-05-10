package com.liubo.domain.agent.service.armory;

import cn.bugstack.wrench.design.framework.tree.AbstractMultiThreadStrategyRouter;
import com.liubo.domain.agent.model.entity.ArmoryCommandEntity;
import com.liubo.domain.agent.model.valobj.AiAgentRegisterVO;
import com.liubo.domain.agent.service.armory.factory.DefaultArmoryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author 68
 * 2026/5/7 09:45
 */
public abstract class AbstractArmorySupport extends AbstractMultiThreadStrategyRouter<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> {
    private final Logger log = LoggerFactory.getLogger(AbstractArmorySupport.class);

    @Resource
    protected ApplicationContext applicationContext;


    @Override
    protected void multiThread(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {

    }

    protected <T> T getBean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }


    protected synchronized <T> void registerBean(String beanName, Class<T> beanClass, T beanInstance) {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass, () -> beanInstance);
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        if (beanFactory.containsBeanDefinition(beanName)) {
            beanFactory.removeBeanDefinition(beanName);
        }
        beanFactory.registerBeanDefinition(beanName,beanDefinition);
        log.info("成功注册Bean: {}", beanName);
    }
}
