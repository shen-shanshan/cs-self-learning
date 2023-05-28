package com.msb.mapper;

import com.msb.pojo.Dept;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface DeptMapper {
    /**
     * 根据部门编号查询部门信息及该部分的所有员工信息
     *
     * @param deptno 要查询的部门编号
     * @return Dept对象, 内部组合了一个Emp的List属性用于封装部门的所有员工信息
     */
    Dept findDeptJoinEmpsByDeptno(int deptno);

    Dept findDeptByDeptno(int deptno);

    @Select("select * from dept where deptno =#{deptno}")
    Dept findByDeptno(int deptno);

    @Update("update dept set dname =#{dname}, loc =#{loc} where deptno =#{deptno}")
    int updateDept(Dept dept);

    @Insert("insert into dept values(DEFAULT,#{dname},#{loc})")
    int addDept(Dept dept);

    @Delete("delete from dept where deptno =#{deptno}")
    int removeDept(int deptno);
}
