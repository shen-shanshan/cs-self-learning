package com.msb.test;

import com.msb.dao.UserDao;
import com.msb.service.UserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test1 {

    @Test
    public void getBean() {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "applicationContext.xml");
        UserService userService = context.getBean("userServiceImpl", UserService.class);
        userService.addUser(1, "张三");

        // UserDao userDao = context.getBean(UserDao.class);
        // System.out.println(userDao.getClass().getSimpleName());
        // 输出：根据 UserDaoImpl 自动生成的代理类（$Proxy16）
    }

}
