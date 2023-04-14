package com.house.everything_house_backend.service.iml;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.house.everything_house_backend.entities.News;
import com.house.everything_house_backend.mapper.NewsMapper;
import com.house.everything_house_backend.service.INewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsService extends ServiceImpl<NewsMapper, News> implements INewsService {
    @Autowired
    private NewsMapper newsMapper;


}
