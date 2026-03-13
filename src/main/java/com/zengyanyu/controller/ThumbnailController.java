package com.zengyanyu.controller;

import com.zengyanyu.util.ThumbnailUtils;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

/**
 * 缩略图测试接口
 * @author zengyanyu
 */
@RestController
public class ThumbnailController {

    public static void main(String[] args) {
        try {
            // 源图片路径（替换为你本地的图片路径）
            File sourceFile = new File("D:/upload/1.png");
            // 目标缩略图路径
            File targetFile = new File("D:/upload/2_thumbnail.jpg");

            // 生成 200x200 的缩略图，质量 0.8
            ThumbnailUtils.generateThumbnail(sourceFile, targetFile, 200, 200, 1.0f);
        } catch (IOException e) {
        }
    }

}
