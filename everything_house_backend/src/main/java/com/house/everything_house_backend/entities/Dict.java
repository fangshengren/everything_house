package com.house.everything_house_backend.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
//可以使用 @TableName 表名注解指定当前实体类对应的表名，比如下面 Dict 实体类对应表名为 sys_dict
@TableName(value="sys_dict")
public class Dict {
    private String name;
    private String value;
    private String type;
}
