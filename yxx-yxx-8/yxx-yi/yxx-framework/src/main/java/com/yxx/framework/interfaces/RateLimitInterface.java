package com.yxx.framework.interfaces;

import com.yxx.common.annotation.RateLimiter;
import com.yxx.common.enums.LimitType;
import com.yxx.common.exception.ServiceException;
import com.yxx.common.utils.ip.IpUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

public interface RateLimitInterface {

    /**
     * 限流接口，触发限流则抛出异常
     *
     * @param point       切入点
     * @param rateLimiter 注解
     * @throws ServiceException 限流异常
     */
    void rateLimiter(JoinPoint point, RateLimiter rateLimiter) throws ServiceException;

    /**
     * 生成限流key
     *
     * @param point       切入点
     * @param rateLimiter 注解
     * @return 限流key
     */
    default String getCombineKey(JoinPoint point, RateLimiter rateLimiter)
    {
        StringBuilder stringBuffer = new StringBuilder(rateLimiter.key());
        if (rateLimiter.limitType() == LimitType.IP)
        {
            stringBuffer.append(IpUtils.getIpAddr()).append("-");
        }
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();
        stringBuffer.append(targetClass.getName()).append("-").append(method.getName());
        return stringBuffer.toString();
    }
}
