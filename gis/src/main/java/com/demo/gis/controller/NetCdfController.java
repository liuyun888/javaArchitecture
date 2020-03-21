package com.demo.gis.controller;


import com.demo.gis.common.result.Result;
import com.demo.gis.service.WindFieldService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.demo.gis.common.constant.Constant.SYMBOL_DOT;

/**
 * @author xutao
 * @date 2020-03-06 16:19
 * @description 风场 nc文件存储
 */
@Slf4j
@RestController
@RequestMapping("v1/data")
public class NetCdfController {

    @Autowired
    private WindFieldService windFieldService;

    /**
     * @param file 风场源文件
     * @return
     */
    @ApiOperation(value = "上传文件解析", notes = "上传文件解析")
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) {
        return Result.success(windFieldService.analyzeNCFile(file));
    }


    @PostMapping("/batch")
    public Result handleFileUpload(@RequestParam("file") MultipartFile originalFile) {
        // 源文件名称
        String originalFileName = originalFile.getOriginalFilename();

        MultipartFile file;
        if (isSpecifiedFile(originalFileName, FileTypeEnum.ZIP)) {
            // 处理 zip 文件
            return Result.success(windFieldService.analyzeNCZIPFile(originalFile));
        } else {
            file = originalFile;
            return Result.success(windFieldService.analyzeNCFile(file));
        }

    }

    private boolean isSpecifiedFile(final String fileName, FileTypeEnum typeEnum) {
        int dotIndex = fileName.lastIndexOf(SYMBOL_DOT);
//        RestAssert.fail(dotIndex == -1, String.format("没有发现文件扩展名:[%s]", fileName));
        // 获取文件扩展名
        String suffix = fileName.substring(dotIndex);
//        RestAssert.fail(Objects.equals(SYMBOL_DOT, suffix), String.format("文件扩展名不合法:[%s]", fileName));
        return FileTypeEnum.get(suffix.toLowerCase()) == typeEnum;
    }

    enum FileTypeEnum {

        // PDF 文件的扩展名
        PDF(".pdf"),

        // zip 压缩包扩展名
        ZIP(".zip"),

        ;

        private String type;

        FileTypeEnum(String type) {
            this.type = type;
        }


        /**
         * 根据 type 获取 {@link FileTypeEnum} 对象
         * <br />
         * 如果没有, 则返回 null
         *
         * @param type 文件扩展名
         * @return {@link FileTypeEnum} 对象
         */
        public static FileTypeEnum get(String type) {
            FileTypeEnum[] values = values();
            for (FileTypeEnum value : values) {
                if (Objects.equals(value.type, type)) {
                    return value;
                }
            }
            return null;
        }
    }





}


