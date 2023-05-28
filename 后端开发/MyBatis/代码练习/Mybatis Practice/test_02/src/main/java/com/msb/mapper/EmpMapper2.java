package com.msb.mapper;

import com.msb.pojo.Emp;

import java.util.List;

// 动态SQL
public interface EmpMapper2 {
    List<Emp> findByCondition(Emp emp);
    List<Emp> findByCondition2(Emp emp);

    int updateEmpByCondition(Emp emp);
    int updateEmpByCondition2(Emp emp);

    List<Emp> findByEmpnos1(int[] empnos);
    List<Emp> findByEmpnos2(List<Integer> empnos);
}
