package com.tuling.common.satoken.config;

import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import cn.hutool.core.util.StrUtil;
import com.tuling.common.satoken.manager.SaTokenHolderManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Configuration
public class SaTokenConfig {


    @Value("${sa-token.token-name}")
    private String tokenName;
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }
    @Bean
    public SaServletFilter saServletFilter() {
       return new TlSaServletFilter().addInclude("/**");
    }


    private  class TlSaServletFilter extends SaServletFilter {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

            try {
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                // Capture the token from request header
                String token = httpRequest.getHeader(tokenName);
                SaTokenHolderManager.setToken(token);
                super.doFilter(request, response, chain);
            }finally {
                SaTokenHolderManager.removeToken();
            }

        }


    }
}
