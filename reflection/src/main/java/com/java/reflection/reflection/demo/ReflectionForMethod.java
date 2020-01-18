package com.java.reflection.reflection.demo;

import com.java.reflection.prepare.BaseClass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: javaArchitecture
 * @description: 与方法相关的反射方法
 * @author: LiuYunKai
 * @create: 2020-01-18 23:14
 **/
public class ReflectionForMethod {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        /**
         * 获得public方法
         *
         * 使用 getMethod()方法获的public class的方法;
         * 需要提供方法的名称、参数类型;
         * 如果class找不到指定的方法，则会继续向上从其父类中查找。
         */
        /*=========================================
         * 方法
         * ========================================
         */

        Class mapClass = HashMap.class;
        Method mapPut = mapClass.getMethod("put", Object.class, Object.class);
        //获取参数类型
        System.out.println(Arrays.toString(mapPut.getParameterTypes()));

        //获取返回类型
        System.out.println(mapPut.getReturnType());

        //访问修饰符
        System.out.println(Modifier.toString(mapPut.getModifiers()));

        /**
         * 调用public方法
         *
         * 利用Method.invoke() 方法调用指定的方法。
         */
        //调用方法
        Map<String, String> map = new HashMap<String, String>();
        mapPut.invoke(map, "key", "val");
        System.out.println(map);

        /**
         * 调用private方法
         *
         * 使用getDeclaredMethod()方法获取私有方法，然后关闭访问限制，即可调用。
         */
        /**
         * 访问私有方法
         */
        Method method3 = BaseClass.class.getDeclaredMethod("method3", null);

        method3.setAccessible(true);
        //静态方法的调用对象可以传入null
        method3.invoke(null, null);


    }

}
