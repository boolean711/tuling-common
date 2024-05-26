package com.tuling.auth.interceptor;

import cn.dev33.satoken.fun.SaFunction;
import cn.dev33.satoken.stp.StpUtil;
import com.tuling.common.satoken.processor.ISaTokenInterceptorArgs;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AuthSaTokenInterceptorArgs implements ISaTokenInterceptorArgs {




    @Override
    public String[] matchArgs() {
        return new String[]{"/**"};
    }

    @Override
    public String[] notMatchAras() {
        return new String[]{"/auth/doLogin", "/auth/logout"};
    }

    @Override
    public TreeMap<Integer, List<SaFunction>> checkFunctionByOrder() {
        TreeMap<Integer, List<SaFunction>> res = new TreeMap<>();
        res.put(0, Collections.singletonList(StpUtil::checkLogin));
        return res;
    }
}
