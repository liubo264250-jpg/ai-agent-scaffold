package com.liubo.domain.agent.service.armory.mcp.client.impl;

import com.liubo.domain.agent.model.valobj.AiAgentConfigTableVO;
import com.liubo.domain.agent.service.armory.mcp.client.ToolMcpCreateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author 68
 * 2026/5/12 08:49
 */
@Slf4j
@Service
public class LocalToolMcpCreateService implements ToolMcpCreateService {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public ToolCallback[] buildToolCallback(AiAgentConfigTableVO.Module.ChatModel.ToolMcp toolMcp) {
        AiAgentConfigTableVO.Module.ChatModel.ToolMcp.LocalParameters localParameters = toolMcp.getLocal();
        ToolCallbackProvider toolCallbackProvider = (ToolCallbackProvider) applicationContext.getBean(localParameters.getName());
        return toolCallbackProvider.getToolCallbacks();
    }
}
