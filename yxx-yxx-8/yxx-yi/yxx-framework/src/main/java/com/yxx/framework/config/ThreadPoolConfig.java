package com.yxx.framework.config;

import com.yxx.common.utils.ExceptionUtil;
import com.yxx.common.utils.Threads;
import com.yxx.common.utils.spring.SpringUtils;
import com.yxx.framework.manager.ThreadPoolExecutorMDCWrapper;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.*;

/**
 * 线程池配置
 **/
@Configuration
@EnableAsync // 启用异步支持
public class ThreadPoolConfig implements AsyncConfigurer
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
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("thread-pool-task-");
        // 传递线程上下文装饰器
        executor.setTaskDecorator(new RequestContextDecorator());
        executor.initialize();
        return executor;
    }

    /**
     * 指定Async注解使用的线程池
     */
    @Override
    public Executor getAsyncExecutor()
    {
        return SpringUtils.getBean("threadPoolTaskExecutor");
    }

    private static final Logger logger = LoggerFactory.getLogger("sys-async");

    /**
     * 异步线程池抛出异常时的处理逻辑
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (Throwable ex, Method method, Object... params) ->
        {
            // 异步方法异常处理
            logger.error("class#method#params : {}#{}#{}\nCaused: {}", method.getDeclaringClass().getName(), method.getName(), Arrays.toString(params), ExceptionUtil.getExceptionMessage(ex));
        };
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

    /**
     * ThreadPoolTaskExecutor是Spring框架提供的线程池，实现了TaskExecutor接口
     * Queue队列监听器使用此线程池
     */
    @Bean(name = "listenerExecutor")
    public ThreadPoolTaskExecutor listenerExecutor()
    {
        ThreadPoolExecutorMDCWrapper executor = new ThreadPoolExecutorMDCWrapper();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("queue-pool-task-");
        executor.initialize();
        return executor;
    }
}
