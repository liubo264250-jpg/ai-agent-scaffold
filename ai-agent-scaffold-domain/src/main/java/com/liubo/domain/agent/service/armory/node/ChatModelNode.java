package com.liubo.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liubo.domain.agent.model.entity.ArmoryCommandEntity;
import com.liubo.domain.agent.model.valobj.AiAgentConfigTableVO;
import com.liubo.domain.agent.model.valobj.AiAgentRegisterVO;
import com.liubo.domain.agent.service.armory.AbstractArmorySupport;
import com.liubo.domain.agent.service.armory.factory.DefaultArmoryFactory;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.spec.McpClientTransport;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 68
 * 2026/5/8 08:56
 */
@Service
@Slf4j
public class ChatModelNode extends AbstractArmorySupport {

    @Resource
    private AgentNode agentNode;

    @Override
    protected AiAgentRegisterVO doApply(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        log.info("execute ChatModelNode");
        OpenAiApi openAiApi = dynamicContext.getOpenAiApi();
        AiAgentConfigTableVO.Module.ChatModel chatModelConfig = requestParameter.getChatModelConfig();
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(chatModelConfig.getModel())
                        .toolCallbacks(buildToolCallBacks(chatModelConfig.getToolMcpList()))
                        .build())
                .build();
        dynamicContext.setChatModel(chatModel);
        return router(requestParameter, dynamicContext);
    }

    private List<ToolCallback> buildToolCallBacks(List<AiAgentConfigTableVO.Module.ChatModel.ToolMcp> toolMcpList) {
        List<McpSyncClient> mcpSyncClients = Optional.ofNullable(toolMcpList)
                .orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .map(this::createMcpSyncClient)
                .collect(Collectors.toList());

        return Arrays.asList(SyncMcpToolCallbackProvider.builder().mcpClients(mcpSyncClients).build().getToolCallbacks());
    }

    private McpSyncClient createMcpSyncClient(AiAgentConfigTableVO.Module.ChatModel.ToolMcp toolMcp) {
        AiAgentConfigTableVO.Module.ChatModel.ToolMcp.SSEServerParameters sseConfig = toolMcp.getSse();
        AiAgentConfigTableVO.Module.ChatModel.ToolMcp.StdioServerParameters stdioConfig = toolMcp.getStdio();
        if (null != sseConfig) {
            // https://127.0.0.1:9999/sse?apikey=DElk89iu8Ehhnbu
            String originalBaseUri = sseConfig.getBaseUri();
            String baseUri;
            String sseEndpoint;

            int queryParamStartIndex = originalBaseUri.indexOf("sse");
            if (queryParamStartIndex != -1) {
                baseUri = originalBaseUri.substring(0, queryParamStartIndex - 1);
                sseEndpoint = originalBaseUri.substring(queryParamStartIndex - 1);
            } else {
                baseUri = originalBaseUri;
                sseEndpoint = sseConfig.getSseEndpoint();
            }
            sseEndpoint = StringUtils.isBlank(sseEndpoint) ? "/sse" : sseEndpoint;
            McpClientTransport mcpClientTransport = HttpClientSseClientTransport.builder(baseUri).sseEndpoint(sseEndpoint).build();
            McpSyncClient mcpSyncClient = McpClient.sync(mcpClientTransport).requestTimeout(Duration.ofMinutes(sseConfig.getRequestTimeout())).build();
            var init_sse = mcpSyncClient.initialize();
            log.info("Tool SSE MCP Initialized {}", init_sse);
            return mcpSyncClient;
        }
        if (null != stdioConfig) {
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
            return mcpSyncClient;
        }
        throw new RuntimeException("toolMcp sse and stdio is null!");
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> get(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        return agentNode;
    }
}
