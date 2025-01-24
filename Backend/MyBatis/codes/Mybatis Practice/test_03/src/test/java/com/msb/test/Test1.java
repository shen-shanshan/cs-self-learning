package com.msb.test;

import com.msb.mapper.DeptMapper;
import com.msb.mapper.EmpMapper;
import com.msb.mapper.ProjectMapper;
import com.msb.pojo.Dept;
import com.msb.pojo.Emp;
import com.msb.pojo.Project;
import com.msb.pojo.ProjectRecord;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
    public void testOneToOne() {
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
        Emp emp = mapper.findEmpJoinDeptByEmpno(7499);
        System.out.println(emp);
    }

    @Test
    public void testOneToMany() {
        DeptMapper mapper = sqlSession.getMapper(DeptMapper.class);
        Dept dept = mapper.findDeptJoinEmpsByDeptno(20);
        System.out.println(dept);
        System.out.println("---------");
        List<Emp> empList = dept.getEmpList();
        for (Emp emp : empList) {
            System.out.println(emp);
        }
    }

    @Test
    public void testManyToMany() {
        ProjectMapper mapper = sqlSession.getMapper(ProjectMapper.class);
        Project project = mapper.findProjectJoinEmpsByPid(2);
        System.out.println(project.getPid());
        System.out.println(project.getPname());
        System.out.println(project.getMoney());
        List<ProjectRecord> projectRecords = project.getProjectRecords();
        for (ProjectRecord projectRecord : projectRecords) {
            Emp emp = projectRecord.getEmp();
            System.out.println(emp);
        }
    }

    // 级联查询
    @Test
    public void testFindByDetpno()   {
        DeptMapper deptMapper = sqlSession.getMapper(DeptMapper.class);
        Dept dept = deptMapper.findDeptByDeptno(20);
        System.out.println(dept.getDname());
        System.out.println(dept.getDeptno());
        System.out.println(dept.getLoc());
        List<Emp> empList = dept.getEmpList();
        for (Emp emp : empList) {
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