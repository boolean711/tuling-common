package com.tuling.common.web.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 3;

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(corePoolSize); // 可以根据需要设置最大线程数
        executor.setQueueCapacity(100); // 根据实际情况设置队列容量
        executor.setThreadNamePrefix("ThreadPool-");
        executor.initialize();
        return executor;
    }
}