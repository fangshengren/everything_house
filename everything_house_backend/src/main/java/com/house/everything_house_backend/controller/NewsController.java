package com.house.everything_house_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.house.everything_house_backend.common.Result;
import com.house.everything_house_backend.entities.News;
import com.house.everything_house_backend.entities.Role;
import com.house.everything_house_backend.service.INewsService;
import com.house.everything_house_backend.service.iml.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news")
public class NewsController {
    /**
     * 1.could read all records of news
     * 2.could find some specific news by searching with some condition relative with title
     * 1~2 may be finished at the same time
     * 3.
     *
     */
    @Autowired
    private INewsService newsService;

    @GetMapping("/page")
    //分页查询的接口，其中可以通过like title实现通过title的分页查询
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String title){
        QueryWrapper<News> queryWrapper=new QueryWrapper<>();
        queryWrapper.like("title",title);
        queryWrapper.orderByDesc("id");
        return Result.success(newsService.page(new Page<>(pageNum,pageSize),queryWrapper));
    }

}
