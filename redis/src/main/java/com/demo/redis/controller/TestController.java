package com.demo.redis.controller;

import com.demo.redis.common.result.Result;
import com.demo.redis.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: javaArchitecture
 * @description: 测试
 * @author: LiuYunKai
 * @create: 2020-03-17 17:21
 **/
@Api(tags = "测试控制器")
@Slf4j
@RestController
@RequestMapping("v1/test")
public class TestController {
    @Autowired
    private TestService testService;


    @ApiOperation(value = "新增Test", notes = "新增Test")
    @PostMapping("/addTest")
    public Result upload(@RequestParam("time") String time) {
        return Result.success(testService.addTest(time));
    }


    @ApiOperation(value = "查询Test", notes = "查询Test")
    @GetMapping("/getTest")
    public Result upload(@RequestParam("time") Long time) {
        return Result.success(testService.getTest(time));
    }
}
