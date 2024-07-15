package com.tuling.common.satoken.processor;

import cn.dev33.satoken.fun.SaFunction;
import cn.hutool.log.Log;
import cn.hutool.log.dialect.log4j.Log4jLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.TreeMap;


public interface ISaTokenInterceptorArgs extends InitializingBean {

    Logger log = LoggerFactory.getLogger(ISaTokenInterceptorArgs.class);
    String[] matchArgs();

    String[] notMatchAras();

    TreeMap<Integer, List<SaFunction>> checkFunctionByOrder();

    @Override
    default void afterPropertiesSet() {

        log.info("Initializing: {}", this.getClass().getName());

    }
}