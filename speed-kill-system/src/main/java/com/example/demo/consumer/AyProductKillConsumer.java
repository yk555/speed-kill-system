package com.example.demo.consumer;

import com.example.demo.model.AyUserKillProduct;
import com.example.demo.service.AyUserKillProductService;
import com.example.demo.service.impl.ProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AyProductKillConsumer {
    Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Resource
    private AyUserKillProductService ayUserKillProductService;

    //消费信息
    @JmsListener(destination = "ay.queue.asyn.save")
    public void receiveQueue(AyUserKillProduct killProduct) {
        //保存
        ayUserKillProductService.save(killProduct);
        // 记录日志
        logger.info("ayUserKillProductService save, and killProduct: " + killProduct);
    }
}
