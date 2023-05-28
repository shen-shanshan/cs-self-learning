package com.msb.dao.impl;

import com.msb.dao.UserDao;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImplB implements UserDao {
    @Override
    public void add() {
        System.out.println("UserDaoImplB add ...");
    }
}
