package com.yxx.framework.interfaces;

import com.yxx.common.annotation.RateLimiter;
import com.yxx.common.enums.LimitType;
import com.yxx.common.exception.ServiceException;
import com.yxx.common.utils.ip.IpUtils;
import com.yxx.common.utils.spring.SpringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Method;

import static com.yxx.common.constant.Constants.LUA_REDIS_TEMPLATE;

/**
 * 限流接口
 */
public interface RateLimitInterface
{

    /**
     * 限流接口，触发限流则抛出异常
     *
     * @param point       切入点
     * @param rateLimiter 注解
     * @throws ServiceException 限流异常
     */
    void rateLimiter(JoinPoint point, RateLimiter rateLimiter) throws ServiceException;

    /**
     * 获取标准的RedisTemplate操作对象，防止序列化参数问题
     */
    default RedisTemplate<String, String> getRedisTemplate()
    {
        return SpringUtils.getBean(LUA_REDIS_TEMPLATE);
    }

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

    /**
     * 生成参数列表
     *
     * @param objects 任意参数输入
     * @return 参数列表数组
     */
    default Object[] getArgs(Object... objects)
    {
        if (objects == null || objects.length == 0)
        {
            return new String[0];
        }
        Object[] strObject = new String[objects.length];
        for (int i = 0; i < objects.length; i++)
        {
            strObject[i] = String.valueOf(objects[i]);
        }
        return strObject;
    }
}
