package com.zengyanyu.system.mapper.primary;

import com.zengyanyu.system.entity.Userinfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zengyanyu
 */
@Mapper
public interface UserinfoMapper {
    /**
     * @param userinfo
     */
    void insert(Userinfo userinfo);

    /**
     * @return
     */
    List<Userinfo> listAll();
}
