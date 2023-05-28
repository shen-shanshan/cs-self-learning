package com.msb.service.impl;

import com.msb.dao.EmpDao;
import com.msb.pojo.Emp;
import com.msb.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @BelongsProject: Spring Practice
 * @BelongsPackage: com.msb.service.impl
 * @Author: SSS
 * @Description:
 */
@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpDao empDao;

    @Override
    public int findEmpCount() {
        return empDao.findEmpCount();
    }

    @Override
    public Emp findByEmpno(int empno) {
        return empDao.findByEmpno(empno);
    }

    @Override
    public List<Emp> findByDeptno(int deptno) {
        return empDao.findByDeptnpo(deptno);
    }

    @Override
    public int addEmp(Emp emp) {
        return empDao.addEmp(emp);
    }

}
