package com.house.everything_house_backend.controller.dto;

import com.house.everything_house_backend.entities.Menu;
import lombok.Data;

import java.util.List;

//UserDTO用来接受前端登录时传递的用户名和密码
@Data
public class UserDTO {
    private int id;
    private String username;
    private String password;
    private String nickname;
    private String token;
    //把当前登录用户的角色以及他的菜单项带出来
    private String role;
    private String avatar;
    private List<Menu> menus;
}
