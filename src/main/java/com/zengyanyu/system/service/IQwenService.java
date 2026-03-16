package com.zengyanyu.system.service;

import com.zengyanyu.system.dto.QwenChatRequest;

import java.util.List;

/**
 * @author zengyanyu
 */
public interface IQwenService {

    /**
     * 发送聊天请求给通义千问
     *
     * @param userMessage 用户输入的问题
     * @return AI 回复内容
     */
    String chat(String userMessage);

    /**
     * 支持多轮对话的聊天方法（扩展）
     *
     * @param messages 对话消息列表（包含历史记录）
     * @return AI 回复内容
     */
    String chatWithHistory(List<QwenChatRequest.QwenMessage> messages);
}
