package com.yxx.framework.config;

import com.yxx.common.utils.Threads;
import com.yxx.framework.manager.ThreadPoolExecutorMDCWrapper;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 **/
@Configuration
@EnableAsync // 启用异步支持
public class ThreadPoolConfig
{
    // 核心线程池大小
    @Value("${server.tomcat.threads.min-spare}")
    private int corePoolSize = 50;

    // 最大可创建的线程数
    @Value("${server.tomcat.threads.max}")
    private int maxPoolSize = 200;

    // 队列最大长度
    @Value("${server.tomcat.accept-count}")
    private int queueCapacity = 1000;

    // 线程池维护线程所允许的空闲时间
    private int keepAliveSeconds = 300;

    /**
     * ThreadPoolTaskExecutor是Spring框架提供的线程池，实现了TaskExecutor接口
     * Async注解使用此线程池
     */
    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor()
    {
        ThreadPoolExecutorMDCWrapper executor = new ThreadPoolExecutorMDCWrapper();
        executor.setMaxPoolSize(maxPoolSize);
        executor.setCorePoolSize(corePoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("thread-pool-task-");
        executor.initialize();
        return executor;
    }

    /**
     * ScheduledExecutorService是Java.util.concurrent包中的一个接口，扩展了ExecutorService接口
     * Scheduled注解使用此线程池，用于执行周期性或定时任务
     * AsyncManager类使用其作为异步操作线程池
     */
    @Bean(name = "scheduledExecutorService")
    public ScheduledExecutorService scheduledExecutorService()
    {
        return new ScheduledThreadPoolExecutor(corePoolSize,
                new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build(),
                new ThreadPoolExecutor.CallerRunsPolicy())
        {
            @Override
            protected void afterExecute(Runnable r, Throwable t)
            {
                super.afterExecute(r, t);
                Threads.printException(r, t);
            }
        };
    }
}
