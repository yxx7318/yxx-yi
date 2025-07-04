package com.yxx.framework.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import jakarta.annotation.Resource;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class RateLimiter {
    private static final Long SUCCESS_FLAG = 1L;
    private static final String SCRIPT = "local tokens_key = KEYS[1]\n" +
            "local timestamp_key = KEYS[2]\n" +
            "local rate = tonumber(ARGV[1])\n" +
            "local capacity = tonumber(ARGV[2])\n" +
            "local now = tonumber(ARGV[3])\n" +
            "local requested = tonumber(ARGV[4])\n" +
            "local fill_time = capacity/rate\n" +
            "local ttl = math.floor(fill_time*2)\n" +
            "local last_tokens = tonumber(redis.call('get', tokens_key))\n" +
            "if last_tokens == nil then\n" +
            "  last_tokens = capacity\n" +
            "end\n" +
            "local last_refreshed = tonumber(redis.call('get', timestamp_key))\n" +
            "if last_refreshed == nil then\n" +
            "  last_refreshed = 0\n" +
            "end\n" +
            "local diff_time = math.max(0, now-last_refreshed)\n" +
            "local filled_tokens = math.min(capacity, last_tokens+(diff_time*rate))\n" +
            "local allowed = filled_tokens >= requested\n" +
            "local new_tokens = filled_tokens\n" +
            "local allowed_num = 0\n" +
            "if allowed then\n" +
            "  new_tokens = filled_tokens - requested\n" +
            "  allowed_num = 1\n" +
            "end\n" +
            "if ttl > 0 then\n" +
            "  redis.call('setex', tokens_key, ttl, new_tokens)\n" +
            "  redis.call('setex', timestamp_key, ttl, now)\n" +
            "end\n" +
            "return allowed_num\n";
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 判断是否允许访问
     *
     * @param id       这次获取令牌桶的id
     * @param rate     每秒填充速率
     * @param capacity 令牌桶最大容量
     * @param tokens   每次访问消耗几个令牌
     * @return true 允许访问 false 不允许访问
     */
    public boolean isAllowed(String id, int rate, int capacity, int tokens) {
        RedisScript<Long> redisScript = new DefaultRedisScript<>(SCRIPT, Long.class);

        Object result = redisTemplate.execute(redisScript,
                getKey(id), String.valueOf(rate), String.valueOf(capacity),
                String.valueOf(Instant.now().getEpochSecond()), String.valueOf(tokens));
        return SUCCESS_FLAG.equals(result);
    }

    private List<String> getKey(String id) {
        String prefix = "limiter:" + id;
        String tokenKey = prefix + ":tokens";
        String timestampKey = prefix + ":timestamp";
        return Arrays.asList(tokenKey, timestampKey);
    }
}