package com.house.everything_house_backend.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName(value="sys_user")
@AllArgsConstructor
@NoArgsConstructor
public class User extends Model<User> {
    @TableId(value = "id",type = IdType.AUTO)
    @ExcelProperty("id")
    private Integer id;

    @ExcelProperty("用户名")
    private String username;

    @ExcelProperty("密码")
    private String password;

    @ExcelProperty("昵称")
    private String nickname;

    @ExcelProperty("邮箱")
    private String email;

    @ExcelProperty("电话")
    private String phone;

    @ExcelProperty("地址")
    private String address;

    @TableField(value="create_time")//这样处理的主要目的是java对带有下划线的字段不识别，所以改为驼峰形式
    @ExcelProperty("创建时间")
    private Date createdTime;//如果需要年月日格式的可以使用Date类型，如果需要具体到时分秒就使用String类型
    @ExcelProperty("化身")
    private String avatar;
    @ExcelProperty("角色")
    private String role;
    @ExcelProperty("盐")
    private String salt;
}
