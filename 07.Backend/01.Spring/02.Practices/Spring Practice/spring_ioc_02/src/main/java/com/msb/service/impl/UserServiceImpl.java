package com.msb.service.impl;

import com.msb.dao.UserDao;
import com.msb.dao.impl.UserDaoImplB;
import com.msb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    @Qualifier(value = "userDaoImplB")
    private UserDao userDao;

    @Override
    public void add() {
        System.out.println("UserServiceImpl add ...");
        userDao.add();
    }
}
