package com.demo.dynamic.proxy.serviceimpl;

import com.demo.dynamic.proxy.service.BaseService;

/**
 * @program: javaArchitecture
 * @description: 主要业务，代理模式要求开发人员只关心主要业务
 * @author: LiuYunKai
 * @create: 2020-01-16 15:45
 **/
public class Person implements BaseService {
    @Override
    public void eat() {
        System.out.println("吃饭。。。");
    }

    @Override
    public void wc() {
        System.out.println("去厕所");
    }
}
