package com.tuling.auth.config;


import com.aliyun.captcha20230305.Client;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliyunConfig {

    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.captcha.endpoint}")
    private String endpoint;
    private final int TIMEOUT = 5000;


    @Bean
    public Client aliyunCaptchaClient() throws Exception {
        Config config = new Config();
        config.accessKeyId = accessKeyId;
        config.accessKeySecret = accessKeySecret;
        config.endpoint = endpoint;
        config.connectTimeout = TIMEOUT;
        config.readTimeout = TIMEOUT;
        return new Client(config);
    }
}

