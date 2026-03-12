package com.zengyanyu.system.service;

import com.zengyanyu.system.entity.Userinfo;

import java.util.List;

public interface IUserinfoService {

    /**
     * @param userinfo
     */
    void addUser(Userinfo userinfo);

    /**
     * @return
     */
    List<Userinfo> queryUsers();
}
