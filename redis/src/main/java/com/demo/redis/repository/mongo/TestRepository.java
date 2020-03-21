package com.demo.redis.repository.mongo;

import com.demo.redis.entity.Test;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @program: javaArchitecture
 * @description: 测试
 * @author: LiuYunKai
 * @create: 2020-03-17 16:47
 **/
public interface TestRepository extends MongoRepository<Test, String> {
}
