package com.yxx.framework.manager;

import com.yxx.common.utils.MDCUtils;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 线程池MDC上下文配置
 */
public class ThreadPoolExecutorMDCWrapper extends ThreadPoolTaskExecutor {

    @Override
    public void execute(Runnable task)
    {
        super.execute(wrap(task, MDCUtils.getContext()));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task)
    {
        return super.submit(wrap(task, MDCUtils.getContext()));
    }

    @Override
    public Future<?> submit(Runnable task)
    {
        return super.submit(wrap(task, MDCUtils.getContext()));
    }

    private <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context)
    {
        return () ->
        {
            MDCUtils.setContextIfExist(context);
            MDCUtils.setTraceIdIfAbsent();
            try
            {
                return callable.call();
            }
            finally
            {
                MDCUtils.clean();
            }
        };
    }

    private Runnable wrap(final Runnable runnable, final Map<String, String> context)
    {
        return () ->
        {
            MDCUtils.setContextIfExist(context);
            MDCUtils.setTraceIdIfAbsent();
            try
            {
                runnable.run();
            }
            finally
            {
                MDCUtils.clean();
            }
        };
    }
}