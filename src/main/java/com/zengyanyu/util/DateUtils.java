package com.zengyanyu.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期时间工具类（解决 SimpleDateFormat 线程不安全问题）
 * @author zengyanyu
 */
public class DateUtils {

    private DateUtils(){
    }

    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * Date 转 LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * LocalDateTime 转 Date
     *
     * @param localDateTime
     * @return
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 格式化 LocalDateTime
     *
     * @param localDateTime
     * @return
     */
    //
    public static String format(LocalDateTime localDateTime) {
        return format(localDateTime, DEFAULT_PATTERN);
    }

    /**
     * 自定义格式格式化 LocalDateTime
     *
     * @param localDateTime
     * @param pattern
     * @return
     */
    public static String format(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(formatter);
    }

    /**
     * 解析字符串为 LocalDateTime
     *
     * @param dateStr
     * @return
     */
    public static LocalDateTime parse(String dateStr) {
        return parse(dateStr, DEFAULT_PATTERN);
    }

    /**
     * 自定义格式解析字符串为 LocalDateTime
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static LocalDateTime parse(String dateStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateStr, formatter);
    }
}
