package com.house.everything_house_backend.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.house.everything_house_backend.entities.User;
import com.house.everything_house_backend.service.ISysUserService;
import java.util.ArrayList;
import java.util.List;

public class UserExcelListener extends AnalysisEventListener<User> {

    private ISysUserService sysUserService;

    private static final int BATCH_COUNT = 5;
    private List<User> userList = new ArrayList<>();

    public UserExcelListener(ISysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Override
    public void invoke(User user, AnalysisContext analysisContext) {
        userList.add(user);
        if (userList.size() >= BATCH_COUNT) {
            saveData();
            userList.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
    }

    private void saveData() {
        sysUserService.importUsersFromXlsx(userList);
    }
}
