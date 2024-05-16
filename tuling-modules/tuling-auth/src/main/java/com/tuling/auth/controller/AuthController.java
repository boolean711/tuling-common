package com.tuling.auth.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.http.HttpStatus;
import com.tuling.auth.domain.UpdatePasswordDto;
import com.tuling.auth.domain.UserLoginDto;
import com.tuling.auth.service.LoginService;
import com.tuling.common.core.exception.ServiceException;
import com.tuling.common.core.param.ApiResponse;
import com.tuling.common.satoken.utils.LoginHelper;
import com.tuling.system.domain.TlLoginUser;
import com.tuling.system.domain.vo.SysTenantVo;
import com.tuling.system.domain.vo.SysUserVo;
import com.tuling.system.service.SysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 登录测试
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private LoginService loginService;

    @Value("${version}")
    private String version;

    @Autowired
    private SysTenantService tenantService;

    @PostMapping("/doLogin")
    public ApiResponse<String> doLogin(@RequestBody @Validated UserLoginDto loginDto) {

        String s = loginService.loginByPassword(loginDto);

        return ApiResponse.success(s).setMessage("登录成功").setShowMessage(true);
    }



    @GetMapping("/userInfo")
    public ApiResponse<SysUserVo> userInfo() {
        TlLoginUser loginUser = (TlLoginUser) LoginHelper.getCurrentLoginUser();

        if (loginUser != null) {
            SysUserVo sysUserVo = loginUser.getSysUserVo();
            SysTenantVo tenantVo = sysUserVo.getTenantVo();
            if (tenantVo != null) {
                SysTenantVo vo = tenantService.getInfoById(tenantVo.getId());
                sysUserVo.setTenantVo(vo);
            }

            sysUserVo.setVersion(version);
            sysUserVo.setPassword(null);
            return ApiResponse.success(sysUserVo);
        }
        throw new ServiceException("登录信息已失效").setCode(HttpStatus.HTTP_UNAUTHORIZED);

    }


    @PostMapping("/updatePassword")
    public ApiResponse<Boolean> updatePassword(@RequestBody UpdatePasswordDto dto) {

        loginService.updatePassword(dto);

        return ApiResponse.success(true);

    }

    @RequestMapping("logout")
    public ApiResponse<Void> logout() {
        StpUtil.logout();
        return ApiResponse.successNoData();
    }


    @GetMapping("/initialPassword")
    public ApiResponse<Boolean> initialPassword() {
        TlLoginUser currentLoginUser = (TlLoginUser) LoginHelper.getCurrentLoginUser();
        if (currentLoginUser == null) {
            throw new ServiceException("未获取到登录信息,请重新登录").setCode(401);
        }
        return ApiResponse.success(currentLoginUser.isInitialPassword());

    }


}
