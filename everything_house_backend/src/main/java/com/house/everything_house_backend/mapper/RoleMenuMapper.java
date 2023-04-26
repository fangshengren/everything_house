package com.house.everything_house_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.house.everything_house_backend.entities.RoleMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    //根据角色id删除角色菜单数据
    @Delete("delete from sys_role_menu where role_id=#{roleId}")
    int deleteByRoleId(@Param("roleId") Integer roleId);

    //根据角色id查找菜单id
    @Select("select menu_Id from sys_role_menu where role_id=#{roleId}")
    List<Integer> selectByRoleId(@Param("roleId") Integer roleId);
}
