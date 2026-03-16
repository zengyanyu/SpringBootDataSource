package com.zengyanyu.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

/**
 * 通义千问配置类
 *
 * @author zengyanyu
 */
@Configuration
public class QwenConfig {

    @Value("${ai.qwen.api-key}")
    private String apiKey;

    @Value("${ai.qwen.base-url}")
    private String baseUrl;

    @Value("${ai.qwen.timeout}")
    private int timeout;

    /**
     * 创建 WebClient 实例（用于调用通义千问 API）
     */
    @Bean
    public WebClient qwenWebClient() {
        // 配置 HTTP 客户端超时
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(timeout));

        return WebClient.builder()
                .baseUrl(baseUrl)
                // 设置认证请求头（通义千问固定格式）
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                // 配置超时和内存大小
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .build();
    }
}
