package com.tuling.common.satoken.manager;



public class SaTokenHolderManager {

    private static final ThreadLocal<String> threadContext = new ThreadLocal<>();


    public static String getToken() {
        return threadContext.get();
    }

    public static void setToken(String val) {
        threadContext.set(val);

    }

    public static void removeToken() {
        threadContext.remove();

    }

}
