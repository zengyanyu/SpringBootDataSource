/*
 * Copyright (c) 2026, 曾衍育 All rights reserved.
 * 自定义License声明
 * ZENGYANYU PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.zengyanyu.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 自动为Java文件的类添加@author注解的工具类
 *
 * @author zengyanyu
 */
public class JavaAuthorAnnotationAdder {

    // 自定义作者名称，你可以根据需要修改
    private static final String AUTHOR_NAME = "zengyanyu";
    // 需要添加的@author注解行
    private static final String AUTHOR_ANNOTATION = " * @author " + AUTHOR_NAME;

    public static void main(String[] args) throws IOException {
        // 请替换为你要处理的根目录路径
        String rootDir = System.getProperty("user.dir") + "/src/main/java";

        // 遍历指定目录下的所有Java文件
        traverseDirectory(Paths.get(rootDir));
        System.out.println("处理完成！");
    }

    /**
     * 递归遍历目录，处理所有.java文件
     *
     * @param dir 要遍历的目录路径
     * @throws IOException IO异常
     */
    private static void traverseDirectory(Path dir) throws IOException {
        if (!Files.isDirectory(dir)) {
            System.err.println(dir + " 不是有效的目录！");
            return;
        }

        // 遍历目录中的所有文件/子目录
        Files.list(dir).forEach(path -> {
            try {
                if (Files.isDirectory(path)) {
                    // 递归处理子目录
                    traverseDirectory(path);
                } else {
                    // 处理.java文件
                    String fileName = path.getFileName().toString();
                    if (fileName.endsWith(".java")) {
                        processJavaFile(path);
                    }
                }
            } catch (IOException e) {
                System.err.println("处理文件 " + path + " 时出错：" + e.getMessage());
            }
        });
    }

    /**
     * 处理单个Java文件，检查并添加@author注解
     *
     * @param javaFile Java文件路径
     * @throws IOException IO异常
     */
    private static void processJavaFile(Path javaFile) throws IOException {
        // 读取文件所有行
        List<String> lines = Files.readAllLines(javaFile, StandardCharsets.UTF_8);
        List<String> newLines = new ArrayList<>();

        boolean hasAuthorAnnotation = false;
        boolean inClassJavadoc = false;
        int classLineIndex = -1;

        // 第一步：找到类定义位置，并检查其Javadoc是否有@author
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();

            // 检测Javadoc开始
            if (line.startsWith("/**")) {
                inClassJavadoc = true;
            }

            // 检测Javadoc中的@author
            if (inClassJavadoc && line.contains("@author")) {
                hasAuthorAnnotation = true;
            }

            // 检测Javadoc结束
            if (inClassJavadoc && line.endsWith("*/")) {
                inClassJavadoc = false;
            }

            // 检测类/接口/枚举定义（简化版，仅处理常见情况）
            if ((line.startsWith("public class") || line.startsWith("class") ||
                    line.startsWith("public abstract class") ||
                    line.startsWith("@") || // 判断类名上是否有注解，在有注解的上一行进行添加
                    line.startsWith("public interface") || line.startsWith("interface") ||
                    line.startsWith("public enum") || line.startsWith("enum")) &&
                    !line.contains("//") && !line.contains("/*")) {
                classLineIndex = i;
                break; // 只处理第一个类定义（单个文件多个类的情况可扩展）
            }
        }

        // 如果已经有@author注解，直接保留原内容
        if (hasAuthorAnnotation) {
            return;
        }

        // 第二步：添加@author注解（分两种情况：有Javadoc/无Javadoc）
        if (classLineIndex == -1) {
            // 文件中未找到类定义，跳过
            return;
        }

        // 查找类定义上方的Javadoc结束位置
        int javadocEndIndex = -1;
        for (int i = classLineIndex - 1; i >= 0; i--) {
            String line = lines.get(i).trim();
            if (line.endsWith("*/")) {
                javadocEndIndex = i;
                break;
            }
        }

        // 情况1：类有Javadoc注释，在Javadoc中添加@author
        if (javadocEndIndex != -1) {
            for (int i = 0; i < lines.size(); i++) {
                newLines.add(lines.get(i));
                // 在Javadoc结束行的上一行插入@author
                if (i == javadocEndIndex - 1 && !lines.get(i).contains("@author")) {
                    newLines.add(AUTHOR_ANNOTATION);
                }
            }
        }
        // 情况2：类没有Javadoc注释，创建新的Javadoc并添加@author
        else {
            for (int i = 0; i < lines.size(); i++) {
                if (i == classLineIndex) {
                    // 添加Javadoc注释
                    newLines.add("/**");
                    newLines.add(AUTHOR_ANNOTATION);
                    newLines.add(" */");
                }
                newLines.add(lines.get(i));
            }
        }

        // 第三步：将修改后的内容写回文件
        Files.write(javaFile, newLines, StandardCharsets.UTF_8);
        System.out.println("已为文件 " + javaFile + " 添加@author注解");
    }
}