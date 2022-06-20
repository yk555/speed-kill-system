package com.example.demo.service;

import com.example.demo.model.AyProduct;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;

// 商品接口
public interface ProductService {

    //查询所有商品
    List<AyProduct> findAll();
    // 在缓存查询商品
    Collection<AyProduct> findAllCache();

    //秒杀商品
    AyProduct killProduct(Integer productId, Integer userId) throws ParseException;
}
