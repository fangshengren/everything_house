package com.house.everything_house_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.house.everything_house_backend.entities.News;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewsMapper extends BaseMapper<News> {
    @Delete("DELETE FROM sys_news WHERE id IN ( SELECT id FROM (SELECT id FROM sys_news ORDER BY id LIMIT 10) temp);")
    public boolean deleteTopTen();

    public boolean deleteAll();
}
