package com.example.demo.producer;

import com.example.demo.model.AyUserKillProduct;
import com.example.demo.service.impl.ProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;

//生产者
@Service
public class AyProductKillProducer {
    Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;
    //send message

    public void sendMessage(Destination destination, final AyUserKillProduct killProduct) {
        logger.info("AyProductKillProducer sendMessage , killProduct is" + killProduct);
        jmsMessagingTemplate.convertAndSend(destination, killProduct);
    }

}
