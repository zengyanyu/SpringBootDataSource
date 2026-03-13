package com.zengyanyu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zengyanyu.entity.Userinfo;
import com.zengyanyu.service.IUserinfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author zengyanyu
 */
@Slf4j
@RestController
@Api(tags = "用户信息控制器")
@RequestMapping("/userinfo")
public class UserinfoController {

    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private IUserinfoService userinfoService;

    @ApiOperation("保存")
    @PostMapping("/save")
    public String save(@RequestBody Userinfo userinfo) {
        userinfoService.addUser(userinfo);
        return "保存成功";
    }

    /**
     * 流式响应
     *
     * @param response
     * @return
     */
    @GetMapping(value = "/users", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Userinfo> flux(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        return Flux.fromIterable(this.userinfoService.queryUsers()).delayElements(Duration.ofSeconds(1));
    }

    /**
     * 流式响应
     *
     * @param response
     * @return
     */
    @GetMapping(path = "/stream")
    public StreamingResponseBody stream(HttpServletResponse response) {
        response.setContentType("text/event-stream;charset=utf-8");
        return outputStream -> {
            userinfoService.queryUsers().forEach(user -> {
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

}
