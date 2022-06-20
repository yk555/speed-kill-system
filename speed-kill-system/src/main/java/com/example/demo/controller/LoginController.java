package com.example.demo.controller;

import com.example.demo.Dao.UserDao;
import com.example.demo.model.AyUser;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.ProductServiceImpl;
import com.example.demo.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@Controller
public class LoginController {
    @Autowired
    HttpServletRequest request;
    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    UserDao userDao;
    //用户的登陆
    @RequestMapping("/")
    public String login()
    {
        return "login";
    }
    @RequestMapping("/dologin")
    public String dologin(@RequestParam("username") String username,@RequestParam("password") String password, HttpSession session, String verifycode, Model model)
    {
        logger.info(username + password);
        AyUser user1=userDao.findByUsernameAndPassword(username, password);

        //String code= (String) session.getAttribute("verifyCode");
        //&&code.equalsIgnoreCase(verifycode)
        if(user1!=null)
        {
            session.setAttribute("id", user1.getId());
            model.addAttribute("message","成功");
            return "enter";
        }
        else
        {
            model.addAttribute("message","失败");
            return "enter";
        }
    }
    @RequestMapping("/index.html")
    public String loginTest()
    {


        System.out.print(request.getSession().getAttribute("id"));

        //return "index";
        return "redirect:/products/all";
    }


}

