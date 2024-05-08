package com.tuling.common.web.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import com.tuling.common.satoken.handler.ISaTokenInterceptorHandler;
import com.tuling.common.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {


    @Autowired(required = false)
    private  ISaTokenInterceptorHandler saTokenInterceptorHandler;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToDateConverter());
    }

    private static class StringToDateConverter implements Converter<String, Date> {

        @Override
        public Date convert(String source) {
            SimpleDateFormat sdf;
            if (source.matches("\\d{4}-\\d{2}-\\d{2}")) {
                // 如果是"yyyy-MM-dd"格式的字符串，时分秒默认都是0
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            } else {
                // 如果是"yyyy-MM-dd HH:mm:ss"格式的字符串
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
            try {
                return sdf.parse(source);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date format: " + source);
            }
        }
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许跨域访问的路径
                .allowedOrigins("*") // 允许访问的源
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许使用的请求方法
                .allowedHeaders("*") // 允许的头信息
                .maxAge(3600); // 预检请求的缓存时间（秒）
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，定义详细认证规则
        if (saTokenInterceptorHandler!=null){
            registry.addInterceptor(new SaInterceptor(saTokenInterceptorHandler)).addPathPatterns("/**");
        }

    }


}
