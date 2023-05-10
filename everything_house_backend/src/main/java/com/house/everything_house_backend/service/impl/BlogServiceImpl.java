package com.house.everything_house_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.house.everything_house_backend.entities.Blog;
import com.house.everything_house_backend.mapper.BlogMapper;
import com.house.everything_house_backend.service.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Override
    public List<String> getBlogHomePicture(){
        return blogMapper.getBlogHomePicture();
    }
}
