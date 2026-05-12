package com.liubo.domain.agent.service.armory.mcp.client;

import com.liubo.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.springframework.ai.tool.ToolCallback;

/**
 * @author 68
 * 2026/5/12 08:47
 */
public interface ToolMcpCreateService {

    ToolCallback[] buildToolCallback(AiAgentConfigTableVO.Module.ChatModel.ToolMcp toolMcp);
}
