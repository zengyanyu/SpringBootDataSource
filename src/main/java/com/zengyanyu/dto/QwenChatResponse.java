package com.zengyanyu.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 通义千问聊天响应实体
 *
 * @author zengyanyu
 */
@Getter
@Setter
public class QwenChatResponse implements Serializable {
    // 响应ID
    private String requestId;
    // 响应输出
    private Output output;

    /**
     * 输出内容
     */
    @Getter
    @Setter
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
    @Getter
    @Setter
    public static class Choice {
        private String finishReason;
        private QwenChatRequest.QwenMessage message;
    }

}
