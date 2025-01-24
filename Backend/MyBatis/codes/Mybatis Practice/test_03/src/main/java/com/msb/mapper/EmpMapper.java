package com.msb.mapper;

import com.msb.pojo.Emp;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EmpMapper {
    /**
     * 根据员工编号查询员工的所有信息并携带所在的部门信息
     * @param empno 要查询的员工编号
     * @return Emp对象,组合了Dept对象作为属性,对部门信息进行存储
     */
    Emp findEmpJoinDeptByEmpno(int empno);

    List<Emp> findEmpsByDeptno(int deptno);
}