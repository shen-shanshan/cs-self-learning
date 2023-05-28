package com.msb.testProxy;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

// jdk 动态代理
public class Test1 {

    @Test
    public void jdkProxyTest() {

        // 被代理对象：dinner
        final Dinner dinner = new Person("张三");

        // 通过 Porxy 动态代理获得一个代理对象，在代理对象中，对某个方法进行增强
        // 1.ClassLoader loader：被代理对象的类加载器
        ClassLoader classLoader = dinner.getClass().getClassLoader();

        // 2.Class<?>[] interfaces：被代理对象所实现的所有接口
        // 通过反射获取被代理类的所有接口
        Class[] interaces = dinner.getClass().getInterfaces();

        // 3.InvocationHandler h：执行处理器对象，专门用于定义增强的规则
        InvocationHandler handler = new InvocationHandler() {

            // invoke：当我们让代理对象调用任何方法时，都会触发 invoke 方法的执行
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                // Object proxy：  代理对象                dinnerProxy
                // Method method： 被代理的方法             eat
                // Object[] args： 被代理的方法运行时的实参   "包子"

                // 默认每个方法都需要接收返回值，这样才能正常获取被代理方法的返回值（如果有的话）
                Object res = null;

                // 针对不同的方法进行不同的代理
                if (method.getName().equals("eat")) {
                    // before
                    System.out.println("饭前洗手");

                    // 让原有的 eat 的方法去运行
                    // 这里要传入的是原本被代理的对象 dinner，而不是代理对象 proxy
                    // 若在这里用 proxy 去执行 invoke，则会再次触发 invoke 的执行，造成无限循环
                    res = method.invoke(dinner, args);

                    // after
                    System.out.println("饭后刷碗");
                } else {
                    // 如果是其他方法,那么正常执行就可以了
                    res = method.invoke(dinner, args);
                }

                return res;
            }
        };

        // 生成代理对象：dinnerProxy
        // 默认返回的是 Object 类型的对象，需要做强制类型转换
        Dinner dinnerProxy = (Dinner) Proxy.newProxyInstance(classLoader, interaces, handler);
        dinnerProxy.eat("包子");
        dinnerProxy.drink();
    }
}

interface Dinner {
    void eat(String foodName);

    void drink();
}

class Person implements Dinner {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    @Override
    public void eat(String foodName) {
        System.out.println(name + "正在吃" + foodName);
    }

    @Override
    public void drink() {
        System.out.println(name + "正在喝茶");
    }
}

class Student implements Dinner {
    private String name;

    public Student(String name) {
        this.name = name;
    }

    @Override
    public void eat(String foodName) {
        System.out.println(name + "正在食堂吃" + foodName);
    }

    @Override
    public void drink() {
        System.out.println(name + "正在喝可乐");
    }
}
