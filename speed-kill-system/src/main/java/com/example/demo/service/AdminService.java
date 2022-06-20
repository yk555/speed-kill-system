package com.example.demo.service;

import com.example.demo.model.AyProduct;

import java.util.Collection;

public interface AdminService {
    Collection<AyProduct> findAllCache();
}
