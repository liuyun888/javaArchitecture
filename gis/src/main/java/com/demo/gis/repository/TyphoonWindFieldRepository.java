package com.demo.gis.repository;

import com.demo.gis.entity.TyphoonWindField;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @program: ops-china
 * @description: 风场
 * @author: LiuYunKai
 * @create: 2020-03-09 10:25
 **/
public interface TyphoonWindFieldRepository extends MongoRepository<TyphoonWindField, String> {
}
