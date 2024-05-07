package com.tuling.common.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BeanListUtils {
    public static <S, T> List<T> copyList(List<S> sourceList, Class<T> targetClass)  {
        if (sourceList == null || sourceList.isEmpty()) {
            return new ArrayList<>();
        }
        return sourceList.stream().map(source -> {
            T target = null;
            try {
                target = targetClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create a new instance of " + targetClass, e);
            }
            BeanUtils.copyProperties(source, target);
            return target;
        }).collect(Collectors.toList());
    }
}