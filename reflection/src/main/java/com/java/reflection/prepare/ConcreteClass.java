package com.java.reflection.prepare;

/**
 * @program: javaArchitecture
 * @description: 具体类
 * @author: LiuYunKai
 * @create: 2020-01-17 19:57
 **/
public class ConcreteClass extends BaseClass implements BaseInterface {

//    public String name = "ConcreteClass - name";


    public int publicInt;

    private String privateString = "privateString";

    protected boolean protectedBoolean;

    Object defaultObject;


    public ConcreteClass(int i) {
        this.publicInt = i;
    }

    @Override
    public void method1() {
        System.out.println("Method1 impl.");
    }

    @Override
    public int method2(String str) {
        System.out.println("Method2 impl.");
        return 2;
    }

    @Override
    public int method4(){
        System.out.println("Method4 override.");
        return 44;
    }

    public int method5(int i){
        System.out.println("Method5 override.");
        return 55;
    }

    /**
     * 内部类
     */
    public class ConcreteClassPublicClass{}
    private class ConcreteClassPrivateClass{}
    protected class ConcreteClassProtectedClass{}
    class ConcreteClassDefaultClass{}

    /**
     * 内部枚举
     */
    enum ConcreteClassDefaultEnum{}
    public enum ConcreteClassPublicEnum{}

    /**
     * 内部接口
     */
    public interface ConcreteClassPublicInterface{}
}
