package com.house.everything_house_backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    /**
     * 这个Controller没有p用，仅仅项目初用来测试
     * @return
     */
    @RequestMapping("/test")
    public String login(){
        return "main.html";
    }

    @GetMapping("/")
    /*Map方式
    public String index(Map<String, Object> map){
        map.put("msg", "Hello, Spring Boot!");
        return "login.html";
    }*/
    /*Model 方式*/
    public String index(Model model){
        model.addAttribute("msg", "Hello 同学");
        return "login.html";
    }

    @PostMapping("/login")
    public String login(String username, String password) {
        System.out.println("用户名是:"+username+"密码是:"+password);
        return "main.html";
    }



}
