package com.demo.dynamic.proxy.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @program: javaArchitecture
 * @description: 代理
 * @author: LiuYunKai
 * @create: 2020-01-16 16:53
 **/
public class Invocation implements InvocationHandler {

    /**
     * 具体被监控的对象
     */
    private Object obj;

    public Invocation(Object object) {
        this.obj = object;
    }

    /**
     * invoke方法：在被监控行为将要执行时会被JVM拦截，被监控行为和行为实现方法会作为参数传递到invoke，
     * 以通知JVM这个被拦截方法是如何与当前次要业务方法绑定实现的。
     * <p>
     * invoke 三个参数：
     *      int v = 小明.eat(); 这个例子中，JVM会进行拦截，
     *      eat方法封装为Method类型对象；
     *      eat方法的所有实参封装到Object[];
     *      负责监控小明的代理对象作为proxy
     *
     * @param proxy 代理对象，方法中无用处
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //0.局部变量，接收主要业务方法执行完毕后的返回值
        Object value = null;
        //1.确认当前被拦截的行为
        String methodName = method.getName();
        //2.根据被拦截行为不同，决定主要业务和次要业务如何绑定执行
        if ("eat".equals(methodName)) {

            wash();

            value = method.invoke(this.obj, args);

        } else if ("wc".equals(methodName)) {

            // 具体的被监控对象 + 对应实参
            value = method.invoke(this.obj, args);

            wash();
        }
        return value;
    }

    private void wash() {
        System.out.println("洗手");
    }


}
