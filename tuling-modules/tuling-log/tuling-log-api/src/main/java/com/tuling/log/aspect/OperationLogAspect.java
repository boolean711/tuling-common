package com.tuling.log.aspect;

import cn.hutool.json.JSONUtil;
import com.tuling.common.satoken.param.LoginUserDetails;
import com.tuling.common.satoken.utils.LoginHelper;
import com.tuling.common.utils.IpUtil;
import com.tuling.log.annotations.OperationLog;
import com.tuling.log.domain.entity.SysOperationLog;
import com.tuling.log.service.SysOperationLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private SysOperationLogService operationLogService;

    @Pointcut("@annotation(com.tuling.log.annotations.OperationLog)")
    public void operationLogPointcut() {
    }

    @Around("operationLogPointcut()")
    public Object logOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = getMethodFromJoinPoint(joinPoint);
        OperationLog operationLogAnnotation = method.getAnnotation(OperationLog.class);
        String operationMethod = operationLogAnnotation.methodName();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String operationIp = IpUtil.getClientIpAddr(request);
        String operationParams = formatParameters(joinPoint.getArgs());
        SysOperationLog log = new SysOperationLog();
        log.setMethodName(operationMethod);

        log.setOperationTime(new Date());
        log.setOperationParams(operationParams);

        log.setOperationIp(operationIp);
        Object result;
        try {
            result = joinPoint.proceed();
            log.setOperationResult(JSONUtil.toJsonStr(result));
        } catch (Exception e) {
            // 记录异常日志
            log.setOperationResult("Exception: " + e.getMessage());
            throw e;
        } finally {
            LoginUserDetails loginUser = LoginHelper.getCurrentLoginUser();
            if (loginUser != null) {
                log.setOperator(loginUser.getNickName());
            }

            operationLogService.save(log);

        }
        return result;
    }

    private Method getMethodFromJoinPoint(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        String methodName = joinPoint.getSignature().getName();
        Class<?>[] parameterTypes = ((org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature()).getParameterTypes();
        return joinPoint.getTarget().getClass().getMethod(methodName, parameterTypes);
    }

    private String formatParameters(Object[] args) {
        if (args != null && args.length > 0) {
            return Arrays.stream(args)
                    .map(arg -> {
                        try {
                            return arg.getClass().getSimpleName() + ": " + JSONUtil.toJsonStr(arg);
                        } catch (Exception e) {
                            return arg.getClass().getSimpleName() + ": [Error converting to JSON]";
                        }
                    })
                    .collect(Collectors.joining(","));
        }
        return "";

    }
}
