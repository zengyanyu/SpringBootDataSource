package com.zengyanyu.system.mapper.secondary;

import com.zengyanyu.system.entity.Log;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper {
    void insert(Log log);
}
