package com.tuling.auth.controller;


import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpStatus;

import com.tuling.auth.constants.RedisKeyPrefixConstants;
import com.tuling.auth.domain.dto.UpdatePasswordDto;
import com.tuling.auth.domain.dto.UserLoginDto;
import com.tuling.auth.domain.vo.UserLoginVo;
import com.tuling.auth.service.LoginService;
import com.tuling.common.core.exception.ServiceException;
import com.tuling.common.core.param.ApiResponse;
import com.tuling.common.core.properties.TencentCloudProperties;
import com.tuling.common.satoken.param.LoginUserDetails;
import com.tuling.common.satoken.utils.LoginHelper;
import com.tuling.common.sms.service.SmsService;
import com.tuling.log.annotations.OperationLog;
import com.tuling.system.domain.TlLoginUser;
import com.tuling.system.domain.vo.SysTenantVo;
import com.tuling.system.domain.vo.SysUserVo;
import com.tuling.system.service.SysTenantService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
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

    @Autowired
    private TencentCloudProperties tencentCloudProperties;

    @Autowired
    private SmsService smsService;


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
    @PostMapping("/doLoginByPhoneNumCode")
    @OperationLog(methodName = "doLoginByPhoneNumCode")
    public ApiResponse<UserLoginVo> doLoginByPhoneNumCode(@RequestParam("phoneNum")String phoneNum,
                                                          @Param("code") String code,
                                                          @Param("tenantId") Long tenantId) {

        UserLoginVo vo = loginService.doLoginByPhoneNumCode(phoneNum,code,tenantId);

        return  ApiResponse.success(vo);

    }


    @PostMapping("/sendLoginPhoneNumCode")
    public ApiResponse<String> sendPhoneNumCode(@RequestParam("phoneNum") String phoneNum) {
        String noticeTemplateId = tencentCloudProperties.getTemplateId().get("smsCode");

        String codeKey = String.format(RedisKeyPrefixConstants.LOGIN_PHONE_NUM_CODE_PREFIX, phoneNum);
        return ApiResponse.success(smsService.sendPhoneNumCode(codeKey, phoneNum, RandomUtil.randomNumbers(6), noticeTemplateId));
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
        return ApiResponse.successNoData().setMessage("退出成功").setShowMessage(true);
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
