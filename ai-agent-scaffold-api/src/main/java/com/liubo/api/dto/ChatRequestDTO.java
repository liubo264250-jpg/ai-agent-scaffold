package com.liubo.api.dto;

import lombok.Data;

/**
 * @author 68
 * 2026/5/15 22:45
 */
@Data
public class ChatRequestDTO {
    private String agentId;
    private String userId;
    private String sessionId;
    private String message;
}
