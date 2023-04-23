package com.house.everything_house_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.house.everything_house_backend.entities.Menu;

import java.util.List;

public interface IMenuService extends IService<Menu> {
    public List<Menu> findMenus(String name);
}
