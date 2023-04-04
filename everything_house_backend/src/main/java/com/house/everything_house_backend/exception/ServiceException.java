package com.house.everything_house_backend.exception;

import lombok.Getter;

//自定义异常
@Getter
public class ServiceException extends RuntimeException{
    private String code;

    public  ServiceException(String code,String msg){
        super(msg);
        this.code=code;
    }
}
