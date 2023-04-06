package com.house.everything_house_backend.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.NoArgsConstructor;

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
}
