package com.tuling.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.json.JSONUtil;
import com.aliyun.captcha20230305.Client;
import com.aliyun.captcha20230305.models.VerifyIntelligentCaptchaRequest;
import com.aliyun.captcha20230305.models.VerifyIntelligentCaptchaResponse;
import com.aliyun.captcha20230305.models.VerifyIntelligentCaptchaResponseBody;

import com.aliyun.teaopenapi.models.Config;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.tuling.auth.domain.dto.UpdatePasswordDto;
import com.tuling.auth.domain.dto.UserLoginDto;
import com.tuling.auth.domain.vo.UserLoginVo;
import com.tuling.auth.service.LoginService;
import com.tuling.common.core.exception.ServiceException;
import com.tuling.common.core.param.ApiResponse;
import com.tuling.common.core.properties.TenantProperties;
import com.tuling.common.satoken.param.LoginUserDetails;
import com.tuling.common.satoken.utils.LoginHelper;
import com.tuling.common.utils.IpUtil;
import com.tuling.system.constants.RedisPrefixKey;
import com.tuling.system.domain.TlLoginUser;
import com.tuling.system.domain.entity.SysTenant;
import com.tuling.system.domain.entity.SysUser;
import com.tuling.system.domain.vo.SysRoleVo;
import com.tuling.system.domain.vo.SysTenantVo;
import com.tuling.system.domain.vo.SysUserVo;
import com.tuling.system.service.SysRoleService;
import com.tuling.system.service.SysTenantService;
import com.tuling.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class LoginServiceImpl implements LoginService {


    @Autowired
    private SysUserService userService;

    @Autowired
    private SysRoleService roleService;


    @Autowired
    private SysTenantService tenantService;

    @Autowired
    @Qualifier("jacksonRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;


    @Autowired
    private TenantProperties tenantProperties;

    private static final Long MAX_INCORRECT_ATTEMPTS = 5L;


    @Autowired
    private Client client;

    @Override
    public UserLoginVo loginByPassword(UserLoginDto loginDto) {
        UserLoginVo userLoginVo = new UserLoginVo();
        VerifyIntelligentCaptchaResponseBody captchaResponseBody = verifyIntelligentCaptcha(loginDto.getSceneId(), loginDto.getCaptchaVerifyParam());

        userLoginVo.setCaptchaResponseBody(captchaResponseBody);

        if (captchaResponseBody.getResult().verifyResult) {
            String incorrectLoginCountKey = String.format(RedisPrefixKey.INCORRECT_LOGIN_COUNT_PREFIX, loginDto.getUsername(), loginDto.getTenantId());
            List<SysUserVo> userVoList = userService.getUserByUsername(loginDto.getUsername(), loginDto.getTenantId());

            if (CollectionUtils.isEmpty(userVoList)) {
                throw new ServiceException("用户名/密码错误");
            }
            SysUserVo userByUsername = userVoList.get(0);
            setRoleList(userByUsername);
            setUserPermissions(userByUsername);
            setTenantVo(userByUsername);
            TlLoginUser loginUser = new TlLoginUser(userByUsername);


            checkLoginTimes(incorrectLoginCountKey, loginUser);

            checkPassword(loginDto, incorrectLoginCountKey, userByUsername);

            if (checkTenantValid(loginUser, userByUsername)) {
                String s = loginUserAndReturnToken(loginUser, userByUsername);

                userLoginVo.setToken(s);
                return userLoginVo;
            }

            throw new ServiceException("租户已到期，请联系管理员");
        }
        return userLoginVo;


    }


    @Override
    public void updatePassword(UpdatePasswordDto dto) {
        LoginUserDetails loginUser = LoginHelper.getCurrentLoginUser();

        if (loginUser == null) {

            throw new ServiceException("修改密码时发生异常，请联系管理员");
        }

        SysUser sysUser = userService.getById(loginUser.getId());

        if (!BCrypt.checkpw(dto.getOldPassword(), sysUser.getPassword())) {
            throw new ServiceException("旧密码不正确");
        }

        sysUser.setPassword(BCrypt.hashpw(dto.getNewPassword()));

        userService.updateById(sysUser);
        StpUtil.logout();

    }

    public VerifyIntelligentCaptchaResponseBody verifyIntelligentCaptcha(String sceneId, String captchaVerifyParam) {
        // 创建APi请求
        VerifyIntelligentCaptchaRequest request = new VerifyIntelligentCaptchaRequest();
        // 本次验证的场景ID，建议传入，防止前端被篡改场景
        request.sceneId = sceneId;
        // 前端传来的验证参数 CaptchaVerifyParam
        request.captchaVerifyParam = captchaVerifyParam;
        try {

            VerifyIntelligentCaptchaResponse resp = client.verifyIntelligentCaptcha(request);
            // 建议使用您系统中的日志组件，打印返回
            // 获取验证码验证结果（请注意判空），将结果返回给前端。出现异常建议认为验证通过，优先保证业务可用，然后尽快排查异常原因。
            log.info("resp:{}", JSONUtil.toJsonStr(resp));
            return resp.getBody();
        } catch (Exception e) {
            // 建议使用您系统中的日志组件，打印异常
            // 出现异常建议认为验证通过，优先保证业务可用，然后尽快排查异常原因。
            log.error("error:", e);
            throw new ServiceException(e);
        }
    }

    private void setTenantVo(SysUserVo user) {
        SysTenant sysTenant = tenantService.getById(user.getTenantId());

        if (sysTenant != null) {
            user.setTenantVo(BeanUtil.toBean(sysTenant, SysTenantVo.class));
        }


    }

    private void setUserPermissions(SysUserVo user) {
        List<SysRoleVo> roleList = user.getRoleList();

        if (CollectionUtils.isNotEmpty(roleList)) {
            List<Long> roleIds = roleList.stream().map(SysRoleVo::getId).collect(Collectors.toList());
            List<String> permissionCodeByRoleIds = roleService.getPermissionCodeByRoleIds(roleIds);

            user.setPermissionList(permissionCodeByRoleIds);
        }

    }

    private boolean checkTenantValid(LoginUserDetails loginUser, SysUserVo user) {

        if (loginUser.isAdmin()) {
            return true;
        }
        if (tenantProperties.isEnable()) {
            return tenantService.isTenantValid(user.getTenantId());
        } else {
            return true;
        }

    }


    private Long incrementIncorrectLoginCount(String key) {
        try {
            Long increment = redisTemplate.opsForValue().increment(key, 1);

            redisTemplate.expire(key, Duration.ofDays(1));
            return increment;
        } catch (Exception e) {
            log.error("登录时redis异常", e);
            throw new ServiceException("登录时发生未知异常，请联系管理员");
        }
    }

    private String loginUserAndReturnToken(LoginUserDetails loginUser, SysUserVo user) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            user.setLastLoginIp(IpUtil.getClientIpAddr(request));
        }
        LoginHelper.login(loginUser, null);
        user.setLastLoginTime(new Date());
        userService.updateById(BeanUtil.toBean(user, SysUser.class));
        return StpUtil.getTokenInfo().tokenValue;
    }

    public void setRoleList(SysUserVo sysUserVo) {

        List<SysRoleVo> roleListByUserId = roleService.getRoleListByUserId(sysUserVo.getId());

        sysUserVo.setRoleList(roleListByUserId);

    }

    private void checkPassword(UserLoginDto loginDto, String incorrectLoginCountKey, SysUserVo userByUsername) {
        if (!BCrypt.checkpw(loginDto.getPassword(), userByUsername.getPassword())) {
            Long aLong = incrementIncorrectLoginCount(incorrectLoginCountKey);
            Long residue = MAX_INCORRECT_ATTEMPTS - aLong;
            if (residue == 0) {
                throw new ServiceException("24小时内密码输入错误次数过多，请联系管理员");
            }
            throw new ServiceException("用户名/密码错误，剩余尝试次数：" + residue);
        }
    }

    private void checkLoginTimes(String incorrectLoginCountKey, TlLoginUser loginUser) {
        if (!loginUser.isAdmin()) {
            Integer times = (Integer) redisTemplate.opsForValue().get(incorrectLoginCountKey);
            if (times == null) {
                times = 0;
            }
            if (times >= MAX_INCORRECT_ATTEMPTS) {
                throw new ServiceException("24小时内密码输入错误次数过多，请联系管理员");
            }
        }
    }


}
