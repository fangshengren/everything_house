package com.house.everything_house_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.house.everything_house_backend.entities.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    //根据角色唯一标识flag查找角色id
    @Select("select id from sys_role where flag=#{flag}")
    Integer selectByflag(@Param("flag") String role);
}
