package com.zengyanyu.util;

import com.zengyanyu.dto.UserDto;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zengyanyu
 */
public class BeanUtils {

    private BeanUtils() {
    }

    public static void main(String[] args) throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("Neld");
        userDto.setName("Stef");
        Map<String, Object> bean = getBean(userDto);
        System.out.println("bean = " + bean);
    }

    /**
     * javaBean转换成Map
     *
     * @param obj 要转换的javaBean对象
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getBean(Object obj) throws Exception {
        Map<String, Object> map = new HashMap<>();
        // 获取javaBean属性描述的包装对象
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass(), Object.class);
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            // 获取属性名
            String name = pd.getName();
            // 获取属性值
            Method readMethod = pd.getReadMethod();
            Object value = readMethod.invoke(obj);
            map.put(name, value);
        }
        return map;
    }

    /**
     * map转换成javaBean
     *
     * @param map
     * @param clazz 要转换的javaBean类型
     * @return
     * @throws Exception
     */
    public static <T> T map2bean(Map<String, Object> map, Class<T> clazz) throws Exception {
        // 获取javaBean属性描述的包装对象
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
        // 获取属性描述器
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        T obj = clazz.newInstance();
        for (PropertyDescriptor pd : pds) {
            // 获取属性名
            String name = pd.getName();
            // 获取属性值
            Method setMethod = pd.getWriteMethod();
            setMethod.invoke(obj, map.get(name));
        }
        return obj;
    }

}
