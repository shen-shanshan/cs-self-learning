package com.msb.test;

import com.msb.bean.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLOutput;

public class Test1 {
    @Test
    public void testGetBean() {
        // 通过反射创建对象，执行的是无参构造方法
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "applicationContext.xml");
        // 若为懒加载，则需要下面调用 user1 以后才会创建对象
        // User user1 = applicationContext.getBean("user1", User.class);
        // System.out.println(user1);

        User user2 = applicationContext.getBean("user2", User.class);
        System.out.println(user2);
    }
}
