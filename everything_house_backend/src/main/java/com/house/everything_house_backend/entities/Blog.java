package com.house.everything_house_backend.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName(value="t_blog")
@AllArgsConstructor
@NoArgsConstructor
public class Blog extends Model<Blog> {
    @TableField(value="blog_id")
    private Integer blogId;
    @TableField(value="blog_title")
    private String blogTitle;
    @TableField(value="blog_content")
    private String blogContent;
    @TableField(value="userid")
    private Integer userId;
    @TableField(value="type_id")
    private Integer typeId;
    @TableField(value="blog_status")
    private Integer blogStatus;
    @TableField(value="create_time")
    private String createTime;
    @TableField(value="cover_image_id")
    private Integer coverImageId;
}
