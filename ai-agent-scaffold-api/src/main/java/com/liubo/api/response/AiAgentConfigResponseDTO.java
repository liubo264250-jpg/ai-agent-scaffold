package com.liubo.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 68
 * 2026/5/15 22:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiAgentConfigResponseDTO {
    /**
     * 智能体ID
     */
    private String agentId;

    /**
     * 智能体名称
     */
    private String agentName;

    /**
     * 智能体描述
     */
    private String agentDesc;
}
