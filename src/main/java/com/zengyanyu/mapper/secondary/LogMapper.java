package com.zengyanyu.mapper.secondary;

import com.zengyanyu.entity.Log;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper {
    void insert(Log log);
}
