package com.msb.service.impl;

import com.msb.dao.AccountDao;
import com.msb.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @BelongsProject: Spring Practice
 * @BelongsPackage: com.msb.service.impl
 * @Author: SSS
 * @Description:
 */
@Service
@Transactional // 自动实现事务控制
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Override
    public int transMoney(int from, int to, int money) {
        int rows = 0;
        // 转出
        rows += accountDao.transMoney(from, 0 - money);
        // 模拟异常
        int i = 1 / 0;
        // 转入
        rows += accountDao.transMoney(to, money);
        return rows;
    }

}
