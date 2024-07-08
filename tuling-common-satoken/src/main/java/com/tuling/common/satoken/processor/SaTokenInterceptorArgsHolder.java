package com.tuling.common.satoken.processor;

import cn.dev33.satoken.fun.SaFunction;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SaTokenInterceptorArgsHolder {

    private List<String> matchArgs = new ArrayList<>();


    private List<String> notMatchAras = new ArrayList<>();

    private Map<Integer, List<SaFunction>> checkFunctionByOrder = new TreeMap<>();

    public void setMatchArgs(List<String> matchArgs) {
        if (CollectionUtil.isNotEmpty(matchArgs)) {
            this.matchArgs = Stream.concat(this.matchArgs.stream(), matchArgs.stream())
                    .distinct()
                    .collect(Collectors.toList());
        }

    }

    public void setNotMatchAras(List<String> notMatchAras) {
        if (CollectionUtil.isNotEmpty(notMatchAras)) {
            this.notMatchAras = Stream.concat(this.notMatchAras.stream(), notMatchAras.stream())
                    .distinct()
                    .collect(Collectors.toList());
        }

    }

    public void setCheckFunctionByOrder(Map<Integer, List<SaFunction>> functionByOrder) {

        if (CollectionUtil.isNotEmpty(functionByOrder)) {


            for (Map.Entry<Integer, List<SaFunction>> entry : functionByOrder.entrySet()) {
                if (this.checkFunctionByOrder.containsKey(entry.getKey())) {
                    throw new BeanCreationException("checkFunction order键已经存在");
                }
                this.checkFunctionByOrder.put(entry.getKey(), entry.getValue());
            }


        }
    }

    public List<String> getMatchArgs() {
        return matchArgs;
    }


    public List<String> getNotMatchAras() {
        return notMatchAras;
    }


    public Map<Integer, List<SaFunction>> getCheckFunction() {

        return this.checkFunctionByOrder;
    }


}
