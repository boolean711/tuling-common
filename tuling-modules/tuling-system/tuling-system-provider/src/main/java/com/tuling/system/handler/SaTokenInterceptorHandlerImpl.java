package com.tuling.system.handler;

import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.tuling.common.core.exception.ServiceException;
import com.tuling.common.core.properties.TenantProperties;
import com.tuling.common.satoken.handler.ISaTokenInterceptorHandler;
import com.tuling.common.satoken.utils.LoginHelper;
import com.tuling.system.domain.TlLoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaTokenInterceptorHandlerImpl implements ISaTokenInterceptorHandler {

    @Autowired
    private TenantProperties tenantProperties;


    @Override
    public void run(Object obj) {
        // 登录校验 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
        SaRouter.match("/**").notMatch("/auth/doLogin", "/auth/logout").check(() -> {
            //校验登录
            StpUtil.checkLogin();
            //检查租户有效期
            checkTenantValid();

        });

    }

    private void checkTenantValid() {
        if (tenantProperties.isEnable()) {
            TlLoginUser currentLoginUser = (TlLoginUser) LoginHelper.getCurrentLoginUser();

            if (!currentLoginUser.isAdmin() && !currentLoginUser.isAccountNonExpired()) {

                throw new ServiceException("租户已到期，请联系管理员").setCode(401);
            }
        }
    }
}
