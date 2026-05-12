package com.liubo.domain.agent.model.valobj.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 68
 * 2026/5/12 09:19
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ToolMcpTypeEnum {
    sse("sse", "sse", "sseToolMcpCreateService"),
    stdio("stdio", "stdio", "stdioToolMcpCreateService"),
    local("local", "local", "localToolMcpCreateService"),
    ;

    private String name;
    private String type;
    private String strategyBeanName;

    public static ToolMcpTypeEnum getByType(String type) {
        if (StringUtils.isBlank(type)) {
            throw new RuntimeException("type value is Empty!");
        }
        ToolMcpTypeEnum result = null;
        for (ToolMcpTypeEnum toolMcpTypeEnum : values()) {
            if (toolMcpTypeEnum.getType().equals(type)) {
                result = toolMcpTypeEnum;
            }
        }
        if (result == null) {
            throw new RuntimeException("type value " + type + " not exist!");
        }
        return result;
    }
}
