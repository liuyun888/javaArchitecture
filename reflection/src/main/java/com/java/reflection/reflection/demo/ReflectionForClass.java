package com.java.reflection.reflection.demo;

import com.java.reflection.prepare.BaseClass;
import com.java.reflection.prepare.ConcreteClass;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * @program: javaArchitecture
 * @description: 通过反射获取class对象
 * @author: LiuYunKai
 * @create: 2020-01-17 20:04
 **/
@Component
@Transactional(rollbackFor = Exception.class)
public class ReflectionForClass {

    public static void main(String[] args) throws ClassNotFoundException {
        /** 获取对象的Class实例 */
        /** 方式1：通过类的静态变量class获取Class对象 */
        Class concreteClass = ConcreteClass.class;

        System.out.println("通过静态对象获取的类名：" + concreteClass.getName());
        //Class的getCanonicalName()方法返回类的名称。在泛型中使用 java.lang.Class，可以帮助框架获取子类。
        System.out.println("通过静态对象获取的CanonicalName: " + concreteClass.getCanonicalName());

        /** 方式2：通过实例的getClass()方法获取Class对象 */
        Class concreteClass2 = new ConcreteClass(1).getClass();

        System.out.println("通过实例的getClass()方法获取的类名：" + concreteClass2.getName());
        //Class的getCanonicalName()方法返回类的名称。在泛型中使用 java.lang.Class，可以帮助框架获取子类。
        System.out.println("通过实例的getClass()方法获取的CanonicalName: " + concreteClass2.getCanonicalName());

        /** 方式3.通过java.lang.Class.forName(String 完整的类名)，完整的类名包含包名 */
        Class concreteClass3 = Class.forName("com.java.reflection.prepare.ConcreteClass");

        System.out.println("通过完整的类名获取的类名：" + concreteClass3.getName());
        //Class的getCanonicalName()方法返回类的名称。在泛型中使用 java.lang.Class，可以帮助框架获取子类。
        System.out.println("通过完整的类名获取的CanonicalName: " + concreteClass3.getCanonicalName());


        /*************** 原始类型的class、包装类型的TYPE均可以获得Class对象。*************/
        Class primative = boolean.class;
        System.out.println("原始类型boolean的class: " + primative.getCanonicalName());

        Class doubleClass = Double.TYPE;
        System.out.println("包装类型Double的class " + doubleClass.getCanonicalName());


        /************** 数组类型的class示例 *****************/
        Class arrayClass = Class.forName("[D");
        System.out.println(arrayClass.getCanonicalName());

        arrayClass = Class.forName("[B");
        System.out.println(arrayClass.getCanonicalName());

        arrayClass = Class.forName("[S");
        System.out.println(arrayClass.getCanonicalName());

        arrayClass = Class.forName("[C");
        System.out.println(arrayClass.getCanonicalName());

        arrayClass = Class.forName("[F");
        System.out.println(arrayClass.getCanonicalName());


        /**
         * 获取超类Super Class
         *
         * getSuperclass() 方法，返回类的超类(基类、父类)的class实例，
         * 如果该类是java.lang.Object、原始类型、接口则返回null。
         * 如果该class是数组形式，则该方法返回java.lang.Object。
         */

        Class superClass = ConcreteClass.class.getSuperclass();
        System.out.println("超类： " + superClass);

        System.out.println("Object的超类： " + Object.class.getSuperclass());
        System.out.println("数组的超类：" + String[].class.getSuperclass());

        /**
         * 获取公有的class
         *
         * Class的getClasses() 方法可以获取class的所有继承的超类、接口和自己定义的公有类、接口、枚举等的数组形式。
         */
        Class<?>[] commonClasses = ConcreteClass.class.getClasses();
        System.out.println("所有的公有类：" + Arrays.toString(commonClasses));

        /**
         * 获取该class自身声明的类
         *
         * getDeclaredClasses()获取当前类型自身定义的所有类、接口，并不包含从父类继承过来的类、接口。
         */
        Class<?>[] declaredClasses = ConcreteClass.class.getDeclaredClasses();
        System.out.println("自身声明的类： " + Arrays.toString(declaredClasses));


        /**
         * 获取定义该class的类
         *
         * class.getDeclaringClass()获取定义class的类。如果该类不是任何类或接口的成员，则返回null。
         */
        System.out.println("声明BaseClassInnerClass的类是：" + BaseClass.BaseClassInnerClass.class.getDeclaringClass());

        System.out.println("声明Double.TYPE的类是： " + Double.TYPE.getDeclaringClass());


        /**
         * 获取该class所在的包名
         *
         * getPackage()方法获取
         */
        System.out.println("ConcreteClass 所在包名：" + ConcreteClass.class.getPackage());

        /**
         * 获取该class的修饰符
         *
         * getModifiers()方法可以获取class实例的访问修饰符的个数。
         * java.lang.reflect.Modifier.toString()可以获取class的修饰符的字符串形式。
         */
        System.out.println("ConcreteClass 的访问修饰符个数：" + ConcreteClass.class.getModifiers());
        System.out.println("ConcreteClass 的修饰符：" + Modifier.toString(ConcreteClass.class.getModifiers()));

        /**
         * 获取该class的类型声明参数
         *
         * getTypeParameters()方法获取class的类型声明参数，如果有的话。比如集合框架的接口均制定了泛型。
         */
        System.out.println("java.util.Map 的类型声明参数：");
        Arrays.asList(Class.forName("java.util.Map").getTypeParameters()).forEach(s -> {
            System.out.println(s);
        });

        /**
         * 获取该class实现的接口
         *
         * getGenericInterfaces() 可以获取class已经实现的接口的数组形式，并包含泛型接口。
         * getInterfaces()方法会返回所有实现的接口，但是不包含泛型接口。
         */
        Arrays.asList(ConcreteClass.class.getGenericInterfaces()).forEach(s -> {
            System.out.println("ConcreteClass 已经实现的接口（含泛型）：" + s);
        });
        System.out.println("***************************");
        Arrays.asList(ConcreteClass.class.getInterfaces()).forEach(s -> {
            System.out.println("ConcreteClass 已经实现的接口（不含泛型）：" + s);
        });

        System.out.println("***************************");
        Arrays.asList(Class.forName("java.util.ArrayList").getGenericInterfaces()).forEach(s -> {
            System.out.println("java.util.ArrayList 已经实现的接口（含泛型）：" + s);
        });

        System.out.println("***************************");
        Arrays.asList(Class.forName("java.util.ArrayList").getInterfaces()).forEach(s -> {
            System.out.println("java.util.ArrayList 已经实现的接口（不含泛型）：" + s);
        });

        /**
         * 获取该class所以public方法
         *
         * getMethods()方法可以获取所有的public方法，包含父类、接口中继承来的public方法。
         */
        System.out.println("**********************");

        Arrays.asList(ConcreteClass.class.getMethods()).forEach(s -> {
            System.out.println("ConcreteClass 的所以public方法：" + s);
        });

        /**
         * 获取该class的所有的public构造器
         *
         * getConstructors()方法能够获取所有的public类型构造器
         */
        System.out.println("**********************");

        Arrays.asList(ConcreteClass.class.getConstructors()).forEach(s -> {
            System.out.println("ConcreteClass 的所有public构造函数：" + s);
        });

        /**
         * 获取该class所有的成员变量
         *
         * getFields()方法可以获取所有的public属性。包含父类、接口中的属性。
         */
        System.out.println("**********************");
        Arrays.asList(ConcreteClass.class.getFields()).forEach(s -> {
           System.out.println("ConcreteClass 的所有public属性：" + s);
        });

        /**
         * 获取该class的所有注解
         *
         * 首先给当前类加上俩注解
         *
         * getAnnotations()方法可以获取所有的注解。但是只有保留策略为RUNTIME的注解。
         */
        System.out.println("**********************");
        Arrays.asList(ReflectionForClass.class.getAnnotations()).forEach(s -> {
            System.out.println("ReflectionForClass 的所有注解：" + s);
        });
        System.out.println("**********************");
        Arrays.asList(ConcreteClass.class.getAnnotations()).forEach(s -> {
            System.out.println("ConcreteClass 的所有注解：" + s);
        });

    }

}
