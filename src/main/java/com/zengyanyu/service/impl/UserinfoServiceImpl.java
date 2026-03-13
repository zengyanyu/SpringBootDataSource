package com.zengyanyu.service.impl;

import com.zengyanyu.entity.Log;
import com.zengyanyu.mapper.primary.UserinfoMapper;
import com.zengyanyu.mapper.secondary.LogMapper;
import com.zengyanyu.entity.Userinfo;
import com.zengyanyu.service.IUserinfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author zengyanyu
 */
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
