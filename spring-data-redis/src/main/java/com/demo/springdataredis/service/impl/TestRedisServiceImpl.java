package com.demo.springdataredis.service.impl;
import	java.util.HashMap;

import com.demo.springdataredis.common.utils.RedisUtil;
import com.demo.springdataredis.service.TestRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @program: javaArchitecture
 * @description: 单机测试
 * @author: LiuYunKai
 * @create: 2020-03-28 14:13
 **/
@Service
public class TestRedisServiceImpl implements TestRedisService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean setValue(String key, String value, long ts) {
        return redisUtil.set(key,value,ts);
    }

    @Override
    public String getValue(String key) {
        return (String) redisUtil.get(key);
    }

    @Override
    public boolean setMapValue(String key) {

        Map<String, Object> wmap = new HashMap<String, Object> (8);
        wmap.put("魏王","曹操");
        wmap.put("魏相","司马懿");
        redisUtil.hmset("魏国",wmap);

        Map<String, Object> hmap = new HashMap<String, Object> (8);
        hmap.put("汉王","刘备");
        hmap.put("汉相","诸葛");
        redisUtil.hmset("汉",hmap);

        Map<String, Object> qmap = new HashMap<String, Object> (8);
        qmap.put("吴王","孙权");
        qmap.put("吴相","鲁肃");
        redisUtil.hmset("吴",qmap);

        return redisUtil.hset(key,"自建","自建人物");
    }

    @Override
    public Map<Object, Object> getMapValue(String key) {
        return redisUtil.hmget(key);
    }
}
