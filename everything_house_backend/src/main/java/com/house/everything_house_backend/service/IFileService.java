package com.house.everything_house_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.house.everything_house_backend.entities.Files;

public interface IFileService extends IService<Files> {
    public Files getFileByMd5(String md5);
    public Files getFileByMd5AndName(String md5, String name);
    public void saveFileInfo(String name, String type, Long size, String url, String md5);

}
