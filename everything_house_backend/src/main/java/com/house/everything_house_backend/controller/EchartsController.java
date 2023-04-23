package com.house.everything_house_backend.controller;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import com.house.everything_house_backend.common.Result;
import com.house.everything_house_backend.entities.User;
import com.house.everything_house_backend.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/echarts")
public class EchartsController {
    @Autowired
    private SysUserServiceImpl sysUserServiceImpl;
    //统计每个季度的会员人数
    @GetMapping("/members")
    public Result members(){
        List<User> list= sysUserServiceImpl.list();
        int q1=0;//分别定义四个季度
        int q2=0;
        int q3=0;
        int q4=0;
        for(User user:list){
            Date createTime=user.getCreatedTime();
            Quarter quarter= DateUtil.quarterEnum(createTime);
            switch (quarter){
                case Q1:q1+=1;break;
                case Q2:q2+=1;break;
                case Q3:q3+=1;break;
                case Q4:q4+=1;break;
                default:break;
            }
        }
        return Result.success(CollUtil.newArrayList(q1,q2,q3,q4));
    }
}
