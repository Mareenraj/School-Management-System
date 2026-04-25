package com.esoft.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {
    // Uses spring.task.execution.pool.* properties from application.properties
    // Spring Boot auto-configures the ThreadPoolTaskExecutor from those properties
}
