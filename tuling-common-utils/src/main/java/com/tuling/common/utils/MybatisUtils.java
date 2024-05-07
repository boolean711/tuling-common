package com.tuling.common.utils;

public class MybatisUtils {

    // 将驼峰命名转换为下划线命名
    public static String camelToUnderscore(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }
        StringBuilder builder = new StringBuilder(camelCase);
        int i = 1; // Start at 1 to skip the initial character
        while (i < builder.length()) {
            char c = builder.charAt(i);
            if (Character.isUpperCase(c)) {
                builder.replace(i, i + 1, "_" + Character.toLowerCase(c));
                i += 2;
            } else {
                i++;
            }
        }
        return builder.toString();
    }
}
