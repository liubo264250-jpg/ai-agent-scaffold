package com.liubo.domain.agent.service.armory.matter.mcp.client.factory;

import com.liubo.domain.agent.model.valobj.AiAgentConfigTableVO;
import com.liubo.domain.agent.model.valobj.enums.ToolMcpTypeEnum;
import com.liubo.domain.agent.service.armory.matter.mcp.client.ToolMcpCreateService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author 68
 * 2026/5/12 08:50
 */
@Slf4j
@Service
public class DefaultMcpClientFactory {

    @Resource
    private Map<String, ToolMcpCreateService> toolMcpCreateServices;

    private ToolMcpCreateService getToolMcpCreateService(AiAgentConfigTableVO.Module.ChatModel.ToolMcp toolMcp) {
        ToolMcpTypeEnum toolMcpTypeEnum = ToolMcpTypeEnum.getByType(toolMcp.getToolType());
        return toolMcpCreateServices.get(toolMcpTypeEnum.getStrategyBeanName());
    }

    public List<ToolCallback> buildToolCallback(AiAgentConfigTableVO.Module.ChatModel.ToolMcp toolMcp){
        ToolMcpCreateService toolMcpCreateService = getToolMcpCreateService(toolMcp);
        return Arrays.asList(toolMcpCreateService.buildToolCallback(toolMcp));
    }
}
