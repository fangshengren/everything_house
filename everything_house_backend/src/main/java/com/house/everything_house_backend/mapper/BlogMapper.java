package com.house.everything_house_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.house.everything_house_backend.entities.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {
    List<String> getBlogHomePicture();

    List<Blog> selectPageWithCoverImageUrl(@Param("page") int page, @Param("limit") int limit);
}
