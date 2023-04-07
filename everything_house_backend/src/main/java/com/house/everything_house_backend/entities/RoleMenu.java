package com.house.everything_house_backend.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
//可以使用 @TableName 表名注解指定当前实体类对应的表名，比如下面 RoleMenu 实体类对应表名为 sys_role_menu
@TableName(value="sys_role_menu")
public class RoleMenu {
    private Integer roleId;
    private Integer menuId;
}
