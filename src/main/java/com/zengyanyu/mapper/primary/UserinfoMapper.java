package com.zengyanyu.mapper.primary;

import com.zengyanyu.entity.Userinfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserinfoMapper {
    void insert(Userinfo userinfo);

    List<Userinfo> listAll();
}
