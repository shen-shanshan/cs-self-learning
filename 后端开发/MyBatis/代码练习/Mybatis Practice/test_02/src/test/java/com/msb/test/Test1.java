package com.msb.test;

import com.msb.mapper.EmpMapper;
import com.msb.pojo.Dept;
import com.msb.pojo.Emp;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test1 {
    private SqlSession sqlSession;

    // 在 test 之前自动执行
    @Before
    public void init() {
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        InputStream resourceAsStream = null;
        try {
            resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory factory = ssfb.build(resourceAsStream);
        sqlSession = factory.openSession();
    }

    @Test
    public void testFindAll() {
        // sqlSession会根据映射文件自动生成一个接口的实现类
        EmpMapper empMapper = sqlSession.getMapper(EmpMapper.class);
        // 调用方法时，不再依赖sqlSession中的API，而是我们自己在接口中定义的方法
        List<Emp> emps = empMapper.findAll();
        for (Emp emp : emps) {
            System.out.println(emp);
        }
    }

    @Test
    public void testFindByDeptnoAndSal() {
        EmpMapper empMapper = sqlSession.getMapper(EmpMapper.class);
        List<Emp> emps = empMapper.findByDeptnoAndSal(20, 3000);
        for (Emp emp : emps) {
            System.out.println(emp);
        }
    }

    @Test
    public void testFindByEmpno() {
        EmpMapper empMapper = sqlSession.getMapper(EmpMapper.class);
        // 方式一：基本数据类型
        /*Emp emp = empMapper.findByEmpno(7499);
        System.out.println(emp);*/

        // 方式二：Map
        /*Map<String, Object> map = new HashMap<>();
        map.put("deptno", 20);
        map.put("sal", 3000);
        List<Emp> emps = empMapper.findByDeptnoAndSal2(map);
        for (Emp emp : emps) {
            System.out.println(emp);
        }*/

        // 方式三：引用类型
        Emp emp = new Emp();
        emp.setDeptno(20);
        emp.setSal(3000.0);
        List<Emp> emps = empMapper.findByDeptnoAndSal3(emp);
        for (Emp emp0 : emps) {
            System.out.println(emp0);
        }

        // 方式四：多个引用类型
    }

    @Test
    public void testFindByEname() {
        EmpMapper empMapper = sqlSession.getMapper(EmpMapper.class);
        // List<Emp> emps = empMapper.findByEname("%a%");
        // 也可以在mapper.xml文件中拼接%
        List<Emp> emps = empMapper.findByEname("a");
        for (Emp emp : emps) {
            System.out.println(emp);
        }
    }

    // 在 test 之后自动执行
    @After
    public void release() {
        // 关闭 SQLSession
        sqlSession.close();
    }
}