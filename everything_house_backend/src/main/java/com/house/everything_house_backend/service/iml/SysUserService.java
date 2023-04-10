package com.house.everything_house_backend.service.iml;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.house.everything_house_backend.common.Constants;
import com.house.everything_house_backend.entities.Menu;
import com.house.everything_house_backend.controller.dto.UserDTO;
import com.house.everything_house_backend.entities.User;
import com.house.everything_house_backend.exception.ServiceException;
import com.house.everything_house_backend.mapper.RoleMapper;
import com.house.everything_house_backend.mapper.RoleMenuMapper;
import com.house.everything_house_backend.mapper.UserMapper;
import com.house.everything_house_backend.service.ISysUserService;
import com.house.everything_house_backend.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class SysUserService extends ServiceImpl<UserMapper, User> implements ISysUserService {
    @Autowired
    private UserMapper userMapper;
    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private MenuService menuService;

    @Override
    public List<User> selectPage(Integer pageNum, Integer pageSize) {
        return userMapper.selectPage(pageNum,pageSize);
    }
    @Override
    public List<User> selectPageByUserName(Integer pageNum, Integer pageSize, String userName) {
        return userMapper.selectPageByUserName(pageNum,pageSize,userName);
    }
    @Override
    public int selectNumberOfUser(){
        return userMapper.selectNumberOfUser();
    }
    @Override
    public int selectNumberOfAdmin(){
        return userMapper.selectNumberOfAdmin();
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

    public UserDTO login(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",userDTO.getUsername());
        queryWrapper.eq("password",userDTO.getPassword());
        User one;
        try{
            one=getOne(queryWrapper);
        }catch (Exception e){
            throw new ServiceException(Constants.CODE_500,"系统错误");//这里假设查询了多于1条记录，就让他报系统错误
        }
        if(one!=null){  //以下是登录判断业务
            BeanUtil.copyProperties(one,userDTO,true);
            //设置token
            String token= TokenUtils.genToken(one.getId().toString(),one.getPassword().toString());
            userDTO.setToken(token);
            String role=one.getRole();//查询出用户的角色标识，比如ROLE_ADMIN
            //设置用户的菜单列表
            List<Menu> roleMenus=getRoleMenus(role);
            userDTO.setMenus(roleMenus);
            return userDTO;//返回登录类userDTO
        }else {
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }
    }

    /**
     * 获取当前用户的菜单列表

     */
    private List<Menu> getRoleMenus(String roleFlag){
        //根据角色标识获取角色Id
        Integer roleId=roleMapper.selectByflag(roleFlag);
        //当前角色Id的所有菜单id集合
        List<Integer> menuIds=roleMenuMapper.selectByRoleId(roleId);
        //查出系统所有菜单
        List<Menu> menus=menuService.findMenus("");
        //筛选当前用户菜单
        List<Menu> roleMenus=new ArrayList<>();
        for(Menu menu:menus){
            if(menuIds.contains(menu.getId())){
                roleMenus.add(menu);
            }
            List<Menu> children=menu.getChildren();
            //removeIf移除children里面不在menuIds集合中的元素
            children.removeIf(child->!menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
}
