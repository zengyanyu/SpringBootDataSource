package com.zengyanyu.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 新增填充
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {

    }

    /**
     * 修改填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
