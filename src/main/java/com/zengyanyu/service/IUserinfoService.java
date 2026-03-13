package com.zengyanyu.service;

import com.zengyanyu.entity.Userinfo;

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
