package com.tuling.common.web.handler;


import cn.dev33.satoken.exception.NotLoginException;
import cn.hutool.http.HttpStatus;
import com.tuling.common.core.exception.ServiceException;
import com.tuling.common.core.exception.TenantException;
import com.tuling.common.core.param.ApiResponse;
import com.tuling.common.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ApiResponse<String> error(Exception e) {


        log.error("发生了异常",e);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 获取请求的URI
        String requestURI = request.getRequestURI();

        // 获取上下文路径
        String contextPath = request.getContextPath();

        // 获取Servlet路径
        String servletPath = request.getServletPath();

        // 获取路径信息
        String pathInfo = request.getPathInfo();
        log.error("操作IP：{}", IpUtil.getClientIpAddr(request));
        log.error("requestURI：{}",requestURI);
        log.error("contextPath：{}",contextPath);
        log.error("servletPath：{}",servletPath);
        log.error("pathInfo：{}",pathInfo);
        if (e instanceof ServiceException) {

            return ApiResponse.error(((ServiceException) e).getCode(), e.getMessage());
        }
        if (e instanceof NotLoginException){
            NotLoginException notLoginException= (NotLoginException) e;
            String message="";
            if(notLoginException.getType().equals(NotLoginException.NOT_TOKEN)) {
                message = "登录信息已失效，请重新登录";
            }
            else if(notLoginException.getType().equals(NotLoginException.INVALID_TOKEN)) {
                message = "登录信息已失效，请重新登录";
            }
            else if(notLoginException.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
                message = "登录信息已失效，请重新登录";
            }
            else if(notLoginException.getType().equals(NotLoginException.BE_REPLACED)) {
                message = "已在其他设备登录，当前会话已退出";
            }
            else if(notLoginException.getType().equals(NotLoginException.KICK_OUT)) {
                message = "登录信息已失效";
            }
            else if(notLoginException.getType().equals(NotLoginException.TOKEN_FREEZE)) {
                message = "长时间未操作，当前登录已退出，请重新登录";
            }
            else if(notLoginException.getType().equals(NotLoginException.NO_PREFIX)) {
                message = "登录信息无效，请重新登录";
            }
            else {
                message = "登录信息已失效，请重新登录";
            }
           return ApiResponse.error(HttpStatus.HTTP_UNAUTHORIZED, message);
        }
        if (e instanceof TenantException){
            return ApiResponse.error(HttpStatus.HTTP_UNAUTHORIZED, "租户已到期，请联系管理员");
        }
        if ( e instanceof  NullPointerException){
            return ApiResponse.error(HttpStatus.HTTP_INTERNAL_ERROR, "空数据异常，请联系管理员");
        }


       return ApiResponse.error(HttpStatus.HTTP_INTERNAL_ERROR, e.getMessage()); }


}
