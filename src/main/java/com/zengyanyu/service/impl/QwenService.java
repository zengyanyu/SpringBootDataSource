package com.zengyanyu.service.impl;

import com.zengyanyu.entity.QwenChatRequest;
import com.zengyanyu.entity.QwenChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * 通义千问服务类（封装 API 调用逻辑）
 *
 * @author zengyanyu
 */
@Service
public class QwenService {

    private final WebClient qwenWebClient;

    @Value("${ai.qwen.model}")
    private String defaultModel;

    @Value("${ai.qwen.result-format}")
    private String resultFormat;

    // 构造注入 WebClient
    public QwenService(WebClient qwenWebClient) {
        this.qwenWebClient = qwenWebClient;
    }

    /**
     * 发送聊天请求给通义千问
     *
     * @param userMessage 用户输入的问题
     * @return AI 回复内容
     */
    public String chat(String userMessage) {
        // 1. 构建请求参数
        QwenChatRequest request = new QwenChatRequest();
        request.setModel(defaultModel);
        request.setResultFormat(resultFormat);
        // 添加用户消息（role 固定为 user）
        QwenChatRequest.Input input = new QwenChatRequest.Input();
        input.setMessages(Collections.singletonList(
                new QwenChatRequest.QwenMessage("user", userMessage)
        ));
        request.setInput(input);

        try {
            // 2. 调用通义千问 API（接口路径固定为 /services/aigc/text-generation/generation）
            Mono<QwenChatResponse> responseMono = qwenWebClient
                    .post()
                    .uri("/services/aigc/text-generation/generation")
                    .bodyValue(request)
                    .retrieve()
                    // 异常处理：捕获接口返回的错误状态码
                    .onStatus(status -> status.isError(), response ->
                            response.bodyToMono(String.class)
                                    .flatMap(errorMsg -> Mono.error(new RuntimeException("通义千问接口调用失败：" + errorMsg)))
                    )
                    .bodyToMono(QwenChatResponse.class);

            // 3. 阻塞获取响应（同步调用，适合简单场景）
            QwenChatResponse response = responseMono.block();

            // 4. 解析响应结果
            if (response != null && response.getOutput() != null) {
                return response.getOutput().getText();
            } else {
                return "通义千问未返回有效内容";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "调用通义千问失败：" + e.getMessage();
        }
    }

    /**
     * 支持多轮对话的聊天方法（扩展）
     *
     * @param messages 对话消息列表（包含历史记录）
     * @return AI 回复内容
     */
    public String chatWithHistory(List<QwenChatRequest.QwenMessage> messages) {
        QwenChatRequest request = new QwenChatRequest();
        request.setModel(defaultModel);
        request.setResultFormat(resultFormat);
        QwenChatRequest.Input input = new QwenChatRequest.Input();
        input.setMessages(messages);
        request.setInput(input);

        try {
            QwenChatResponse response = qwenWebClient
                    .post()
                    .uri("/services/aigc/text-generation/generation")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(QwenChatResponse.class)
                    .block();

            if (response != null && response.getOutput() != null) {
                return response.getOutput().getText();
            } else {
                return "通义千问未返回有效内容";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "调用通义千问失败：" + e.getMessage();
        }
    }
}
