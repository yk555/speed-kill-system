package com.example.demo.service.impl;

import com.example.demo.model.AyUserKillProduct;
import com.example.demo.repository.AyUserKillProductRepository;
import com.example.demo.service.AyUserKillProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Optional;

@Service
public class AyUserKillProductServiceImpl implements AyUserKillProductService {

    @Resource
    private AyUserKillProductRepository ayUserKillProductRepository;

    // 保存用户秒杀记录
    @Override
    public AyUserKillProduct save(AyUserKillProduct killProduct) {
        return ayUserKillProductRepository.save(killProduct);
    }

    @Override
    public Collection<AyUserKillProduct> getRecode(Integer userId) {
        return ayUserKillProductRepository.findAllByUserId(userId);
    }

    @Override
    public AyUserKillProduct updateRecode(Integer recodeId) {
        AyUserKillProduct ayUserKillProduct = ayUserKillProductRepository.findById(recodeId).get();
        ayUserKillProduct.setState(1);

        return ayUserKillProductRepository.save(ayUserKillProduct);
    }
}
