package com.house.everything_house_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.house.everything_house_backend.entities.Blog;

import java.util.List;

public interface IBlogService extends IService<Blog> {
    public List<String> getBlogHomePicture();
}
