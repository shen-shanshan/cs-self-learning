package com.msb.test;

import com.msb.bean.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test1 {
    @Test
    public void testGetBean() {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "applicationContext.xml");
        User user = context.getBean("user1", User.class);
        System.out.println(user);
    }
}
