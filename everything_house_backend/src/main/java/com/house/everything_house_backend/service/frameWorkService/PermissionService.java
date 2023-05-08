package com.house.everything_house_backend.service.frameWorkService;

import com.house.everything_house_backend.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("permission")
public class PermissionService {
    @Autowired
    private SysUserServiceImpl sysUserServiceImpl;
//    待完成
    public boolean isRoleAdmin(String permission) {
        return false;
    }

}
