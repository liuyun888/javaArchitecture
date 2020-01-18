package com.java.reflection.reflection.demo;

import com.java.reflection.prepare.ConcreteClass;

import java.lang.reflect.Field;

/**
 * @program: javaArchitecture
 * @description: 反射在属性（成员变量）中的应用
 * @author: LiuYunKai
 * @create: 2020-01-18 00:51
 **/
public class ReflectionForField {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        /**
         * 获取public（成员变量）
         *
         * 1、通过class中提到的getFields()
         * 2、通过 getField(变量名)，来获取指定的变量，这个方法会在 该class–>接口–>父类 的顺序寻找指定的属性。
         */

        /**
         * 场景一：我们在ConcreteClass类、BaseClass类、BaseInterface接口分别增加一个属性：
         * public String name = "ConcreteClass - name";
         * public String name = "BaseClass - name";
         * public String name = "BaseInterface - name";
         *
         * 此时结果为：
         * public java.lang.String com.java.reflection.prepare.ConcreteClass.name
         */

        /**
         * 场景二：我们在BaseClass类、BaseInterface接口分别增加一个属性：
         * public String name = "BaseClass - name";
         * public String name = "BaseInterface - name";
         *
         * 此时结果为：
         * public static final java.lang.String com.java.reflection.prepare.BaseInterface.name
         */

        /**
         * 场景三：我们仅仅在BaseClass类增加一个属性：
         * public String name = "BaseClass - name";
         *
         * 此时结果为：
         * public java.lang.String com.java.reflection.prepare.BaseClass.name
         */

        /**
         * 场景四：当将BaseClass中的name属性也注释掉后
         *
         * 则出现异常：
         *  java.lang.NoSuchFieldException: name
         */
        System.out.println("************************");
        try {
            System.out.println(ConcreteClass.class.getField("name"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }


        /**
         * 获取声明该属性的类型
         *
         * 使用属性的 getDeclaringClass()
         */
        System.out.println("***********获取声明该属性的类型*************");
        try {
            System.out.println(ConcreteClass.class.getField("privateString").getDeclaringClass());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        System.out.println("************************");
        try {
            System.out.println(ConcreteClass.class.getField("publicInt").getDeclaringClass());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        System.out.println("************************");
        try {
            System.out.println(ConcreteClass.class.getField("interfaceInt").getDeclaringClass());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        /**
         * 获取该属性的类型
         *
         * getType()方法返回该属性的类型的class实例。
         */
        System.out.println("***********获取该属性的类型*************");
        try {
            System.out.println(ConcreteClass.class.getField("interfaceInt").getType());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        /**
         * get/set public的属性值
         *
         * Field.get(Object) 获取该属性的值。
         */
        Field field = ConcreteClass.class.getField("publicInt");

        System.out.println("属性的类型：" + field.getType());

        ConcreteClass obj = new ConcreteClass(7);

        // Field.get()返回的是一个Object类型，如果是原始类型则返回其包装类型。
        // 如果是final属性，set() 方法抛出java.lang.IllegalAccessException。
        System.out.println("获取属性值：" + field.get(obj));

        field.set(obj,77);
        System.out.println("获取修改后的属性值：" + field.get(obj));

        /**
         * Get/Set private类型的属性值
         *
         * java中在类之外是不能访问private变量的。但是通过反射可以关闭检查访问修饰符的机制。
         */
        Field privateField = ConcreteClass.class.getDeclaredField("privateString");
        // 直接访问会报： Exception in thread "main" java.lang.IllegalAccessException: Class com.java.reflection.reflection
        // .demo
        // .ReflectionForField can not access a member of class com.java.reflection.prepare.ConcreteClass with
        // modifiers "private"
        //	at sun.reflect.Reflection.ensureMemberAccess(Reflection.java:102)
        //System.out.println(privateField.get(obj));

        /**
         * 设置可访问机制Field.setAccessible(true)
         */
        Field privateField2 = ConcreteClass.class.getDeclaredField("privateString");
        privateField2.setAccessible(true);
        System.out.println("获取私有属性："+privateField2.get(obj));

        privateField2.set(obj, "修改后私有属性[value]");
        System.out.println("获取修改后私有属性："+privateField2.get(obj));



    }

}
