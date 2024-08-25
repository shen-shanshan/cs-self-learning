package com.msb.test;

import com.msb.mapper.DeptMapper;
import com.msb.mapper.EmpMapper;
import com.msb.mapper.EmpMapper2;
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
import java.util.Date;
import java.util.List;

public class Test2 {
    private SqlSession sqlSession;

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
    public void testAddDept() {
        DeptMapper mapper = sqlSession.getMapper(DeptMapper.class);
        Dept dept = new Dept(null, "JAVA", "北京");
        int i = mapper.addDept(dept);
        System.out.println(i);
        // 主键自增后直接回填到该对象对应的属性上
        System.out.println(dept.getDeptno());
        sqlSession.commit();
    }

    @Test
    public void testAddEmp() {
        /*EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
        mapper.addEmp(new Emp(null, "王悦能", "", 7521, new Date()
                , 2314.0, 100.0, 10));
        sqlSession.commit();*/

        // 动态SQL
        EmpMapper2 mapper = sqlSession.getMapper(EmpMapper2.class);
        Emp emp = new Emp();
        emp.setEname("SMITH");
        List<Emp> emps = mapper.findByCondition(emp);
        for (Emp emp1 : emps) {
            System.out.println(emp1);
        }
    }

    @After
    public void release() {
        // 关闭 SQLSession
        sqlSession.close();
    }
}