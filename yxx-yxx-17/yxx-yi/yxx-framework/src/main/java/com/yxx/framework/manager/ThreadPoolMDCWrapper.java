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
public class ThreadPoolMDCWrapper extends ThreadPoolTaskExecutor {

    @Override
    public void execute(Runnable task)
    {
        super.execute(wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task)
    {
        return super.submit(wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public Future<?> submit(Runnable task)
    {
        return super.submit(wrap(task, MDC.getCopyOfContextMap()));
    }

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context)
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
                MDC.clear();
            }
        };
    }

    public static Runnable wrap(final Runnable runnable, final Map<String, String> context)
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
                MDC.clear();
            }
        };
    }

    public static <T> Callable<T> wrap(final Callable<T> callable)
    {
        final Map<String, String> context = MDC.getCopyOfContextMap();
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
                MDC.clear();
            }
        };
    }

    public static Runnable wrap(final Runnable runnable)
    {
        final Map<String, String> context = MDC.getCopyOfContextMap();
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
                MDC.clear();
            }
        };
    }
}