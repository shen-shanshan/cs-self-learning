package com.msb.test;

import com.msb.pojo.Emp;
import com.msb.service.EmpService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.List;

/**
 * @BelongsProject: Spring Practice
 * @BelongsPackage: com.msb.test
 * @Author: SSS
 * @Description:
 */
public class Test1 {

    @Test
    public void testEmpService() {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "applicationContext.xml");
        EmpService empService = context.getBean(EmpService.class);
        int empCount = empService.findEmpCount();
        System.out.println(empCount);

        System.out.println("--------------------------------");
        Emp emp = empService.findByEmpno(7521);
        System.out.println(emp);

        System.out.println("--------------------------------");
        List<Emp> emps = empService.findByDeptno(20);
        emps.forEach(System.out::println);

        System.out.println("--------------------------------");
        empService.addEmp(new Emp(null, "王悦能", "SALESMAN", 7521
                , new Date(), 2000.0, 100.0, 10));
    }

}
