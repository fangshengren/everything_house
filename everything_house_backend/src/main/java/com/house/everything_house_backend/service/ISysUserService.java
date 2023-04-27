package com.house.everything_house_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.house.everything_house_backend.controller.dto.UserDTO;
import com.house.everything_house_backend.entities.User;

import java.util.List;
import java.util.Map;

public interface ISysUserService{
    public List<User> selectPage(Integer pageNum, Integer pageSize);
    public List<User> selectPageByUserName(Integer pageNum, Integer pageSize, String userName);
    public int insert(User user);

    List<User> selectByMultipleCondition(Map<String, Object> params);

    int selectTotalByMultipleCondition(Map<String, Object> params);
    public Integer deleteById(Integer id);
    boolean removeByIds(Long[] ids);
    boolean importUsersFromXlsx(List<User> userList);

    int updateUser(User user);

    UserDTO login(UserDTO userDTO);

    public UserDTO loginByEmail(UserDTO userDTO);

    int selectNumberOfUser();

    int selectNumberOfAdmin();

    public int selectNumberOfUserName(String queryUserName);

    public void sendCode(String email,String subject);

    public void sendUserDetailEmail(String username,String password,String email);

    public UserDTO bindEmail(UserDTO userDTO);

    public UserDTO unbindEmail(UserDTO userDTO);

}
