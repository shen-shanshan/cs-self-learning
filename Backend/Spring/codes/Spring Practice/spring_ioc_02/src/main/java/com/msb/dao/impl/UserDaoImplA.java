package com.msb.dao.impl;

import com.msb.dao.UserDao;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImplA implements UserDao {
    @Override
    public void add() {
        System.out.println("UserDaoImplA add ...");
    }
}
