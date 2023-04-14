package com.house.everything_house_backend.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "sys_news")
public class News {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String title;
    private String content;
    @TableField(value = "create_time")
    private String createTime;
}
