package com.zengyanyu.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zengyanyu.system.entity.Userinfo;
import com.zengyanyu.system.service.impl.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 流式响应控制器
 *
 * @author zengyanyu
 */
@RestController
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private ObjectMapper objectMapper;

    @GetMapping(value = "/users", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Userinfo> flux(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        return Flux.fromIterable(this.userService.queryUsers()).delayElements(Duration.ofSeconds(1));
    }

    @GetMapping(path = "/stream")
    public StreamingResponseBody stream(HttpServletResponse response) {
        response.setContentType("text/event-stream;charset=utf-8");
        return outputStream -> {
            userService.queryUsers().forEach(user -> {
                try {
                    String json = objectMapper.writeValueAsString(user) + "\n";
                    outputStream.write(json.getBytes());
                    outputStream.flush();
                    // 模拟效果
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        };
    }
};
