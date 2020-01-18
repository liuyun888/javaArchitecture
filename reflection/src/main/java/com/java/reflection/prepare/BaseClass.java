package com.java.reflection.prepare;

/**
 * @program: javaArchitecture
 * @description: 基础类
 * @author: LiuYunKai
 * @create: 2020-01-17 19:48
 **/
public class BaseClass {

//    public String name = "BaseClass - name";



    public int baseInt;

    private static void method3(){
        System.out.println("Private Method3");
    }

    public int method4(){
        System.out.println("Method4");
        return 4;
    }

    public static int method5(){
        System.out.println("Method5");
        return 5;
    }

    void method6(){
        System.out.println("Method6");
    }

    /**
     * public 的内部类
     */
    public class BaseClassInnerClass{}

    /**
     * public 的枚举
     */
    public enum BaseClassInnerEnum{}

}
