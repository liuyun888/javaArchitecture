package com.demo.redis.service.impl;

import com.demo.redis.common.constant.Constant;
import com.demo.redis.entity.Test;
import com.demo.redis.repository.mongo.TestRepository;
import com.demo.redis.repository.redis.TestRedisRepository;
import com.demo.redis.service.TestService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @program: javaArchitecture
 * @description: 测试
 * @author: LiuYunKai
 * @create: 2020-03-17 16:59
 **/
@Service
public class TestServiceImpl implements TestService, Constant {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TestRedisRepository testRedisRepository;

    @Override
    public boolean addTest(String str) {

        if (StringUtils.isBlank(str)) {
          str = String.valueOf(System.currentTimeMillis());
        }
        Test test = new Test();
        test.setId(str);
        test.setJson("[{\"o\":" + str + ",\"a\":41.0,\"v\":0.0}]");

        testRedisRepository.setTest(test);

        testRepository.save(test);

        return true;
    }

    @Override
    public String getTest(Long time) {

        String jsonStr = testRedisRepository.getTest(String.valueOf(time));
        if (StringUtils.isBlank(jsonStr)) {
            Optional<Test> testOptional = testRepository.findById(String.valueOf(time));
            if (testOptional.isPresent()) {
                jsonStr = testOptional.get().getJson();
            }
        }

        return jsonStr;
    }
}
