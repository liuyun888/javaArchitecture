package com.demo.dynamic.proxy.serviceimpl;

import com.demo.dynamic.proxy.service.BaseService;

/**
 * @program: javaArchitecture
 * @description: 具体业务
 * @author: LiuYunKai
 * @create: 2020-01-16 19:41
 **/
public class James implements BaseService {
    @Override
    public void eat() {
        System.out.println("规律饮食。。。");
    }

    @Override
    public void wc() {
        System.out.println("去厕所");
    }

}
