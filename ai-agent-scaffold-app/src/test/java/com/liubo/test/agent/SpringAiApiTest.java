package com.liubo.test.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

import java.io.FilterOutputStream;

/**
 * @author 68
 * 2026/4/16 08:47
 */
@Slf4j
public class SpringAiApiTest {
    public static void main(String[] args) {
        OpenAiApi openAiApi = OpenAiApi.builder()
                .baseUrl("https://apis.itedus.cn")
                .apiKey("sk-4aILQwYoWOtkVHgJC9Ca82D1373d425984Cb3bAc3cE6DeA2")
                .completionsPath("v1/chat/completions")
                .embeddingsPath("v1/embeddings")
                .build();

        OpenAiChatModel openAiChatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions
                        .builder()
                        .model("gpt-4.1")
                        .build())
                .build();

        String call = openAiChatModel.call("java是什么?");
        log.info("测试结果:{}", call);    }
}
