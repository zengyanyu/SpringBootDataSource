package com.zengyanyu.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Spring 上下文工具类（非 Spring 管理的类中获取 Bean/配置）
 *
 * @author zengyanyu
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringContextUtil.applicationContext = context;
    }

    /**
     * 获取 ApplicationContext
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 根据名称获取 Bean
     *
     * @param beanName
     * @param <T>
     * @return
     */
    public static <T> T getBean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

    /**
     * 根据类型获取 Bean
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> getBeanOfType(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }

    /**
     * 获取环境变量
     *
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        Environment environment = applicationContext.getEnvironment();
        return environment.getProperty(key);
    }

    /**
     * 获取环境变量（带默认值）
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getProperty(String key, String defaultValue) {
        Environment environment = applicationContext.getEnvironment();
        return environment.getProperty(key, defaultValue);
    }
}
