package com.zengyanyu.mapper.secondary;

import com.zengyanyu.entity.Log;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zengyanyu
 */
@Mapper
public interface LogMapper {
    void insert(Log log);
}
