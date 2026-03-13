package com.zengyanyu.controller;

import com.zengyanyu.entity.QwenChatRequest;
import com.zengyanyu.service.impl.QwenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通义千问接口控制器
 *
 * @author zengyanyu
 */
@Api(tags = "通义千问聊天控制器对象")
@RestController
@RequestMapping("/api/ai/qwen")
public class QwenController {

    @Resource
    private QwenService qwenService;

    /**
     * 简单聊天接口（单轮对话）
     *
     * @param request 请求参数（包含 message 字段）
     * @return AI 回复
     */
    @ApiOperation("简单聊天接口（单轮对话）")
    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        String reply = qwenService.chat(message);
        HashMap<String, String> hashmap = new HashMap<>();
        hashmap.put("reply", reply);
        return hashmap;
    }

    /**
     * 多轮对话接口（支持历史记录）
     *
     * @param messages 消息列表（包含 role 和 content）
     * @return AI 回复
     */
    @ApiOperation("多轮对话接口（支持历史记录）")
    @PostMapping("/chat-history")
    public Map<String, String> chatWithHistory(@RequestBody List<QwenChatRequest.QwenMessage> messages) {
        String reply = qwenService.chatWithHistory(messages);
        HashMap<String, String> hashmap = new HashMap<>();
        hashmap.put("reply", reply);
        return hashmap;
    }
}
