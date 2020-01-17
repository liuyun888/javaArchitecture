package com.demo.dynamic.proxy.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @program: javaArchitecture
 * @description: 运动员的代理
 * @author: LiuYunKai
 * @create: 2020-01-16 19:46
 **/
public class PlayerInvocation implements InvocationHandler {

    Object target;

    public PlayerInvocation(Object param) {
        this.target = param;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        training();
        Object value = method.invoke(this.target,args);
        training();


        return value;
    }

    private void training(){
        System.out.println("开始训练");
    }

}
