package com.liubo.domain.agent.service;

import com.liubo.domain.agent.model.valobj.AiAgentConfigTableVO;

import java.util.List;

/**
 * @author 68
 * 2026/5/7 09:23
 */
public interface IArmoryService {
    void acceptArmoryAgents(List<AiAgentConfigTableVO> tables) throws Exception;
}
