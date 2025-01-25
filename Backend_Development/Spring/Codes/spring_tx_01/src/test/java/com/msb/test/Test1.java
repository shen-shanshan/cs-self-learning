package com.msb.test;

import com.msb.service.AccountService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @BelongsProject: Spring Practice
 * @BelongsPackage: com.msb.test
 * @Author: SSS
 * @Description:
 */
public class Test1 {

    @Test
    public void testTransMoney() {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "applicationContext.xml");
        AccountService accountService = context.getBean(AccountService.class);
        int rows = accountService.transMoney(1, 2, 100);
        System.out.println(rows);
    }

}
