package com.liubo.domain.agent.service.armory.matter.skills;

import com.liubo.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;

/**
 * @author 68
 * 2026/5/16 10:16
 */
public interface ToolSkillsCreateService {

    List<ToolCallback> buildToolCallback(AiAgentConfigTableVO.Module.ChatModel.ToolSkills toolSkills);

}
