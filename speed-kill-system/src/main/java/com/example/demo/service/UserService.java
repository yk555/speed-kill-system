package com.example.demo.service;

import com.example.demo.model.AyUser;

public interface UserService {

    AyUser finduser(String name, String password);
    AyUser findMy(Integer id);
}
