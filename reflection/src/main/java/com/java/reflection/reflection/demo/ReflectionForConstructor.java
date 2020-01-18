package com.java.reflection.reflection.demo;

import com.java.reflection.prepare.ConcreteClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @program: javaArchitecture
 * @description: 反射在构造器中的使用
 * @author: LiuYunKai
 * @create: 2020-01-18 23:24
 **/
public class ReflectionForConstructor {

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        /**
         * 获取public构造器
         *
         * 使用getConstructor()方法获取指定的public构造器
         */
        Constructor<?> constructor = ConcreteClass.class.getConstructor(int.class);

        System.out.println(Arrays.toString(constructor.getParameterTypes()));

        Constructor<?> hashMapConstructor = HashMap.class.getConstructor(null);
        System.out.println(Arrays.toString(hashMapConstructor.getParameterTypes()));

        /**
         * 利用构造器初始化对象实例
         *
         * 利用constructor 实例的newInstance() 方法获初始化实例。
         */
        /**
         * 初始化实例
         */
        Object myObj = constructor.newInstance(10);
        Method myObjMethod = myObj.getClass().getMethod("method1", null);
        //输出 "Method1 impl."
        myObjMethod.invoke(myObj, null);

        HashMap<String,String> myMap = (HashMap<String,String>)hashMapConstructor.newInstance(null);



    }

}
