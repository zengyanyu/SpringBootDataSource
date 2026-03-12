package com.zengyanyu.system.mapper.primary;

import com.zengyanyu.system.entity.Userinfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserinfoMapper {
    void insert(Userinfo userinfo);

    List<Userinfo> listAll();
}
