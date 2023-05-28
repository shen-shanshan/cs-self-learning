package com.msb.dao.impl;

import com.msb.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @BelongsProject: Spring Practice
 * @BelongsPackage: com.msb.dao.impl
 * @Author: SSS
 * @Description:
 */
@Repository
public class AccountDaoImpl implements AccountDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int transMoney(int id, int money) {
        String sql = "update account set money = money + ? where id = ?";
        return jdbcTemplate.update(sql, money, id);
    }

}
