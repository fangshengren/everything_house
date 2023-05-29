package com.house.everything_house_backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.house.everything_house_backend.common.Result;
import com.house.everything_house_backend.config.AuthAccess;
import com.house.everything_house_backend.controller.dto.BlogDTO;
import com.house.everything_house_backend.entities.Blog;
import com.house.everything_house_backend.service.IBlogService;
import com.house.everything_house_backend.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private IBlogService blogService;
    @Autowired
    private IFileService fileService;

    public BlogController() {
    }

    @AuthAccess
    @GetMapping("/getBlogHomePicture")
    public Result getBlogHomePicture(){
        try{
            return Result.success(blogService.getBlogHomePicture());
        }catch (Exception e){
            return Result.error("500","获取博客首页图片失败"+e.getMessage());
        }
    }
    @AuthAccess
    @GetMapping("/getBlogs")
    public Result getBlogs(@RequestParam(name = "limit", defaultValue = "5") int limit,
                             @RequestParam(name = "page", defaultValue = "1") int page) {
        Page<Blog> blogPage = blogService.getBlogs(limit, page);
        return Result.success(blogPage);
    }

    @AuthAccess
    @GetMapping("/detail/{blogId}")
    public Result getBlogDetail(@PathVariable int blogId) {
        Blog blog = blogService.getBlogById(blogId);
        return Result.success(blog);
    }

    @PostMapping("/saveBlog")
    public Result saveBlog(@RequestBody BlogDTO blogDTO){
        //前端传递过来标题、内容、作者ID、图片ID
        Blog blog = new Blog();
        blog.setBlogTitle(blogDTO.getBlogTitle());
        blog.setBlogContent(blogDTO.getBlogContent());
        blog.setUserId(blogDTO.getAuthorID());
        //System.out.println("imgID"+blogDTO.getImgID());
        blog.setCoverImageId(blogDTO.getImgID());
        blog.setBlogStatus(1);
        boolean isSaved = blogService.save(blog);
        if (isSaved) {
            // 获取新创建的博客的 ID，并将其返回
            return Result.success(blog.getBlogId());
        } else {
            return Result.error("500", "保存博客失败");
        }
    }

}
