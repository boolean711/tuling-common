package com.tuling.common.satoken.config;

import cn.dev33.satoken.stp.StpInterface;
import com.tuling.common.satoken.param.LoginUserDetails;
import com.tuling.common.satoken.utils.LoginHelper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class StpInterfaceImpl implements StpInterface {
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return LoginHelper.getAuthorities();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        //TODO
        return new ArrayList<>();

    }
}
