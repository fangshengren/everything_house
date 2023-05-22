package com.house.everything_house_backend.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.house.everything_house_backend.entities.Blog;
import com.house.everything_house_backend.mapper.BlogMapper;
import com.house.everything_house_backend.service.IBlogService;
import com.house.everything_house_backend.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private RedisUtil redisUtil;

    private String homePictureKey="blogHomePicture";

    @Override
    public List<String> getBlogHomePicture(){
        if (!redisUtil.hasKey(homePictureKey)){
            redisUtil.set(homePictureKey,blogMapper.getBlogHomePicture(),60*60*24);
        }
        return (List<String>) redisUtil.get(homePictureKey);
    }
//    @Override
//    public Page<Blog> getBlogs(int limit, int page) {
//        Page<Blog> blogPage = new Page<>(page, limit);
//        return blogMapper.selectPage(blogPage, null);
//    }

    @Override
    public Page<Blog> getBlogs(int limit, int page) {
        List<Blog> blogs = blogMapper.selectPageWithCoverImageUrl((page - 1) * limit, limit);

        Page<Blog> blogPage = new Page<>(page, limit);
        blogPage.setRecords(blogs);
        blogPage.setTotal(blogMapper.selectCount(null));  // Assuming this method exists

        return blogPage;
    }

    @Override
    public Blog getBlogById(int blogId) {
        return blogMapper.selectById(blogId);
    }
}
