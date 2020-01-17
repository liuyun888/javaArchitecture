package com.demo.dynamic.proxy.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @program: javaArchitecture
 * @description: 运动员代理工厂
 * @author: LiuYunKai
 * @create: 2020-01-16 19:55
 **/
public class PlayerProxyFactory {

    public static Object build(Class clazz) throws IllegalAccessException, InstantiationException {

        Object obj = clazz.newInstance();

        InvocationHandler handler = new PlayerInvocation(obj);

        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(),handler);
    }


}
