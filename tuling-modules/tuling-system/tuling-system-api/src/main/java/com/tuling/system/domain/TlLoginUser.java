package com.tuling.system.domain;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tuling.common.core.constants.PermissionConstants;
import com.tuling.common.satoken.param.LoginUserDetails;
import com.tuling.common.utils.SpringUtils;
import com.tuling.system.constants.CommonConstants;
import com.tuling.system.domain.vo.SysRoleVo;
import com.tuling.system.domain.vo.SysTenantVo;
import com.tuling.system.domain.vo.SysUserVo;
import com.tuling.system.service.SysTenantService;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TlLoginUser implements LoginUserDetails {


    private SysUserVo sysUserVo;

    public TlLoginUser() {

    }

    public TlLoginUser(SysUserVo sysUserVo) {
        this.sysUserVo = sysUserVo;
    }

    public boolean isInitialPassword() {
        if (!isAdmin()) {
            return BCrypt.checkpw(CommonConstants.DEFAULT_PASSWORD, getPassword());
        }
        return false;
    }


    @Override
    @JsonIgnore
    public Long getId() {
        return sysUserVo.getId();
    }

    @Override
    @JsonIgnore
    public List<String> getAuthorities() {
        return sysUserVo.getPermissionList();
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return sysUserVo.getPassword();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return sysUserVo.getUsername();
    }

    @Override
    @JsonIgnore
    public String getNickName() {
        return  sysUserVo.getNickName();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return checkTenantValid();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAdmin() {
        List<String> permissionList = sysUserVo.getPermissionList();

        if (CollectionUtils.isNotEmpty(permissionList)) {
            return permissionList.contains(PermissionConstants.ADMIN);
        }
        return false;
    }

    @Override
    @JsonIgnore
    public Long getTenantId() {
        SysTenantVo tenantVo = sysUserVo.getTenantVo();
        return tenantVo.getId();
    }

    @Override
    @JsonIgnore
    public List<Long> getRoleIdList() {
        List<SysRoleVo> roleList = sysUserVo.getRoleList();

        if (CollectionUtils.isNotEmpty(roleList)) {
            return roleList.stream().map(SysRoleVo::getId).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private boolean checkTenantValid() {
        if (!isAdmin()) {

            SysTenantVo tenantVo = sysUserVo.getTenantVo();

            if (tenantVo != null) {
                SysTenantService tenantService = SpringUtils.getBean(SysTenantService.class);
                return tenantService.isTenantValid(tenantVo.getId());
            }
            return false;

        }
        return true;

    }
}
