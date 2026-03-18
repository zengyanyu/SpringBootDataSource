package com.zengyanyu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 通义千问聊天请求实体
 *
 * @author zengyanyu
 */
@Getter
@Setter
public class QwenChatRequest implements Serializable {

    // 模型名称
    private String model;

    private Input input;

    // 响应格式（text/json）
    private String resultFormat;

    @Getter
    @Setter
    public static class Input {
        // 对话消息列表
        private List<QwenMessage> messages;
    }

    /**
     * 消息实体
     */
    @Getter
    @Setter
    @AllArgsConstructor
    public static class QwenMessage {
        // 角色：user（用户）、assistant（助手）、system（系统）
        private String role;
        // 消息内容
        private String content;
    }
}
