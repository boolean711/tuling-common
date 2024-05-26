package com.tuling.common.web.config;

import cn.dev33.satoken.fun.SaFunction;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.router.SaRouterStaff;
import com.tuling.common.satoken.processor.SaTokenInterceptorArgsHolder;
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
import java.util.List;
import java.util.Map;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {


    @Autowired
    private SaTokenInterceptorArgsHolder handlerHolder;

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

        registry.addInterceptor(new SaInterceptor(r -> {
            SaRouterStaff saRouterStaff = SaRouter.match(handlerHolder.getMatchArgs()).notMatch(handlerHolder.getNotMatchAras());

            Map<Integer, List<SaFunction>> checkFunction = handlerHolder.getCheckFunction();

            for (Map.Entry<Integer, List<SaFunction>> entry : checkFunction.entrySet()) {
                List<SaFunction> saFunctionList = entry.getValue();
                for (SaFunction saFunction : saFunctionList) {
                    saRouterStaff.check(saFunction);
                }

            }
        })).addPathPatterns("/**");
        // 注册 Sa-Token 拦截器，打开注解式鉴权功能
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
    }


}
