package com.example.demo.controller;


import com.example.demo.model.Admin;
import com.example.demo.model.AyProduct;
import com.example.demo.model.AyUser;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.AdminService;
import com.example.demo.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

@Controller
@RequestMapping("/admin")
public class adminController {
    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Resource
    AdminRepository adminRepository;
    @Resource
    AdminService adminService;
    @Resource
    ProductRepository productRepository;
    // 管理员的登陆
    @RequestMapping("/login")
    public String adminLogin() {
        return "adminLogin";
    }

    @RequestMapping("/dologin")
    public String dologin(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session, Model model)
    {
        logger.info(username + password);
        Admin user1=adminRepository.findByUsernameAndPassword(username, password);

        //String code= (String) session.getAttribute("verifyCode");
        //&&code.equalsIgnoreCase(verifycode)
        if(user1!=null)
        {
            session.setAttribute("id", user1.getId());
            model.addAttribute("message","成功");
            return "redirect:/admin/products/all";
        }
        else
        {
            model.addAttribute("message","失败");
            return "enter";
        }
    }

    @RequestMapping("/products/all")
    public String findAllCache(Model model) {

        Collection<AyProduct> products = adminService.findAllCache();
        model.addAttribute("products", products);
        return "adminList";
    }

    @RequestMapping("/control")
    public String Control(Model model) {
        return "adminControl";
    }

    // 上传商品的地方
    @PostMapping("/upload")
    public String UploadPicture(@RequestParam("file") MultipartFile file, @RequestParam("startTime") String startTime0,@RequestParam("endTime") String endTime0,
                                @RequestParam("name") String name,
                                @RequestParam("number") Integer number,
                                RedirectAttributes attributes) throws IOException, ParseException {

        // 上传文件/图像到指定文件夹（这里可以改成你想存放地址的相对路径）
        File savePos = new File("src/main/resources/static");
        if(!savePos.exists()){  // 不存在，则创建该文件夹
            savePos.mkdir();
        }
        // 获取存放位置的规范路径
        String realPath = savePos.getCanonicalPath();
        // 上传该文件/图像至该文件夹下
        file.transferTo(new File(realPath+"/"+file.getOriginalFilename()));
        attributes.addFlashAttribute("message","添加成功！");

        startTime0 = startTime0 + ":00";
        endTime0 = endTime0 + ":00";
        Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:00").parse(startTime0.replace('T',' '));
        Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:00").parse(endTime0.replace('T',' '));
        System.out.println(startTime);
        System.out.println(endTime);

        AyProduct ayProduct = new AyProduct();
        ayProduct.setNumber(number);
        ayProduct.setName(name);
        ayProduct.setStartTime(startTime);
        ayProduct.setEndTime(endTime);
        ayProduct.setProductImg("/" + file.getOriginalFilename());
        Collection<AyProduct> ayProducts = productRepository.findAll();
        Date nowDate = new Date();
        ayProduct.setCreateTime(nowDate);
        ayProduct.setId(ayProducts.size()+1);
        productRepository.save(ayProduct);

        return "redirect:/admin/products/all";
    }
}
