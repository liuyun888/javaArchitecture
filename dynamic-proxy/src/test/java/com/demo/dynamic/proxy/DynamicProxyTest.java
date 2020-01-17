package com.demo.dynamic.proxy;

import com.demo.dynamic.proxy.service.BaseService;
import com.demo.dynamic.proxy.serviceimpl.James;
import com.demo.dynamic.proxy.serviceimpl.Person;
import com.demo.dynamic.proxy.util.PlayerProxyFactory;
import com.demo.dynamic.proxy.util.ProxyFactory;

/**
 * @program: javaArchitecture
 * @description: 动态代理测试
 * @author: LiuYunKai
 * @create: 2020-01-16 19:06
 **/
public class DynamicProxyTest {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {

        BaseService mike = (BaseService) ProxyFactory.builder(Person.class);

        mike.eat();


        BaseService james = (BaseService) PlayerProxyFactory.build(James.class);
        james.eat();

    }

}
