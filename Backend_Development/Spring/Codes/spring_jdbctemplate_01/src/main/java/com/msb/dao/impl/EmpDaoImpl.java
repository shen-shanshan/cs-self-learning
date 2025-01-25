package com.msb.dao.impl;

import com.msb.dao.EmpDao;
import com.msb.pojo.Emp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @BelongsProject: Spring Practice
 * @BelongsPackage: com.msb.dao.impl
 * @Author: SSS
 * @Description:
 */
@Repository
public class EmpDaoImpl implements EmpDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 查询员工个数
     *
     * @return int
     */
    @Override
    public int findEmpCount() {
        String sql = "select count(1) from emp";
        /**
         * 参数：
         * 1.SQL 语句
         * 2.返回值类型的字节码
         */
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    /**
     * 根据员工编号查询员工信息
     *
     * @param empno
     * @return Emp
     */
    @Override
    public Emp findByEmpno(int empno) {
        /**
         * 参数：
         * 1.SQL 语句
         * 2.RowMapper 指定查询的结果集用哪个来进行封装
         * 3.SQL 语句需要传入的实参
         */
        String sql = "select * from emp where empno = ?";
        BeanPropertyRowMapper<Emp> rowMapper = new BeanPropertyRowMapper<Emp>(Emp.class);
        Emp emp = jdbcTemplate.queryForObject(sql, rowMapper, empno);
        return emp;
    }

    /**
     * 查询多个员工对象集合
     *
     * @param deptno
     * @return List<Emp>
     */
    @Override
    public List<Emp> findByDeptnpo(int deptno) {
        /**
         * 参数：
         * 1.SQL 语句
         * 2.RowMapper 指定查询的结果集用哪个来进行封装
         * 3.SQL 语句需要传入的实参
         */
        String sql = "select * from emp where deptno = ?";
        BeanPropertyRowMapper<Emp> rowMapper = new BeanPropertyRowMapper<Emp>(Emp.class);
        List<Emp> emps = jdbcTemplate.query(sql, rowMapper, deptno);
        return emps;
    }

    /**
     * 增加员工信息
     *
     * @param emp
     * @return int
     */
    @Override
    public int addEmp(Emp emp) {
        String sql = "insert into emp values(DEFAULT,?,?,?,?,?,?,?)";
        Object[] args = {emp.getEname(), emp.getJob(), emp.getMgr()
                , emp.getHiredate(), emp.getSal(), emp.getComm(), emp.getDeptno()};
        return jdbcTemplate.update(sql, args);
    }

}
