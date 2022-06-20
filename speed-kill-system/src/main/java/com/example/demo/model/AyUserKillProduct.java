package com.example.demo.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/*
* 用户秒杀商品记录
*
* */
@Entity
@Table(name = "ay_user_kill_product")
public class AyUserKillProduct implements Serializable {
    @Id
    /*
    * 主键的生成策略
    * IDENTITY 自增主键、Oracle不支持
    * AUTO JPA自动选择 为默认方式
    * SEQUENCE 通过序列产生主键 MYSQL不支持
    * TABLW 通过表模拟序列产生主键*/
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 商品id
    private Integer productId;
    // 用户id
    private Integer userId;

    // 状态 -1 无效 ； 0 成功； 1 已付款
    private Integer state;

    // 创建时间
    private Date createTime;

    private String img;
    private String name;
    private Integer price;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
