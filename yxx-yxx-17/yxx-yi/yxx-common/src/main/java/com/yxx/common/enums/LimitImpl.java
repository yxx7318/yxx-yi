package com.yxx.common.enums;

/**
 * 限流实现
 */

public enum LimitImpl
{
    /**
     * 计数器限流算法
     */
    FIXED_WINDOW("fixedWindowRateLimiter"),

    /**
     * 令牌桶算法
     */
    TOKEN_BUCKET("tokenBucketRateLimiter")
    ;

    private final String beanName;

    LimitImpl(String beanName)
    {
        this.beanName = beanName;
    }

    public String getBeanName()
    {
        return beanName;
    }
}
