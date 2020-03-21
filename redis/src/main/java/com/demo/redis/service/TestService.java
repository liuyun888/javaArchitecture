package com.demo.redis.service;

/**
 * @program: javaArchitecture
 * @description: 测试
 * @author: LiuYunKai
 * @create: 2020-03-17 16:55
 **/
public interface TestService {

    /**
     * 新增
     * @return
     */
    boolean addTest(String str);

    /**
     * 查询
     * @param time
     * @return
     */
    String getTest(Long time);

}
