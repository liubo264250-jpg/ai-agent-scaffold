package com.liubo.domain.agent.model.valobj;

import com.google.adk.runner.InMemoryRunner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 68
 * 2026/5/7 09:38
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiAgentRegisterVO {
    private String agentId;
    private String appName;
    private String agentName;
    private String agentDesc;
    private InMemoryRunner runner;
}
