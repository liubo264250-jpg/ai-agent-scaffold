package com.liubo.domain.agent.service.armory.factory;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.liubo.domain.agent.model.entity.ArmoryCommandEntity;
import com.liubo.domain.agent.model.valobj.AiAgentRegisterVO;
import com.liubo.domain.agent.service.armory.node.RootNode;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 68
 * 2026/5/7 09:39
 */
@Service
public class DefaultArmoryFactory {

    @Resource
    private RootNode rootNode;

    public StrategyHandler<ArmoryCommandEntity, DynamicContext, AiAgentRegisterVO> armoryStrategyHandler() {
        return rootNode;
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {

        private Map<String, Object> dataObjects = new HashMap<>();

        public <T> void setValue(String key, T value) {
            dataObjects.put(key, value);
        }

        public <T> T getValue(String key) {
            return (T) dataObjects.get(key);
        }
    }
}
