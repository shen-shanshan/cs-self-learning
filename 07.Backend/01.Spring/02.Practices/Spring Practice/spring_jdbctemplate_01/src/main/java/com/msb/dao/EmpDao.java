package com.msb.dao;

import com.msb.pojo.Emp;

import java.util.List;

public interface EmpDao {

    int findEmpCount();

    Emp findByEmpno(int empno);

    List<Emp> findByDeptnpo(int deptno);

    int addEmp(Emp emp);

}
