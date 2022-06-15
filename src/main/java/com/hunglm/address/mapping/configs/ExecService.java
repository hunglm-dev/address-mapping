package com.hunglm.address.mapping.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ExecService {
    @Bean
    TaskExecutor cityExecutor() {
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        threadPoolExecutor.setCorePoolSize(5);
        threadPoolExecutor.setMaxPoolSize(5);
        threadPoolExecutor.setQueueCapacity(10000);
        threadPoolExecutor.setThreadNamePrefix("task-exec-");
        return threadPoolExecutor;
    }

}
