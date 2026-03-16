package com.zengyanyu.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 通义千问聊天响应实体
 *
 * @author zengyanyu
 */
@Data
public class QwenChatResponse implements Serializable {
    // 响应ID
    private String requestId;
    // 响应输出
    private Output output;
    // 用量统计
    private Usage usage;

    /**
     * 输出内容
     */
    @Data
    public static class Output {
        // 文本响应
        private String text;
        // 结束原因（stop=正常结束）
        private String finishReason;
        // 选择列表（兼容多返回场景）
        private List<Choice> choices;
    }

    /**
     * 选择项
     */
    @Data
    public static class Choice {
        private String finishReason;
        private QwenChatRequest.QwenMessage message;
    }

    /**
     * 用量统计
     */
    @Data
    public static class Usage {
        // 输入令牌数
        private Integer inputTokens;
        // 输出令牌数
        private Integer outputTokens;
        // 总令牌数
        private Integer totalTokens;
    }
}
