package com.demo.springdataredis.controller;

import com.demo.springdataredis.common.result.Result;
import com.demo.springdataredis.service.TestRedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: javaArchitecture
 * @description: 测试
 * @author: LiuYunKai
 * @create: 2020-03-21 16:08
 **/
@Api(tags = "Redis测试控制器")
@RestController
@RequestMapping("v1/test")
public class TestRedisController {

    @Autowired
    private TestRedisService testRedisService;

    @ApiOperation(value = "新增Test", notes = "新增Test")
    @PostMapping("/addTest")
    public Result addTest(@RequestParam("key") String key, @RequestParam("value") String value, @RequestParam("ts") Long time) {
        return Result.success(testRedisService.setValue(key, value, time));
    }


    @ApiOperation(value = "查询Test", notes = "查询Test")
    @GetMapping("/getTest")
    public Result getTest(@RequestParam("key") String key) {
        return Result.success(testRedisService.getValue(key));
    }

    @ApiOperation(value = "新增MapTest", notes = "新增MapTest")
    @PostMapping("/addMapTest")
    public Result addMapTest(@RequestParam("key") String key) {
        return Result.success(testRedisService.setMapValue(key));
    }


    @ApiOperation(value = "查询MapTest", notes = "查询MapTest")
    @GetMapping("/getMapTest")
    public Result getMapTest(@RequestParam("key") String key) {
        return Result.success(testRedisService.getMapValue(key));
    }
}
