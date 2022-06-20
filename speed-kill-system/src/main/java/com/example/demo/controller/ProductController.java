package com.example.demo.controller;

import com.example.demo.model.AyProduct;
import com.example.demo.model.AyUser;
import com.example.demo.model.AyUserKillProduct;
import com.example.demo.service.AyUserKillProductService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Resource
    private ProductService productService;
    @Resource
    private AyUserKillProductService ayUserKillProductService;
    @Resource
    private UserService userService;
    @Autowired
    HttpServletRequest request;
    //出现首页

    // 查询所有商品
    @RequestMapping("/all")
    public String findAll(Model model) {
        Integer id = (Integer) request.getSession().getAttribute("id");
        AyUser ayUser = userService.findMy(id);
        if (ayUser.getAge() < 18 || ayUser.getJob() != 1 || ayUser.getTrust() != 1 || ayUser.getNopay() >= 2) {
            return "false";
        }
        List<AyProduct> products = productService.findAll();
        model.addAttribute("products", products);
        return "product_list";
    }

    // 有问题
    //秒杀商品
    @RequestMapping("/{id}/kill")
    public String killProduct(Model model, @PathVariable("id") Integer productId, @RequestParam("userId") Integer userId) throws ParseException {
        AyProduct ayProduct = productService.killProduct(productId, userId);
        if (null != ayProduct) {
            return "success";
        }
        return "fail";
    }

    @RequestMapping("/all/cache")
    public String findAllCache(Model model) {

        Collection<AyProduct> products = productService.findAllCache();
        model.addAttribute("products", products);
        return "product_list";
    }
    //用户订单接口
    @RequestMapping("/recode")
    public String findRecode(Model model) {
        Integer userId = (Integer) request.getSession().getAttribute("id");
        Collection<AyUserKillProduct> recodes = ayUserKillProductService.getRecode(userId);
        for (AyUserKillProduct ayUserKillProduct : recodes) {
            System.out.println(ayUserKillProduct.getProductId());
        }

        model.addAttribute("recodes", recodes);
        return "kill_recode_list";
    }
    @RequestMapping("/pay")
    public String payRecode(@RequestParam("recodeId") Integer recodeId,Model model) {
        ayUserKillProductService.updateRecode(recodeId);
        return "redirect:/products/recode";
    }
}
