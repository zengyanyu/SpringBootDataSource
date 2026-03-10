package com.zengyanyu.system.mapper.primary;

import com.zengyanyu.system.entity.Userinfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserinfoMapper {
    void insert(Userinfo userinfo);
}
