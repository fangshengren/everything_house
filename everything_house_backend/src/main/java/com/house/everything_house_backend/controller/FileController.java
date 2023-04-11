package com.house.everything_house_backend.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.house.everything_house_backend.common.Result;
import com.house.everything_house_backend.entities.Files;
import com.house.everything_house_backend.mapper.FileMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 文件上传接口
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Value("${files.upload.path}")
    private String fileUploadPath;
    @Value("${server.port}")
    private String port;
    @Resource
    private FileMapper fileMapper;

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String type = FileUtil.extName(originalFileName);
        Long size = file.getSize();

        File uploadParentFile = new File(fileUploadPath);
        if (!uploadParentFile.exists()) {
            uploadParentFile.mkdirs();
        }

        //定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type;
        File uploadFile = new File(fileUploadPath + fileUUID);

        // 临时存储上传的文件以便计算 MD5
        file.transferTo(uploadFile);
        String md5 = SecureUtil.md5(uploadFile);

        // 获取文件 url
        String url;
        // 查询文件 MD5 和文件名是否存在
        Files files = getFileByMd5AndName(md5, originalFileName);

        if (files != null) {
            // 文件已存在，不需要上传和插入数据库
            uploadFile.delete();
            return files.getUrl();
        }

        // 如果 MD5 存在但文件名不同，则插入数据库
        Files md5Files = getFileByMd5(md5);
        if (md5Files != null) {
            url = md5Files.getUrl();
            saveFileInfo(originalFileName, type, size, url, md5);
            uploadFile.delete();
            return url;
        }

        // 如果 MD5 不存在，则上传文件并插入数据库
        url = "http://localhost:" + port + "/file/" + fileUUID;
        saveFileInfo(originalFileName, type, size, url, md5);
        return url;

    }

    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        //根据uuid获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        //设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");
        //读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    private Files getFileByMd5(String md5) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Files> filesList = fileMapper.selectList(queryWrapper);
        return filesList.isEmpty() ? null : filesList.get(0);
    }

    private Files getFileByMd5AndName(String md5, String name) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5).eq("name", name);
        return fileMapper.selectOne(queryWrapper);
    }

    private void saveFileInfo(String name, String type, Long size, String url, String md5) {
        Files saveFile = new Files();
        saveFile.setName(name);
        saveFile.setType(type);
        saveFile.setSize(size / 1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5); // 设置文件的 MD5 属性
        fileMapper.insert(saveFile);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Files files) {
        return Result.success(fileMapper.updateById(files));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        Files files = fileMapper.selectById(id);
        files.setIsDelete(true);
        fileMapper.updateById(files);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        // select * from sys_file where id in (id,id,id...)
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<Files> files = fileMapper.selectList(queryWrapper);
        for (Files file : files) {
            file.setIsDelete(true);
            fileMapper.updateById(file);
        }
        return Result.success();
    }

    /**
     * 分页查询接口
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {

        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        // 查询未删除的记录
        queryWrapper.eq("is_delete", false);
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        return Result.success(fileMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper));
    }
}
