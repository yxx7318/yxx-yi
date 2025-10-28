package com.yxx.framework.interfaces.impl;

import com.yxx.common.annotation.RateLimiter;
import com.yxx.common.constant.LuaConstants;
import com.yxx.common.exception.ServiceException;
import com.yxx.common.utils.TimestampUtils;
import com.yxx.framework.interfaces.RateLimitInterface;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


/**
 * 令牌桶算法
 * - 桶中存储一定数量的令牌
 * - 系统以固定速率向桶中添加令牌，直到达到桶的最大容量
 * - 每次请求需要消耗一个或多个令牌才能被处理
 * - 如果桶中没有足够的令牌，则拒绝请求或让请求等待
 * <p>
 * <a href="https://blog.csdn.net/weixin_42645678/article/details/124428393">redis实现令牌桶的正确姿势</a>
 *
 * @author : yxx
 */
@Component
public class TokenBucketRateLimiter implements RateLimitInterface
{
    private static final Logger log = LoggerFactory.getLogger(TokenBucketRateLimiter.class);

    private final RedisTemplate<String, String> redisTemplate = getRedisTemplate();

    private final RedisScript<Long> limitScript = limitScript();

    public RedisScript<Long> limitScript()
    {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(LuaConstants.TOKEN_BUCKET_LIMIT_SCRIPT_TEXT);
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    /**
     * 成功标识
     */
    private static final Long SUCCESS_FLAG = 1L;

    /**
     * 判断是否允许访问
     *
     * @param keys      缓存键
     * @param rate      每秒填充速率
     * @param capacity  令牌桶最大容量
     * @param requested 访问消耗令牌数
     * @return true 允许访问 false 不允许访问
     */
    public boolean isAllowed(List<String> keys, int rate, int capacity, int requested)
    {
        Object result = redisTemplate.execute(limitScript, keys, this.getArgs(rate, capacity, TimestampUtils.secondTimestamp(), requested));
        return SUCCESS_FLAG.equals(result);
    }

    @Override
    public void rateLimiter(JoinPoint point, RateLimiter rateLimiter) throws ServiceException
    {
        String combineKey = this.getCombineKey(point, rateLimiter);
        String tokensKey = combineKey + "-tokens";
        String timestamp = combineKey + "-timestamp";
        if (!isAllowed(Arrays.asList(tokensKey, timestamp), rateLimiter.rate(), rateLimiter.capacity(), rateLimiter.requested()))
        {
            throw new ServiceException("访问过于频繁，请稍候再试");
        }
        log.info("缓存key'{}', 填充速率'{}', 最大容量'{}', 消耗令牌'{}'", combineKey, rateLimiter.rate(), rateLimiter.capacity(), rateLimiter.requested());
    }
}