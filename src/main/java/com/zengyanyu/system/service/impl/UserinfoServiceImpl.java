package com.zengyanyu.system.service.impl;

import com.zengyanyu.system.entity.Log;
import com.zengyanyu.system.entity.Userinfo;
import com.zengyanyu.system.mapper.primary.UserinfoMapper;
import com.zengyanyu.system.mapper.secondary.LogMapper;
import com.zengyanyu.system.service.IUserinfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserinfoServiceImpl implements IUserinfoService {

    // 自动关联主数据源
    @Resource
    private UserinfoMapper userinfoMapper;

    // 自动关联从数据源
    @Resource
    private LogMapper logMapper;

    @Transactional
    @Override
    public void addUser(Userinfo userinfo) {
        // 操作主数据源
        userinfoMapper.insert(userinfo);
        // 操作从数据源
        logMapper.insert(new Log(userinfo.getId(), "新增用户"));
    }

    @Override
    public List<Userinfo> queryUsers() {
        return userinfoMapper.listAll();
    }
}
