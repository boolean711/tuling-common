package com.tuling.system.Interceptor;

import cn.dev33.satoken.fun.SaFunction;
import com.tuling.common.core.exception.ServiceException;
import com.tuling.common.core.properties.TenantProperties;
import com.tuling.common.satoken.processor.ISaTokenInterceptorArgs;
import com.tuling.common.satoken.utils.LoginHelper;
import com.tuling.system.domain.TlLoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

@Component
@Slf4j
public class SystemSaTokenInterceptorArgs implements ISaTokenInterceptorArgs {

    @Autowired
    private TenantProperties tenantProperties;




    @Override
    public String[] matchArgs() {
        return new String[]{"/**"};
    }

    @Override
    public String[] notMatchAras() {
        return new String[0];
    }

    @Override
    public TreeMap<Integer, List<SaFunction>> checkFunctionByOrder() {
        TreeMap<Integer, List<SaFunction>> res = new TreeMap<>();
        res.put(1, Collections.singletonList(this::checkTenantValid));
        return res;
    }

    private void checkTenantValid() {
       log.info("checkTenantValid：==============");
        if (tenantProperties.isEnable()) {
            TlLoginUser currentLoginUser = (TlLoginUser) LoginHelper.getCurrentLoginUser();

            if (currentLoginUser==null){
                throw new ServiceException("未知用户，请联系管理员").setCode(401);
            }
            if (!currentLoginUser.isAdmin() && !currentLoginUser.isAccountNonExpired()) {

                throw new ServiceException("租户已到期，请联系管理员").setCode(401);
            }
        }
    }
}
