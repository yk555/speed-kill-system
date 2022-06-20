package com.example.demo.repository;

import com.example.demo.model.AyUserKillProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface AyUserKillProductRepository extends JpaRepository<AyUserKillProduct, Integer> {
    Collection<AyUserKillProduct> findAllByUserId(Integer userId);
    AyUserKillProduct findAllByUserIdAndProductId(Integer userId, Integer productId);

}
