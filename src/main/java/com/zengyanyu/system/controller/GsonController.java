package com.zengyanyu.system.controller;

import com.google.gson.Gson;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "Gson控制器")
public class GsonController {

    @Resource
    private Gson gson;

    @GetMapping("/toGson")
    public String toGson() {
        return gson.toJson("username:1,password:123");
    }
}
