package com.liubo.domain.agent.model.entity;

import com.liubo.domain.agent.model.valobj.AiAgentConfigTableVO;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author 68
 * 2026/5/7 09:43
 */
@Data
@Builder
public class ArmoryCommandEntity {

    private AiAgentConfigTableVO aiAgentConfigTableVO;

    public AiAgentConfigTableVO.Module.AiApi getAiApiConfig() {
        return Optional.ofNullable(aiAgentConfigTableVO)
                .map(AiAgentConfigTableVO::getModule)
                .map(AiAgentConfigTableVO.Module::getAiApi)
                .orElse(new AiAgentConfigTableVO.Module.AiApi());
    }

    public AiAgentConfigTableVO.Module.ChatModel getChatModelConfig(){
        return Optional.ofNullable(aiAgentConfigTableVO)
                .map(AiAgentConfigTableVO::getModule)
                .map(AiAgentConfigTableVO.Module::getChatModel)
                .orElse(new AiAgentConfigTableVO.Module.ChatModel());
    }

    public List<AiAgentConfigTableVO.Module.Agent> getAgentsConfig(){
        return Optional.ofNullable(aiAgentConfigTableVO)
                .map(AiAgentConfigTableVO::getModule)
                .map(AiAgentConfigTableVO.Module::getAgents)
                .orElse(Collections.emptyList());
    }
}
