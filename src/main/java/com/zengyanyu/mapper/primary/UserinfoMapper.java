package com.zengyanyu.mapper.primary;

import com.zengyanyu.entity.Userinfo;
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
