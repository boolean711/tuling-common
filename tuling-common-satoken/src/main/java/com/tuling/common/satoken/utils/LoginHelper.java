package com.tuling.common.satoken.utils;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaStorage;
import cn.dev33.satoken.dao.SaSessionForJacksonCustomized;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.tuling.common.core.constants.PermissionConstants;
import com.tuling.common.satoken.manager.SaTokenHolderManager;
import com.tuling.common.satoken.param.LoginUserDetails;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.*;
import java.util.function.Supplier;

/**
 * 登录鉴权助手
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class LoginHelper {

    public static final String LOGIN_USER_KEY = "loginUser:";

    public static final String AUTHENTICATION_LOGIN_SESSION = "Authentication:login:session:%s";


    public static final String AUTHENTICATION_LOGIN_TOKEN = "Authentication:login:token:%s";

    public static final String LOGIN_USER_SESSION_EXTRA_KEY = "loginUserSessionExtra:";


    /**
     * 登录系统 基于 设备类型
     * 针对相同用户体系不同设备
     *
     * @param loginUser 登录用户信息
     * @param model     配置参数
     */
    public static void login(LoginUserDetails loginUser, SaLoginModel model) {
        storageSetLoginUser(loginUser);


        model = ObjectUtil.defaultIfNull(model, new SaLoginModel());
        model.setExtra(LOGIN_USER_KEY, loginUser);
        StpUtil.login(loginUser.getId(), model);

        SaSession session = StpUtil.getSession();
        session.updateTimeout(model.getTimeout());

        session.set(LOGIN_USER_SESSION_EXTRA_KEY, loginUser);

    }

    public static void storageSetLoginUser(LoginUserDetails loginUser) {
        SaStorage storage = SaHolder.getStorage();
        storage.set(LOGIN_USER_KEY, loginUser);
    }


    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static LoginUserDetails getCurrentLoginUser() {

        Object storageIfAbsentSet = getStorageIfAbsentSet(LOGIN_USER_KEY, () -> {

            String token = SaTokenHolderManager.getToken();


            return getLoginUserByToken(token);

        });


        if (storageIfAbsentSet != null) {
            if (storageIfAbsentSet instanceof LoginUserDetails) {
                return (LoginUserDetails) storageIfAbsentSet;
            } else if (storageIfAbsentSet instanceof JSONObject) {
                return JSONUtil.toBean((JSONObject) storageIfAbsentSet, LoginUserDetails.class);
            }


        }
        return null;
    }

    /**
     * 获取登录用户，仅限有token
     *
     * @param id
     * @return
     */
    public static LoginUserDetails getLoginUserById(Long id) {
        SaTokenDao saTokenDao = SaManager.getSaTokenDao();
        SaSessionForJacksonCustomized session = (SaSessionForJacksonCustomized) saTokenDao.getObject(String.format(AUTHENTICATION_LOGIN_SESSION, id));

        if (session != null) {
            return (LoginUserDetails) session.get(LOGIN_USER_SESSION_EXTRA_KEY);
        }
        return null;

    }


    /**
     * 获取用户基于token
     */
    public static LoginUserDetails getLoginUserByToken(String token) {

        SaTokenDao saTokenDao = SaManager.getSaTokenDao();

        String id = saTokenDao.get(String.format(AUTHENTICATION_LOGIN_TOKEN, token));

        if (StrUtil.isNotBlank(id)) {
            return getLoginUserById(Long.parseLong(id));

        }
        return null;
    }


    public static void resetContext(RequestAttributes requestAttributes, String token) {
        RequestContextHolder.setRequestAttributes(requestAttributes);
        SaTokenHolderManager.setToken(token);
    }


    public static List<Long> getRoleIdList() {
        LoginUserDetails currentLoginUser = getCurrentLoginUser();
        if (currentLoginUser != null) {
            return currentLoginUser.getRoleIdList();
        }

        return Collections.emptyList();
    }

    /**
     * 获取权限集合
     *
     * @return
     */
    public static List<String> getAuthorities() {

        LoginUserDetails currentLoginUser = getCurrentLoginUser();
        if (currentLoginUser != null) {
            return currentLoginUser.getAuthorities();
        }

        return Collections.emptyList();
    }

    private static Object getExtra(String key) {

        return StpUtil.getExtra(key);
    }

    /**
     * 是否为超级管理员
     *
     * @param
     * @return 结果
     */
    public static boolean isAdmin() {
        LoginUserDetails loginUser = getCurrentLoginUser();
        if (loginUser != null) {
            return loginUser.isAdmin();
        }

        return false;
    }

    /**
     * 是否为租户管理员
     *
     * @param
     * @return 结果
     */
    public static boolean isTenantAdmin() {
        LoginUserDetails loginUser = getCurrentLoginUser();
        if (loginUser != null) {
            return loginUser.getAuthorities().contains(PermissionConstants.TENANT_ADMIN);
        }

        return false;
    }


    public static Long getCurrentTenantId() {

        if (isAdmin()){
            return -1L;
        }
        LoginUserDetails loginUser = getCurrentLoginUser();


        if (loginUser != null) {
            return loginUser.getTenantId();
        }

        return null;
    }

    public static boolean isLogin() {
        return getCurrentLoginUser() != null;
    }

    private static Object getStorageIfAbsentSet(String key, Supplier<Object> handle) {
        try {
            Object obj = SaHolder.getStorage().get(key);
            if (ObjectUtil.isNull(obj)) {
                obj = handle.get();
                SaHolder.getStorage().set(key, obj);
            }
            return obj;
        } catch (Exception e) {

            log.error("getStorageIfAbsentSet发生异常", e);
            return null;
        }
    }
}
