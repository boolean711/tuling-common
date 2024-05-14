package com.tuling.common.satoken.handler;

import cn.dev33.satoken.stp.StpInterface;
import com.tuling.common.satoken.utils.LoginHelper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StpInterfaceImplHandler implements StpInterface {


    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return LoginHelper.getAuthorities();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return LoginHelper.getRoleIdList().stream().map(String::valueOf).collect(Collectors.toList());
    }
}
