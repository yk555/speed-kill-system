package com.example.demo.Dao;

import com.example.demo.model.AyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<AyUser,Integer> {

    AyUser findByUsernameAndPassword(String username,String password);
}
