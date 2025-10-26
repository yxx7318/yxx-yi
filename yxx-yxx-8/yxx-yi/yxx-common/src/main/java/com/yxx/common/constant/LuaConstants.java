package com.yxx.common.constant;

import io.jsonwebtoken.Claims;

import java.util.Locale;

/**
 * 通用常量信息
 */
public class LuaConstants
{
    /**
     * 计数器限流脚本
     */
    public final static String FIXED_WINDOW_LIMIT_SCRIPT_TEXT = "local key = KEYS[1]\n" +
                "local count = tonumber(ARGV[1])\n" +
                "local time = tonumber(ARGV[2])\n" +
                "local current = redis.call('get', key);\n" +
                "if current and tonumber(current) > count then\n" +
                "    return tonumber(current);\n" +
                "end\n" +
                "current = redis.call('incr', key)\n" +
                "if tonumber(current) == 1 then\n" +
                "    redis.call('expire', key, time)\n" +
                "end\n" +
                "return tonumber(current);";

    /**
     * 令牌桶限流脚本
     */
    public final static String TOKEN_BUCKET_LIMIT_SCRIPT_TEXT = "local tokens_key = KEYS[1]\n" +
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
}
