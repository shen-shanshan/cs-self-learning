package com.msb.test;

import com.msb.dao.EmpDao;
import javafx.application.Application;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test1 {
    @Test
    public void testGetBean() {
        // 获取容器对象
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        // getBean() 返回的是 Object 对象
        // 方式一：
        // EmpDao empDao = applicationContext.getBean("empDao", EmpDao.class);
        // 方式二：
        EmpDao empDao = (EmpDao) applicationContext.getBean("empDao");
        empDao.addEmp();

        EmpDao empDao2 = (EmpDao) applicationContext.getBean("empDao2");
        empDao2.addEmp();
    }
}
