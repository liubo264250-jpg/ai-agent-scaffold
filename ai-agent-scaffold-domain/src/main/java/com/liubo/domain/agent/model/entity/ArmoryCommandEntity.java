package com.liubo.domain.agent.model.entity;

import com.liubo.domain.agent.model.valobj.AiAgentConfigTableVO;
import lombok.Builder;
import lombok.Data;

/**
 * @author 68
 * 2026/5/7 09:43
 */
@Data
@Builder
public class ArmoryCommandEntity {

    private AiAgentConfigTableVO aiAgentConfigTableVO;
}
