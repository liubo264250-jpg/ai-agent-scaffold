package com.liubo.api;

import com.liubo.api.dto.ChatRequestDTO;
import com.liubo.api.dto.CreateSessionRequestDTO;
import com.liubo.api.response.AiAgentConfigResponseDTO;
import com.liubo.api.response.ChatResponseDTO;
import com.liubo.api.response.CreateSessionResponseDTO;
import com.liubo.api.response.Response;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.List;

/**
 * @author 68
 * 2026/5/15 22:40
 */
public interface IAgentService {
    Response<List<AiAgentConfigResponseDTO>> queryAiAgentConfigList();

    Response<CreateSessionResponseDTO> createSession(CreateSessionRequestDTO requestDTO);

    Response<ChatResponseDTO> chat(ChatRequestDTO requestDTO);

    ResponseBodyEmitter chatStream(ChatRequestDTO requestDTO);
}
