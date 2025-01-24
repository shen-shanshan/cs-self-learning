package com.msb.mapper;

import com.msb.pojo.Emp;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 代理模式开发：
 * 不用写实现类，而是在映射文件中，针对接口去写SQL语句。
 */
public interface EmpMapper {
    /**
     * 该方法用于查询全部的员工信息
     *
     * @return 全部员工信息封装的Emp对象的List集合
     */
    List<Emp> findAll();

    /**
     * 根据员工编号查询单个员工信息的方法
     *
     * @param empno 员工编号
     * @return 如果找到了返回Emp对象, 找不到返回null
     */
    Emp findByEmpno(int empno);

    /**
     * 根据员工编号和薪资下限去查询员工信息
     *
     * @param empno 员工编号
     * @param sal   薪资下限
     * @return 多个Emp对象的List集合
     */
    List<Emp> findByDeptnoAndSal(@Param("deptno") int deptno, @Param("sal") double sal);

    List<Emp> findByDeptnoAndSal2(Map<String, Object> map);

    List<Emp> findByDeptnoAndSal3(Emp emp);

    List<Emp> findByDeptnoAndSal4(@Param("empa") Emp empa, @Param("empb") Emp empb);

    /**
     * 根据名字做模糊查询
     *
     * @param name 模糊查询的文字
     * @return Emp对象List集合
     */
    List<Emp> findByEname(String name);

    /**
     * 增加员工信息
     * @param emp 存储新增员工信息的Emp对象
     * @return 对数据库数据产生影响的行数
     */
    int addEmp(Emp emp);

    /**
     * 根据员工编号修改员工姓名的方法
     * @param empno 要修改的员工编号
     * @param ename 修改之后的新的员工名字
     * @return 对数据库数据产生影响的行数
     */
    int updateEnameByEmpno(@Param("empno") int empno, @Param("ename") String ename);

    /**
     * 根据员工编号删除员工信息
     * @param empno 要删除的员工编号
     * @return 对数据库数据产生影响的行数
     */
    int deleteByEmpno(int empno);
}