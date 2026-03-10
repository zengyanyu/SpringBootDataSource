package com.zengyanyu.system.controller;

import com.zengyanyu.system.entity.Userinfo;
import com.zengyanyu.system.service.UserinfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zengyanyu
 */
@Slf4j
@RestController
@Api(tags = "用户信息控制器")
@RequestMapping("/userinfo")
public class UserinfoController {

    @Resource
    private UserinfoService userinfoService;

    @ApiOperation("保存")
    @PostMapping("/save")
    public String save(@RequestBody Userinfo userinfo) {
        userinfoService.addUser(userinfo);
        return "保存成功";
    }

}
