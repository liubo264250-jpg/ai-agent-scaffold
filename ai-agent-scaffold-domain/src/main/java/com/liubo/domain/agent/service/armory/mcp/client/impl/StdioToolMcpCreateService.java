package com.liubo.domain.agent.service.armory.mcp.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liubo.domain.agent.model.valobj.AiAgentConfigTableVO;
import com.liubo.domain.agent.service.armory.mcp.client.ToolMcpCreateService;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @author 68
 * 2026/5/12 08:48
 */
@Slf4j
@Service
public class StdioToolMcpCreateService implements ToolMcpCreateService {
    @Override
    public ToolCallback[] buildToolCallback(AiAgentConfigTableVO.Module.ChatModel.ToolMcp toolMcp) {
        AiAgentConfigTableVO.Module.ChatModel.ToolMcp.StdioServerParameters stdioConfig = toolMcp.getStdio();
        AiAgentConfigTableVO.Module.ChatModel.ToolMcp.StdioServerParameters.ServerParameters serverParameters = stdioConfig.getServerParameters();
        // https://github.com/modelcontextprotocol/servers/tree/main/src/filesystem
        ServerParameters stdioParams = ServerParameters
                .builder(serverParameters.getCommand())
                .args(serverParameters.getArgs())
                .env(serverParameters.getEnv())
                .build();
        StdioClientTransport stdioClientTransport = new StdioClientTransport(stdioParams, new JacksonMcpJsonMapper(new ObjectMapper()));
        McpSyncClient mcpSyncClient = McpClient.sync(stdioClientTransport).requestTimeout(Duration.ofMinutes(stdioConfig.getRequestTimeout())).build();
        var init_stdio = mcpSyncClient.initialize();
        log.info("Tool Stdio MCP Initialized {}", init_stdio);
        return SyncMcpToolCallbackProvider.builder().mcpClients(mcpSyncClient).build().getToolCallbacks();
    }
}
