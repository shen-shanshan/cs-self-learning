package com.msb.dao;

import com.msb.dao.EmpDao;

public class EmpDaoImpl2 implements EmpDao {
    @Override
    public int addEmp() {
        System.out.println("addEmp invoked 2 !");
        return 0;
    }
}
