package com.demo.gis.controller;

import com.demo.gis.common.result.Result;
import com.demo.gis.service.WaterDepthService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: javaArchitecture
 * @description: 水深
 * @author: LiuYunKai
 * @create: 2020-03-04 15:52
 **/
@Api("水深控制器")
@RestController
@RequestMapping("gis/waterDepth")
public class WaterDepthController {

    @Autowired
    private WaterDepthService waterDepthService;

    @PostMapping("waterDeptInfo")
    public Result addWaterDeptInfo(@RequestParam String path) {
        return Result.success(waterDepthService.addWaterDeptInfo(path));
    }

}
