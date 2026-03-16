package com.zengyanyu.system.mapper.secondary;

import com.zengyanyu.system.entity.Log;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zengyanyu
 */
@Mapper
public interface LogMapper {
    /**
     * @param log
     */
    void insert(Log log);
}
