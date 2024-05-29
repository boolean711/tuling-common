package com.tuling.auth.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;

import com.aliyun.captcha20230305.models.VerifyIntelligentCaptchaRequest;
import com.aliyun.captcha20230305.models.VerifyIntelligentCaptchaResponse;
import com.aliyun.captcha20230305.models.VerifyIntelligentCaptchaResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.tuling.auth.domain.dto.UpdatePasswordDto;
import com.tuling.auth.domain.dto.UserLoginDto;
import com.tuling.auth.domain.vo.UserLoginVo;
import com.tuling.auth.service.LoginService;
import com.tuling.common.core.exception.ServiceException;
import com.tuling.common.core.param.ApiResponse;
import com.tuling.common.satoken.param.LoginUserDetails;
import com.tuling.common.satoken.utils.LoginHelper;
import com.tuling.log.annotations.OperationLog;
import com.tuling.system.domain.TlLoginUser;
import com.tuling.system.domain.vo.SysTenantVo;
import com.tuling.system.domain.vo.SysUserVo;
import com.tuling.system.service.SysTenantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



/**
 * 登录测试
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private LoginService loginService;

    @Value("${version}")
    private String version;

    @Autowired
    private SysTenantService tenantService;

    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;

    private String sceneId="1gmllrc6";

    @PostMapping("/doLogin")
    @OperationLog(methodName = "doLogin")
    public ApiResponse<UserLoginVo> doLogin(@RequestBody @Validated UserLoginDto loginDto) {

        UserLoginVo vo = loginService.loginByPassword(loginDto);

        Boolean verifyResult = vo.getCaptchaResponseBody().getResult().verifyResult;
        if (verifyResult){
            return ApiResponse.success(vo).setMessage("登录成功").setShowMessage(true);
        }
        return  ApiResponse.success(vo);

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
    @OperationLog(methodName = "updatePassword")
    public ApiResponse<Boolean> updatePassword(@RequestBody UpdatePasswordDto dto) {

        loginService.updatePassword(dto);

        return ApiResponse.success(true);

    }

    @RequestMapping("/logout")
    @OperationLog(methodName = "logout")
    public ApiResponse<Void> logout() {
        LoginUserDetails loginUser = LoginHelper.getCurrentLoginUser();

        StpUtil.logout();
        //设置临时用户,主动退出时可记录日志使用
        LoginHelper.storageSetLoginUser(loginUser);
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
