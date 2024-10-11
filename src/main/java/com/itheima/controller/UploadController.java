package com.itheima.controller;

import com.itheima.anno.Log;
import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {
    @Log
    @PostMapping("/upload")
    public Result upload(String name, Integer age, MultipartFile image) throws IOException {
        log.info("文件上传: {},{}),{}", name, age, image);
        String filename = image.getOriginalFilename();
        int index = filename.lastIndexOf(".");
        String substring = filename.substring(index);
        String newFileName = UUID.randomUUID().toString() + substring;
        // 将文件传到本地保存
        image.transferTo(new File("D:/" + newFileName));
        return Result.success();
    }

}
