package com.example.demo.service.impl;

import com.example.demo.model.AyProduct;
import com.example.demo.model.AyUserKillProduct;
import com.example.demo.model.KillStatus;
import com.example.demo.producer.AyProductKillProducer;
import com.example.demo.repository.AyUserKillProductRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.jms.Destination;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
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
    public List<AyProduct> findAll() {
        try{
            List<AyProduct> ayProducts = productRepository.findAll();
            logger.info(String.valueOf(ayProducts.get(1).getStartTime()));
            return ayProducts;

        }catch (Exception e) {
            logger.error("ProductServiceImpl.findAll error", e);
            return Collections.EMPTY_LIST;//有问题
        }
    }

    @Override
    public Collection<AyProduct> findAllCache() {
        try{
            //从缓存中查找
            redisTemplate = redisStringTemplate(redisTemplate);

            Map<Integer, AyProduct> productMap = redisTemplate.opsForHash().entries(KILL_PRODUCT_LIST);

            Collection<AyProduct> ayProducts = null;
            //缓存中没有
            if (CollectionUtils.isEmpty(productMap)) {
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
            }
            ayProducts = productMap.values();
            logger.info(String.valueOf(productMap.get(1).getStartTime()));
            return ayProducts;
        }catch (Exception e) {
            logger.error("ProductServiceImpl.findAllCache error", e);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public AyProduct killProduct(Integer productId, Integer userId) throws ParseException {
        //AyProduct ayProduct = productRepository.findById(productId).get();
        AyUserKillProduct ayUserKillProduct = ayUserKillProductRepository.findAllByUserIdAndProductId(userId, productId);
        if (ayUserKillProduct != null) return null;
        AyProduct ayProduct = (AyProduct) redisTemplate.opsForHash().get(KILL_PRODUCT_LIST, productId);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date data = new Date();
        String dateString = formatter.format(data);
        String format = "yyyy-MM-dd HH:mm:ss";
        Date nowTime = new SimpleDateFormat(format).parse(dateString);
        if (!isEffectiveDate(nowTime, ayProduct.getStartTime(), ayProduct.getEndTime())) return null;

        logger.info( String.valueOf(ayProduct.getNumber()));
        //没库存
        if (ayProduct.getNumber() == 0) {
            return null;
        }

        // 设置库存数 - 1
        ayProduct.setNumber(ayProduct.getNumber() - 1);
        // 更新商品库存
        ayProduct = productRepository.save(ayProduct);

        // 保存商品的秒杀记录
        AyUserKillProduct killProduct = new AyUserKillProduct();
        killProduct.setCreateTime(new Date());
        killProduct.setProductId(productId);
        killProduct.setUserId(userId);
        killProduct.setImg(ayProduct.getProductImg());
        killProduct.setName(ayProduct.getName());
        killProduct.setPrice(ayProduct.getPrice());
        // 设置秒杀状态
        killProduct.setState(KillStatus.SUCCESS.getCode());
        ayUserKillProductRepository.save(killProduct);
        //异步保存
        ayProductKillProducer.sendMessage(destination, killProduct);
        redisTemplate.opsForHash().put(KILL_PRODUCT_LIST, killProduct.getProductId(), ayProduct);
        return ayProduct;

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
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }





}
