package com.yxx.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.yxx.common.constant.CacheConstants;
import com.yxx.common.enums.LimitImpl;
import com.yxx.common.enums.LimitType;

/**
 * 限流注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter
{
    /**
     * 限流key
     */
    public String key() default CacheConstants.RATE_LIMIT_KEY;

    /**
     * 限流时间,单位秒(计数器)
     */
    public int time() default 60;

    /**
     * 限流次数(计数器)
     */
    public int count() default 100;

    /**
     * 限流类型
     */
    public LimitType limitType() default LimitType.DEFAULT;

    /**
     * 限流实现
     */
    public LimitImpl limitImpl() default LimitImpl.TOKEN_BUCKET;

    /**
     * 每秒填充速率(令牌桶)
     */
    public int rate() default 2;

    /**
     * 令牌桶最大容量
     */
    public int capacity() default 100;

    /**
     * 访问消耗令牌数
     */
    public int requested() default 1;
}
