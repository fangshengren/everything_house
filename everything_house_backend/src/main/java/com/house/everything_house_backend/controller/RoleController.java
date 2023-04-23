package com.house.everything_house_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.house.everything_house_backend.common.Result;
import com.house.everything_house_backend.entities.Role;
import com.house.everything_house_backend.service.impl.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleServiceImpl roleServiceImpl;
    //增加角色
    @PostMapping
    public Result save(@RequestBody Role role){
        roleServiceImpl.saveOrUpdate(role);
        return Result.success();
    }
    //根据id删除角色
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id){
        roleServiceImpl.removeById(id);
        return Result.success();
    }
    //批量删除角色
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        roleServiceImpl.removeByIds(ids);
        return Result.success();
    }
    //查询全部角色
    @GetMapping
    public Result findAll(){
        return Result.success(roleServiceImpl.list());
    }
    //根据id查找角色
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id){
        return Result.success(roleServiceImpl.getById(id));
    }
    //分页查找
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name){
        QueryWrapper<Role> queryWrapper=new QueryWrapper<>();
        queryWrapper.like("name",name);
        queryWrapper.orderByDesc("id");
        return Result.success(roleServiceImpl.page(new Page<>(pageNum,pageSize),queryWrapper));
    }
    /**
     * 设置初始绑定结果
     *
     * @param roleId 角色Id
     * @return
     */
    @GetMapping("/roleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId){
        return Result.success(roleServiceImpl.getRoleMenu(roleId));
    }
    /**
     * 绑定角色和菜单的关系
     * @param roleId 角色Id
     * @param menuIds 菜单列表
     * @return
     */
    @PostMapping("/roleMenu/{roleId}")
    public Result roleMenu(@PathVariable Integer roleId,@RequestBody List<Integer> menuIds){
        roleServiceImpl.setRoleMenu(roleId,menuIds);
        return Result.success();
    }

}
