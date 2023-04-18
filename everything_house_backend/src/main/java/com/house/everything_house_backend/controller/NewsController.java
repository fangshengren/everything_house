package com.house.everything_house_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.house.everything_house_backend.common.Result;
import com.house.everything_house_backend.entities.News;
import com.house.everything_house_backend.service.INewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        queryWrapper.orderByAsc("id");
        return Result.success(newsService.page(new Page<>(pageNum,pageSize),queryWrapper));
    }

    @GetMapping("")
    public List<News> getNews() {
        try {
            return newsService.getNews();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    @DeleteMapping("/del/batch")
    public Result deleteByIds(@RequestBody List<Integer> ids){
        newsService.removeByIds(ids);
        return Result.success();
    }

    @DeleteMapping("/delTopTen")
    public Result deleteTopTen(){
        newsService.deleteTopTen();
        return Result.success();
    }

    @DeleteMapping("/delAll")
    public Result deleteAll(){
        newsService.deleteAll();
        return Result.success();
    }

    @PostMapping
    public Result save(@RequestBody List<News> newsList) {
        System.out.println("Received newsList size: " + newsList.size());
        newsService.saveBatch(newsList);
        return Result.success();
    }

}
