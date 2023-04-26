package com.house.everything_house_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.house.everything_house_backend.entities.Files;
import com.house.everything_house_backend.mapper.FileMapper;
import com.house.everything_house_backend.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Primary
public class FileServiceImpl extends ServiceImpl<FileMapper, Files> implements IFileService {
    @Autowired
    FileMapper fileMapper;
    @Override
    public Files getFileByMd5(String md5) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Files> filesList = fileMapper.selectList(queryWrapper);
        return filesList.isEmpty() ? null : filesList.get(0);
    }
    @Override
    public Files getFileByMd5AndName(String md5, String name) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5).eq("name", name);
        return fileMapper.selectOne(queryWrapper);
    }
    @Override
    public void saveFileInfo(String name, String type, Long size, String url, String md5) {
        Files saveFile = new Files();
        saveFile.setName(name);
        saveFile.setType(type);
        saveFile.setSize(size / 1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5); // 设置文件的 MD5 属性
        fileMapper.insert(saveFile);
    }
}
