package com.liubo.test.agent;

import com.alibaba.fastjson.JSON;
import com.liubo.domain.agent.model.entity.ChatCommandEntity;
import com.liubo.domain.agent.model.valobj.AiAgentConfigTableVO;
import com.liubo.domain.agent.service.IChatService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MimeTypeUtils;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * @author 68
 * 2026/5/15 09:33
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ChatServiceTest {

    @Resource
    private IChatService chatService;

    @Value("classpath:file/penguin.jpg")
    private org.springframework.core.io.Resource imageResource;

    @Test
    public void test_handleMessage_01() throws InterruptedException {
//        List<String> messages = chatService.handleMessage("100001", "xiaofuge", "你具备哪些能力");
//        log.info("测试结果:{}", JSON.toJSONString(messages));
        List<String> message = chatService.handleMessage("100003", "xiaofuge", "你具备哪些skill技能");
        log.info("测试结果:{}", JSON.toJSONString(message));
    }

    @Test
    public void test_handleMessage_02() throws InterruptedException {
        List<String> messages = chatService.handleMessage("100002", "xiaofuge", "你具备哪些能力");
        log.info("测试结果:{}", JSON.toJSONString(messages));
    }

    @Test
    public void test_handleMessage_03() throws InterruptedException {
        List<String> messages = chatService.handleMessage("100003", "xiaofuge", "把xiaofuge转换为大写");
        log.info("测试结果:{}", JSON.toJSONString(messages));
    }

    @Test
    public void test_queryAiAgentConfigList() {
        List<AiAgentConfigTableVO.Agent> agents = chatService.queryAiAgentConfigList();
        log.info("测试结果:{}", JSON.toJSONString(agents));
    }

    @Test
    public void test_handleMessage_04_withImage() throws Exception {
        String agentId = "100003";
        String userId = "xiaofuge";

        String sessionId = chatService.createSession(agentId, userId);

        URL resource = Thread.currentThread().getContextClassLoader().getResource("file/penguin.jpg");

        Assert.assertNotNull(resource);

        byte[] bytes;
        try (InputStream inputStream = resource.openStream()) {
            bytes = inputStream.readAllBytes();
        }

        ChatCommandEntity chatCommandEntity = ChatCommandEntity.builder()
                .agentId(agentId)
                .userId(userId)
                .sessionId(sessionId)
                .texts(List.of(new ChatCommandEntity.Content.Text("请识别这张图片，告诉我是什么动物，并用一句话描述。")))
//                .texts(List.of())
                .files(List.of())
//                .inlineDatas(List.of(new ChatCommandEntity.Content.InlineData(imageResource.getContentAsByteArray(), MimeTypeUtils.IMAGE_PNG_VALUE)))
                .inlineDatas(List.of(new ChatCommandEntity.Content.InlineData(bytes, MimeTypeUtils.IMAGE_PNG_VALUE)))
                .build();

        List<String> messages = chatService.handleMessage(chatCommandEntity);
        log.info("测试结果:{}", JSON.toJSONString(messages));
    }


}
