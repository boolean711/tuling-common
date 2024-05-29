package com.tuling.security.Interceptor;

import cn.dev33.satoken.fun.SaFunction;
import com.tuling.common.core.exception.ServiceException;
import com.tuling.common.core.properties.TenantProperties;
import com.tuling.common.satoken.processor.ISaTokenInterceptorArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

@Component
@Slf4j
public class SecuritySaTokenInterceptorArgs implements ISaTokenInterceptorArgs {



    @Override
    public String[] matchArgs() {
        return new String[]{"/**"};
    }

    @Override
    public String[] notMatchAras() {
        return new String[]{"/security/encryption/publicKey"};
    }

    @Override
    public TreeMap<Integer, List<SaFunction>> checkFunctionByOrder() {

        return new TreeMap<>();
    }


}
