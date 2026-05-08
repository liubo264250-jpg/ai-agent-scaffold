package com.liubo.domain.agent.service.armory;

import com.liubo.domain.agent.model.entity.ArmoryCommandEntity;
import com.liubo.domain.agent.model.valobj.AiAgentConfigTableVO;
import com.liubo.domain.agent.service.IArmoryService;
import com.liubo.domain.agent.service.armory.factory.DefaultArmoryFactory;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 68
 * 2026/5/7 09:36
 */
@Service
public class ArmoryService implements IArmoryService {

    @Resource
    private DefaultArmoryFactory defaultArmoryFactory;

    @Override
    public void acceptArmoryAgents(List<AiAgentConfigTableVO> tables) throws Exception {
        for (AiAgentConfigTableVO table : tables) {
            defaultArmoryFactory.armoryStrategyHandler()
                    .apply(ArmoryCommandEntity.builder().aiAgentConfigTableVO(table).build(), new DefaultArmoryFactory.DynamicContext());
        }
    }
}
