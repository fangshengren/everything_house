package com.house.everything_house_backend.controller;

import com.house.everything_house_backend.common.Result;
import com.house.everything_house_backend.config.AuthAccess;
import com.house.everything_house_backend.service.IBlogService;
import com.house.everything_house_backend.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private IBlogService blogService;
    @Autowired
    private IFileService fileService;

    public BlogController() {
    }

    @AuthAccess
    @GetMapping("/getBlogHomePicture")
    public Result getBlogHomePicture(){
        try{
            return Result.success(blogService.getBlogHomePicture());
        }catch (Exception e){
            return Result.error("500","获取博客首页图片失败"+e.getMessage());
        }
    }
}
