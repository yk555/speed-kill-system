package com.example.demo.service;

import com.example.demo.model.AyUserKillProduct;

import java.util.Collection;

// 用户秒杀商品记录接口
public interface AyUserKillProductService {

    AyUserKillProduct save(AyUserKillProduct killProduct);
    Collection<AyUserKillProduct> getRecode(Integer userId);
    AyUserKillProduct updateRecode(Integer recodeId);
}
