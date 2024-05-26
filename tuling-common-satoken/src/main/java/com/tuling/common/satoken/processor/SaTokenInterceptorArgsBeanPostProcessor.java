package com.tuling.common.satoken.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class SaTokenInterceptorArgsBeanPostProcessor implements BeanPostProcessor {

    @Autowired
   private SaTokenInterceptorArgsHolder handlerHolder;
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ISaTokenInterceptorArgs){
            ISaTokenInterceptorArgs args= (ISaTokenInterceptorArgs) bean;
            handlerHolder.setMatchArgs(Arrays.asList(args.matchArgs()));
            handlerHolder.setNotMatchAras(Arrays.asList(args.notMatchAras()));
            handlerHolder.setCheckFunctionByOrder(args.checkFunctionByOrder());
            log.info("argsHolder init：{} ",beanName);
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
