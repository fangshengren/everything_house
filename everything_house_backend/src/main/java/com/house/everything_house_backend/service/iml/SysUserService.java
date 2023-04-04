package com.house.everything_house_backend.service.iml;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.house.everything_house_backend.entities.User;
import com.house.everything_house_backend.mapper.UserMapper;
import com.house.everything_house_backend.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class SysUserService extends ServiceImpl<UserMapper, User> implements ISysUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> selectPage(Integer pageNum, Integer pageSize) {
        return userMapper.selectPage(pageNum,pageSize);
    }
    @Override
    public List<User> selectPageByUserName(Integer pageNum, Integer pageSize, String userName) {
        return userMapper.selectPageByUserName(pageNum,pageSize,userName);
    }
    @Override
    public int insert(User user){
        return userMapper.insertUser(user);
    }
    @Override
    public boolean importUsersFromXlsx(List<User> userList) {
        return saveBatch(userList);
    }

    @Override
    public List<User> selectByMultipleCondition(Map<String, Object> params){
        return userMapper.selectByMultipleCondition(params);
    }
    @Override
    public int selectTotalByMultipleCondition(Map<String, Object> params){
        return userMapper.selectTotalByMultipleCondition(params);
    }

    @Override
    public Integer deleteById(Integer id) {
        return userMapper.deleteById(id);
    }
    @Override
    public boolean removeByIds(Long[] ids){
        return userMapper.removeByIds(ids);
    }

    @Override
    public boolean save(User entity) {
        return super.save(entity);
    }
}
