package com.zengyanyu.system.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志记录注解
 *
 * @author zengyanyu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogRecord {

    /**
     * 操作名称
     */
    String value();

}

