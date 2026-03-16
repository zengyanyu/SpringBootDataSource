package com.zengyanyu.system.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * 给Dto或者Entity包中的类添加Serializable接口
 *
 * @author zengyanyu
 */
public class AddSerializableTool {

    // 目标包路径（替换为你的实际路径）
    private static final Pattern CLASS_PATTERN = Pattern.compile("public class (\\w+)\\s*\\{");
    private static final Pattern SERIALIZABLE_PATTERN = Pattern.compile("implements.*Serializable");

    public static void main(String[] args) throws IOException {
        // 请替换为你要处理的根目录路径
        String rootDir = System.getProperty("user.dir") + "/src/main/java/com/zengyanyu/";
        // 扫描 dto 和 entity 目录
        scanDir(new File(rootDir + "dto"));
        scanDir(new File(rootDir + "entity"));
    }

    /**
     * 扫描目录
     *
     * @param dir 文件目录
     * @throws IOException IO异常
     */
    private static void scanDir(File dir) throws IOException {
        if (!dir.exists())
            return;
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                scanDir(file);
            } else if (file.getName().endsWith(".java")) {
                processJavaFile(file);
            }
        }
    }

    /**
     * @param file 文件
     * @throws IOException IO异常
     */
    private static void processJavaFile(File file) throws IOException {
        // 读取文件内容
        String content = new String(Files.readAllBytes(Paths.get(file.getPath())));

        // 如果已实现 Serializable，跳过
        if (SERIALIZABLE_PATTERN.matcher(content).find()) {
            return;
        }

        // 替换类定义，添加 implements Serializable
        String newContent = CLASS_PATTERN.matcher(content)
                .replaceFirst("public class $1 implements Serializable {");
        // 添加 serialVersionUID
//        newContent = newContent.replace("{", "{\n    private static final long serialVersionUID = 1L;\n");

        // 写回文件
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(newContent);
            System.out.println("已为 " + file.getName() + " 添加 Serializable 接口");
        }
    }
}
