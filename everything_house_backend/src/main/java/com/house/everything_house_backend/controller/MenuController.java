package com.house.everything_house_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.house.everything_house_backend.common.Result;
import com.house.everything_house_backend.entities.Menu;
import com.house.everything_house_backend.mapper.DictMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.house.everything_house_backend.service.impl.MenuServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuServiceImpl menuServiceImpl;
    @Resource
    private DictMapper dictMapper;

    //增加菜单
    @PostMapping
    public Result save(@RequestBody Menu menu){
        menuServiceImpl.saveOrUpdate(menu);
        return Result.success();
    }

    //根据id删除菜单
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id){
        menuServiceImpl.removeById(id);
        return Result.success();
    }

    //批量删除菜单
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        menuServiceImpl.removeByIds(ids);
        return Result.success();
    }

    //根据id查找菜单
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id){
        return Result.success(menuServiceImpl.getById(id));
    }

    //查询全部菜单
    @GetMapping
    public Result findAll( @RequestParam(defaultValue = "") String name){
        return Result.success(menuServiceImpl.findMenus(name));
    }

    //查询所有菜单id
    @GetMapping("/ids")
    public Result findAllIds(){
        return Result.success(menuServiceImpl.list().stream().map(Menu::getId));
    }

    //分页查找
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name){
        QueryWrapper<Menu> queryWrapper=new QueryWrapper<>();
        queryWrapper.like("name",name);
        queryWrapper.orderByDesc("id");
        return Result.success(menuServiceImpl.page(new Page<>(pageNum,pageSize),queryWrapper));
    }

    //查找图标
    @GetMapping("/icons")
    public Result getIcons(){
        QueryWrapper<Menu> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("type", "icon");
        return Result.success(dictMapper.selectList(null));
    }



}
