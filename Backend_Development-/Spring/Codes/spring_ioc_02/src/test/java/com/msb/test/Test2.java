package com.msb.test;

import com.msb.bean.User;
import com.msb.service.impl.UserServiceImpl;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test2 {
    @Test
    public void testGetBean() {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "applicationContext2.xml");
        UserServiceImpl userService = context.getBean("userServiceImpl", UserServiceImpl.class);
        userService.add();
    }
}
