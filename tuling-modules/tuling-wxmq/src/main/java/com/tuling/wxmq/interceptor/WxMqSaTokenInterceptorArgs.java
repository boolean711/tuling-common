package com.tuling.wxmq.interceptor;

import cn.dev33.satoken.fun.SaFunction;
import com.tuling.common.satoken.processor.ISaTokenInterceptorArgs;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.TreeMap;

@Component
public class WxMqSaTokenInterceptorArgs implements ISaTokenInterceptorArgs {



    @Override
    public String[] matchArgs() {
        return new String[]{"/**"};
    }

    @Override
    public String[] notMatchAras() {
        return new String[]{"/wx/**"};
    }

    @Override
    public TreeMap<Integer, List<SaFunction>> checkFunctionByOrder() {
        return new TreeMap<>();
    }
}
