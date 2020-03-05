package com.demo.gis.service;

import com.demo.gis.entity.TyphoonWaterDepth;

import java.util.List;

/**
 * @program: javaArchitecture
 * @description: 水深
 * @author: LiuYunKai
 * @create: 2020-03-04 15:55
 **/
public interface WaterDepthService {

    /**
     *
     * @param path
     * @return
     */
    List<TyphoonWaterDepth> addWaterDeptInfo(String path);
}
