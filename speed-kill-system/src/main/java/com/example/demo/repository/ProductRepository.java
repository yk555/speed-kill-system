package com.example.demo.repository;

import com.example.demo.model.AyProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<AyProduct, Integer> {
}
