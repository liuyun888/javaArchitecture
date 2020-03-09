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




}


