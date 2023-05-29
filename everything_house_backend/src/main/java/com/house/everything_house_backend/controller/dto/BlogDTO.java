package com.house.everything_house_backend.controller.dto;

import lombok.Data;

@Data
public class BlogDTO {
    private String blogTitle;
    private String blogContent;
    private Integer imgID;
    private Integer authorID;
    private String time;
}
