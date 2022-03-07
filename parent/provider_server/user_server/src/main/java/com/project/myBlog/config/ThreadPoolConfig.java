package com.project.myBlog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
// enable asynchronous
public class ThreadPoolConfig {

    @Bean("taskExecutor")
    public Executor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // set core size
        executor.setCorePoolSize(5);
        // set thread size
        executor.setMaxPoolSize(20);
        // set queue size
        executor.setQueueCapacity(Integer.MAX_VALUE);
        // set thread alive time
        executor.setKeepAliveSeconds(60);

        executor.setThreadNamePrefix("myBlogProject");

        // don't close until all task are finished
        executor.setWaitForTasksToCompleteOnShutdown(true);

        executor.initialize();
        return executor;
    }
}
