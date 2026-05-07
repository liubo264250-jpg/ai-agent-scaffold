package com.liubo.domain.agent.model.valobj.properties;

import com.liubo.domain.agent.model.valobj.AiAgentConfigTableVO;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author 68
 * 2026/5/6 09:47
 */
@Data
@ConfigurationProperties(prefix = "ai.agent.config")
public class AiAgentAutoConfigProperties {
    private Map<String, AiAgentConfigTableVO> tables;
}
