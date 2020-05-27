package com.top.java.audition;

/**
 * @program: javaArchitecture
 * @description: == 和 equals 的区别
 * @author: haner
 * @create: 2020-05-26 23:02
 */
public class EqualsExplanation {

    public static void main(String[] args) {

        String x = "string";
        String y = "string";
        String z = new String("string");

        System.out.println(x==y);  //true
        System.out.println(x==z);  //false
        System.out.println(x.equals(y));  //true
        System.out.println(x.equals(z));  //true


        Cat c1 = new Cat("flMn");
        Cat c2 = new Cat("提莫");
        System.out.println("Cat:"+ c1 .equals(c2)); //false


        String s1 = new String("阿莫");
        String s2 = new String("阿莫");
        System.out.println(s1.equals(s2)); // true

        System.out.println("==============================================");


        String str1 ="通话";
        String str2 ="重地";
        System.out.println(String.format("str1: %d | str2: %d", str1.hashCode(),str2.hashCode()));
        System.out.println(str1.equals(str2));
    }

}
