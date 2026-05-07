package com.liubo.test.agent;

import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 68
 * 2026/4/16 09:03
 */
@Slf4j
public class LangChain4jApiTest {
    public static void main(String[] args) {
        OpenAiChatModel openAiChatModel = OpenAiChatModel.builder()
                .baseUrl("https://apis.itedus.cn/v1")
                .apiKey("sk-4aILQwYoWOtkVHgJC9Ca82D1373d425984Cb3bAc3cE6DeA2")
                .modelName("gpt-4o-mini")
                .build();
        String result = openAiChatModel.chat("golang是什么？");
        log.info("result:{}", result);
    }
}
