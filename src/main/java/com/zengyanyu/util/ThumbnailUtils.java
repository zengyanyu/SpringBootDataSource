package com.zengyanyu.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 缩略图生成工具类
 */
public class ThumbnailUtils {

    /**
     * 按指定尺寸生成缩略图（等比例缩放，不足部分补白）
     *
     * @param sourceFile 源图片文件
     * @param targetFile 目标缩略图文件
     * @param width      缩略图宽度
     * @param height     缩略图高度
     * @param quality    图片质量（0.0-1.0，1.0为最高质量）
     * @throws IOException 图片处理异常
     */
    public static void generateThumbnail(File sourceFile, File targetFile, int width, int height, float quality) throws IOException {
        // 创建目标文件的父目录（如果不存在）
        File parentDir = targetFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        // 核心：生成缩略图（居中裁剪+补白，保证尺寸严格匹配）
        Thumbnails.of(sourceFile)
                .size(width, height)          // 指定目标尺寸
                .keepAspectRatio(true)        // 保持宽高比
                .crop(Positions.CENTER)       // 居中裁剪
                .outputQuality(quality)       // 设置图片质量
                .outputFormat("jpg")          // 输出格式（支持jpg/png/gif）
                .toFile(targetFile);          // 写入目标文件
    }

    /**
     * 按比例缩放生成缩略图（不固定尺寸）
     *
     * @param inputStream  源图片输入流（适用于上传文件）
     * @param outputStream 缩略图输出流
     * @param scale        缩放比例（0.5表示缩小到50%）
     * @param quality      图片质量
     * @throws IOException 图片处理异常
     */
    public static void generateThumbnailByScale(InputStream inputStream, OutputStream outputStream, double scale, float quality) throws IOException {
        Thumbnails.of(inputStream)
                .scale(scale)                 // 按比例缩放
                .outputQuality(quality)       // 图片质量
                .toOutputStream(outputStream); // 写入输出流
    }

}