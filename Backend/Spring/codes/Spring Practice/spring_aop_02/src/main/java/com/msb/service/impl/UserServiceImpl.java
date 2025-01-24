package com.msb.service.impl;

import com.msb.dao.UserDao;
import com.msb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public int addUser(int userid, String username) {
        System.out.println("userService add ...");
        int rows = userDao.addUser(userid, username);
        return rows;
    }

}
