package com.demo.gis.repository;

import com.demo.gis.entity.TyphoonWaterDepth;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @program: javaArchitecture
 * @description: 水深
 * @author: LiuYunKai
 * @create: 2020-03-04 15:48
 **/
public interface TyphoonWaterDepthRepository extends MongoRepository<TyphoonWaterDepth, String> {

}
