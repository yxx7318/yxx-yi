package com.yxx.framework.aspectj;

import com.yxx.common.utils.ServletUtils;
import com.yxx.common.utils.spring.SpringUtils;
import com.yxx.framework.interfaces.RateLimitInterface;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.yxx.common.annotation.RateLimiter;
import com.yxx.common.exception.ServiceException;
import com.yxx.common.utils.ip.IpUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 限流处理
 */
@Aspect
@Component
public class RateLimiterAspect
{
    private static final Logger log = LoggerFactory.getLogger(RateLimiterAspect.class);

    @Autowired
    private Map<String, RateLimitInterface> limitInterfaces = new HashMap<>();

    @Before("@annotation(rateLimiter) || @within(rateLimiter)")
    public void doBefore(JoinPoint point, RateLimiter rateLimiter) throws Throwable
    {
        String uri = ServletUtils.getRequest().getRequestURI();
        log.info("速率限制请求'{}'", uri);
        try
        {
            limitInterfaces.get(rateLimiter.limitImpl().getBeanName()).rateLimiter(point, rateLimiter);
        }
        catch (ServiceException e)
        {
            log.error("触发限流策略, 请求uri'{}', 请求ip'{}'", uri, IpUtils.getIpAddr());
            throw e;
        }
        catch (Exception e)
        {
            throw new RuntimeException("服务器限流异常，请稍候再试");
        }
    }

}
