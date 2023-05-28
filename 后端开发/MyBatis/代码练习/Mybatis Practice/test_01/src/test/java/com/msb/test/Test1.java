package com.msb.test;

import com.msb.pojo.Dept;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.io.InputStream;

public class Test1 {
    private SqlSession sqlSession;

    // 在 test 之前自动执行
    @Before
    public void init(){
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

    // 不用自己写 main() 方法
    @Test
    public void testFindAll(){
        // 根据 mapper 映射文件中 SQL 的 ID 与 SQL 语句的对应关系调用 SQL 语句
        // list 里装的是查询后返回的多个实体类对象
        List<Dept> list = sqlSession.selectList("findAll");
        for (Dept dept : list) {
            System.out.println(dept);
        }
    }

    // 在 test 之后自动执行
    @After
    public void release(){
        // 关闭 SQLSession
        sqlSession.close();
    }
}