package com.zengyanyu.system.service.impl;

import com.zengyanyu.system.entity.Userinfo;
import com.zengyanyu.system.mapper.primary.UserinfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("blockUserService")
public class UserService {

    @Resource
    private UserinfoMapper userinfoMapper;

    public List<Userinfo> queryUsers() {
        return userinfoMapper.listAll();
    }
}
