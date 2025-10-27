package com.yxx.framework.config;

import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Callable;

public class RequestContextDecorator implements TaskDecorator
{

    @Override
    public Runnable decorate(Runnable runnable)
    {
        return decorateRunnable(runnable);
    }

    /**
     * 传递请求上下文到线程
     */
    public static Runnable decorateRunnable(Runnable runnable)
    {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return () ->
        {
            try
            {
                RequestContextHolder.setRequestAttributes(requestAttributes);
                SecurityContextHolder.setContext(securityContext);
                runnable.run();
            }
            finally
            {
                RequestContextHolder.resetRequestAttributes();
                SecurityContextHolder.clearContext();
            }
        };
    }

    /**
     * 传递请求上下文到线程
     */
    public static <T> Callable<T> decorateCallable(final Callable<T> callable)
    {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return () ->
        {
            try
            {
                RequestContextHolder.setRequestAttributes(requestAttributes);
                SecurityContextHolder.setContext(securityContext);
                return callable.call();
            }
            finally
            {
                RequestContextHolder.resetRequestAttributes();
                SecurityContextHolder.clearContext();
            }
        };
    }
}
