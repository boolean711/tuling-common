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
        log.error("操作IP：{}", IpUtil.getClientIpAddr(request));
        log.error("请求路径：{}",request.getContextPath());
        if (e instanceof ServiceException) {
            return ApiResponse.error(((ServiceException) e).getCode(), e.getMessage());
        }
        if (e instanceof NotLoginException){
           return ApiResponse.error(HttpStatus.HTTP_UNAUTHORIZED, "登录信息已失效，请重新登录");
        }
        if (e instanceof TenantException){
            return ApiResponse.error(HttpStatus.HTTP_UNAUTHORIZED, "租户已到期，请联系管理员");
        }
        if ( e instanceof  NullPointerException){
            return ApiResponse.error(HttpStatus.HTTP_INTERNAL_ERROR, "空数据异常，请联系管理员");
        }


       return ApiResponse.error(HttpStatus.HTTP_INTERNAL_ERROR, e.getMessage()); }


}
