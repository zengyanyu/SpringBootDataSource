package com.zengyanyu.system.util;

import com.zengyanyu.system.dto.PersonDto;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 异常堆栈信息工具类
 * 基于 StackTraceElement 解析异常的详细信息
 * JDK 8 及以上版本（兼容所有主流 Java 版本）。
 * 无需额外依赖，纯 JDK 原生 API 实现
 *
 * @author zengyanyu
 */
@Slf4j
public class ExceptionStackUtil {

    private ExceptionStackUtil() {
    }

    /**
     * 获取异常的完整堆栈信息字符串
     *
     * @param throwable 异常对象
     * @return 完整的堆栈信息字符串
     */
    public static String getFullStackTrace(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    /**
     * 解析异常堆栈，获取 StackTraceElement 列表
     *
     * @param throwable 异常对象
     * @return StackTraceElement 列表
     */
    public static List<StackTraceElement> getStackTraceElements(Throwable throwable) {
        if (throwable == null) {
            return new ArrayList<>();
        }
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        List<StackTraceElement> elements = new ArrayList<>();
        if (stackTrace != null && stackTrace.length > 0) {
            for (StackTraceElement element : stackTrace) {
                elements.add(element);
            }
        }
        return elements;
    }

    /**
     * 获取异常的关键信息（异常类型+消息+发生位置）
     *
     * @param throwable 异常对象
     * @return 简化的异常信息字符串
     */
    public static String getSimpleExceptionInfo(Throwable throwable) {
        if (throwable == null) {
            return "";
        }

        // 获取异常的核心堆栈元素（第一个元素是异常发生的具体位置）
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        String location = "未知位置";
        if (stackTrace != null && stackTrace.length > 0) {
            StackTraceElement coreElement = stackTrace[0];
            location = String.format("类：%s, 方法：%s, 行号：%d",
                    coreElement.getClassName(),
                    coreElement.getMethodName(),
                    coreElement.getLineNumber());
        }

        return String.format("异常类型：%s, \n\t异常消息：%s, 发生位置：%s",
                throwable.getClass().getName(),
                throwable.getMessage() == null ? "无" : throwable.getMessage(),
                location);
    }

    /**
     * 过滤指定包名的堆栈元素（只保留业务代码的堆栈，排除框架/第三方库）
     *
     * @param throwable     异常对象
     * @param packagePrefix 需要保留的包名前缀（如：com.yourproject）
     * @return 过滤后的 StackTraceElement 列表
     */
    public static List<StackTraceElement> filterStackTraceByPackage(Throwable throwable, String packagePrefix) {
        if (throwable == null || packagePrefix == null || packagePrefix.isEmpty()) {
            return new ArrayList<>();
        }

        List<StackTraceElement> filteredElements = new ArrayList<>();
        StackTraceElement[] stackTrace = throwable.getStackTrace();

        if (stackTrace != null) {
            for (StackTraceElement element : stackTrace) {
                // 只保留指定包名下的堆栈元素
                if (element.getClassName().startsWith(packagePrefix)) {
                    filteredElements.add(element);
                }
            }
        }
        return filteredElements;
    }

    /**
     * 打印过滤后的堆栈信息（方便调试）
     *
     * @param elements 堆栈元素列表
     */
    public static void printFilteredStackTrace(List<StackTraceElement> elements) {
        if (elements == null || elements.isEmpty()) {
            System.out.println("无匹配的堆栈信息");
            return;
        }
        System.out.println("===== 过滤后的异常堆栈 =====");
        for (StackTraceElement element : elements) {
            System.out.printf("类：%s | 方法：%s | 文件：%s | 行号：%d%n",
                    element.getClassName(),
                    element.getMethodName(),
                    element.getFileName(),
                    element.getLineNumber());
        }
    }

    public static void main(String[] args) {
//        test1();
        test3();
    }

    private static void test3() {
        test4();
    }

    private static void test4() {
        test5();
    }

    private static void test5() {
        test2();
    }

    private static void test2() {
        // 在 catch 块中使用
        try {
            // 业务代码
//            int result = 10 / 0;
            PersonDto personDto = null;
            String name = personDto.getName();
        } catch (Exception e) {
            // 记录简化异常信息到日志
            String simpleInfo = ExceptionStackUtil.getSimpleExceptionInfo(e);
            log.error("业务异常：{}", simpleInfo);

            // 如需详细排查，获取完整堆栈
            String fullStack = ExceptionStackUtil.getFullStackTrace(e);
            log.debug("完整堆栈：{}", fullStack);

            // 过滤业务包下的堆栈
//            List<StackTraceElement> businessStack = ExceptionStackUtil.filterStackTraceByPackage(e, "com");
//            ExceptionStackUtil.printFilteredStackTrace(businessStack);
        }
    }

    private static void test1() {
        try {
            // 模拟一个异常
            int result = 10 / 0;
        } catch (Exception e) {
//            // 1. 获取完整堆栈
//            System.out.println("【完整堆栈信息】");
//            System.out.println(getFullStackTrace(e));

            // 2. 获取简化信息
            System.out.println("\n【简化异常信息】");
            System.out.println(getSimpleExceptionInfo(e));

            // 3. 过滤堆栈（只保留当前包的堆栈）
            List<StackTraceElement> filtered = filterStackTraceByPackage(e, "com");
            System.out.println("\n【过滤后的堆栈】");
            printFilteredStackTrace(filtered);
        }
    }
}