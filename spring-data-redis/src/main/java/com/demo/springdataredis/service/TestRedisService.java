package com.demo.springdataredis.service;

import java.util.Map;

/**
 * @program: javaArchitecture
 * @description: 单机测试
 * @author: LiuYunKai
 * @create: 2020-03-28 14:13
 **/
public interface TestRedisService {

    /** ===== String =====*/
    boolean setValue(String key, String value, long ts);

    String getValue(String key);

    boolean setMapValue(String key);

    Map<Object, Object> getMapValue(String key);
}
