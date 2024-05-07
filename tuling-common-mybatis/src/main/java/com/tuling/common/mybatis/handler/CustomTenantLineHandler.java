package com.tuling.common.mybatis.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;

import com.tuling.common.core.properties.TenantProperties;
import com.tuling.common.satoken.param.LoginUserDetails;
import com.tuling.common.satoken.utils.LoginHelper;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

public class CustomTenantLineHandler implements TenantLineHandler {
    private final String TENANT_COLUMN = "tenant_id";


    private TenantProperties tenantProperties;

    public CustomTenantLineHandler(TenantProperties tenantProperties) {
        this.tenantProperties = tenantProperties;
    }


    @Override
    public Expression getTenantId() {
        LoginUserDetails loginUser = LoginHelper.getCurrentLoginUser();

        if (loginUser != null) {
            if (loginUser.isAdmin()) {
                return new LongValue(-1L);
            }
            return new LongValue(loginUser.getTenantId());
        }


        return new LongValue();
    }

    @Override
    public String getTenantIdColumn() {
        return TENANT_COLUMN;
    }

    @Override
    public boolean ignoreTable(String tableName) {
        LoginUserDetails loginUser = LoginHelper.getCurrentLoginUser();
        if (loginUser != null) {
            if (loginUser.isAdmin()) {
                return true;
            }

        }
        if (!tenantProperties.isEnable()) {
            return true;
        }


        if (!tenantProperties.getTableName().contains(tableName)) {
            return true;
        }

        return false;
    }
}
