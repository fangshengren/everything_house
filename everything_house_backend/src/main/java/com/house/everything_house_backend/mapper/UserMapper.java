package com.house.everything_house_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.house.everything_house_backend.entities.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from sys_user limit #{pageNum},#{pageSize}")
    List<User> selectPage(Integer pageNum, Integer pageSize);
    @Select("select * from sys_user where username like #{userName} limit #{pageNum},#{pageSize}")
    List<User> selectPageByUserName(Integer pageNum, Integer pageSize,String userName);

    @Select("select * from sys_user")
    List<User> findAll();
    List<User> selectByMultipleCondition(Map<String, Object> params);
    Integer selectTotalByMultipleCondition(Map<String, Object> params);
    int insertUser(User user);
    int insertMoreUser(@Param("userList") List<User> userList);
    int updateUser(User user);

    @Delete("delete from sys_user where id=#{id}")
    int deleteById(@Param("id") Integer id);

    boolean removeByIds(Long[] ids);
}
