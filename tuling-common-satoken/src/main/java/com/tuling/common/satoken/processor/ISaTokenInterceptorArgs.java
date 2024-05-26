package com.tuling.common.satoken.processor;

import cn.dev33.satoken.fun.SaFunction;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public interface ISaTokenInterceptorArgs {

    String[] matchArgs();

    String[] notMatchAras();

    TreeMap<Integer, List<SaFunction>> checkFunctionByOrder();


}
