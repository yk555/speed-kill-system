package com.example.demo.repository;

import com.example.demo.model.AyUser;

public interface UserRepository {
    AyUser findByUserNameAndPassword(String username, String password);
}
