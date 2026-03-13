package com.zengyanyu.entity;

import lombok.Data;

import java.util.List;

/**
 * 通义千问聊天请求实体
 *
 * @author zengyanyu
 */
@Data
public class QwenChatRequest {

    // 模型名称
    private String model;

    private Input input;

    // 响应格式（text/json）
    private String result_format;
    // 温度（0-1，值越高越随机）
    private Double temperature = 0.7;
    // 最大生成长度
    private Integer max_tokens = 1024;

    @Data
    public static class Input {
        // 对话消息列表
        private List<QwenMessage> messages;
    }

    /**
     * 消息实体
     */
    @Data
    public static class QwenMessage {
        // 角色：user（用户）、assistant（助手）、system（系统）
        private String role;
        // 消息内容
        private String content;

        public QwenMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
