package com.example.demo.service.impl;

import com.example.demo.Dao.UserDao;
import com.example.demo.model.AyUser;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    //用户登陆的实现
    @Override
    public AyUser finduser(String username, String password) {

        return userDao.findByUsernameAndPassword(username,password);
    }
    @Override
    public AyUser findMy(Integer id) {
        return userDao.getById(id);
    }
}

