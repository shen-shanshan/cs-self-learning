package com.msb.dao.impl;

import com.msb.dao.EmpDao;
import org.springframework.stereotype.Repository;

@Repository
public class EmpDaoImpl implements EmpDao {

    @Override
    public int addEmp(int empno, String ename, String job) {
        System.out.println("empDao add ...");
        return 1;
    }

}
