package com.example.demo.service.impl;

import com.example.demo.model.AyProduct;
import com.example.demo.producer.AyProductKillProducer;
import com.example.demo.repository.AyUserKillProductRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.AdminService;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.jms.Destination;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    public RedisTemplate<Object, Object> redisStringTemplate(RedisTemplate<Object, Object> redisTemplate) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // 如果手动将Value转换成了JSON，就不要再用JSON序列化器了。
        // redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        redisTemplate.setValueSerializer(stringRedisSerializer);
        return redisTemplate;
    }

    @Resource
    private ProductRepository productRepository;

    @Resource
    private AyUserKillProductRepository ayUserKillProductRepository;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private AyProductKillProducer ayProductKillProducer;

    private static Destination destination = new ActiveMQQueue("ay.queue.asyn.save");

    //定义缓存key
    private static final String KILL_PRODUCT_LIST = "kill_product_list";
    // 有问题
    Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Override
    public Collection<AyProduct> findAllCache() {
        try{
            //从缓存中查找
            redisTemplate = redisStringTemplate(redisTemplate);

            Map<Integer, AyProduct> productMap = redisTemplate.opsForHash().entries(KILL_PRODUCT_LIST);

            Collection<AyProduct> ayProducts = null;
            //缓存中没有

                //从数据库中查询商品
                ayProducts = productRepository.findAll();
                //将商品list转换为map
                productMap = converToMap(ayProducts);
                logger.info(String.valueOf(productMap.get(1).getStartTime()));
                //保存到缓存中
                redisTemplate.opsForHash().putAll(KILL_PRODUCT_LIST, productMap);
                //设置缓存过期时间，如果商品数据变化少就长一点，反之就短一点
                //redisTemplate.expire(KILL_PRODUCT_LIST, 1000000, TimeUnit.MILLISECONDS);
                //设置永不过期
                redisTemplate.persist(KILL_PRODUCT_LIST);
                return ayProducts;


        }catch (Exception e) {
            logger.error("ProductServiceImpl.findAllCache error", e);
            return Collections.EMPTY_LIST;
        }
    }
    // list转化为map
    private Map<Integer, AyProduct> converToMap(Collection<AyProduct> ayProducts) {
        if (CollectionUtils.isEmpty(ayProducts)) {
            return Collections.EMPTY_MAP;
        }
        Map<Integer, AyProduct> productMap = new HashMap<>(ayProducts.size());
        for (AyProduct product : ayProducts) {
            productMap.put(product.getId(), product);
        }
        return productMap;
    }
}
