package com.demo.dynamic.proxy.util;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @program: javaArchitecture
 * @description: 代理
 * @author: LiuYunKai
 * @create: 2020-01-16 18:57
 **/
public class ProxyFactory {

    /**
     *
     * JDK动态代理模式下，代理对象的数据类型应该由监控的行为来描述
     *
     * @param clazz class文件，监控类
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Object builder(Class clazz) throws IllegalAccessException, InstantiationException {

        //0.创建监控实例对象
        Object obj = clazz.newInstance();

        //1.创建一个处理对象
        InvocationHandler handler = new Invocation(obj);

        //2.向JVM申请一个负责监控obj的 代理对象
        /**
         * 三个参数：
         * loader： the class loader to define the proxy class
         * interfaces： the list of interfaces for the proxy class to implement
         * h： the invocation handler to dispatch method invocations to
         */
        Object $proxy = Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), handler);

        return $proxy;
    }

}
