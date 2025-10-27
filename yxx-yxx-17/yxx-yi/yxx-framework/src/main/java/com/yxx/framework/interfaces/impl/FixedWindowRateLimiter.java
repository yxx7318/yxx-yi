package com.yxx.framework.interfaces.impl;

import com.yxx.common.annotation.RateLimiter;
import com.yxx.common.constant.LuaConstants;
import com.yxx.common.exception.ServiceException;
import com.yxx.common.utils.StringUtils;
import com.yxx.framework.interfaces.RateLimitInterface;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;


/**
 * 计数器限流算法
 * - 在一个固定的时间窗口内，统计请求的数量
 * - 如果请求数量超过了设定的阈值，则拒绝后续的请求
 *
 * @author : yxx
 */
@Component
public class FixedWindowRateLimiter implements RateLimitInterface
{
    private static final Logger log = LoggerFactory.getLogger(TokenBucketRateLimiter.class);

    private final RedisTemplate<String, String> redisTemplate = getRedisTemplate();

    private final RedisScript<Long> limitScript = limitScript();

    public RedisScript<Long> limitScript()
    {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(LuaConstants.FIXED_WINDOW_LIMIT_SCRIPT_TEXT);
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    @Override
    public void rateLimiter(JoinPoint point, RateLimiter rateLimiter) throws ServiceException
    {
        int time = rateLimiter.time();
        int count = rateLimiter.count();

        String combineKey = this.getCombineKey(point, rateLimiter);
        List<String> keys = Collections.singletonList(combineKey);

        Long number = redisTemplate.execute(limitScript, keys, this.getArgs(count, time));
        if (StringUtils.isNull(number) || number.intValue() > count)
        {
            throw new ServiceException("访问过于频繁，请稍候再试");
        }
        log.info("缓存key'{}', 限流次数'{}', 使用次数'{}'", combineKey, count, number.intValue());
    }
}