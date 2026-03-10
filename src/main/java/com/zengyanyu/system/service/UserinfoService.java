package com.zengyanyu.system.service;

import com.zengyanyu.system.entity.Log;
import com.zengyanyu.system.entity.Userinfo;
import com.zengyanyu.system.mapper.primary.UserinfoMapper;
import com.zengyanyu.system.mapper.secondary.LogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserinfoService {

    // 自动关联主数据源
    @Resource
    private UserinfoMapper userinfoMapper;

    // 自动关联从数据源
    @Resource
    private LogMapper logMapper;

    public void addUser(Userinfo userinfo) {
        // 操作主数据源
        userinfoMapper.insert(userinfo);
        // 操作从数据源
        logMapper.insert(new Log(userinfo.getId(), "新增用户"));
    }
}
