package com.house.everything_house_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.house.everything_house_backend.entities.News;

import java.io.IOException;
import java.util.List;

public interface INewsService extends IService<News> {


    List<News> getNews() throws IOException;
    public boolean deleteTopTen();
    public boolean deleteAll();
}
