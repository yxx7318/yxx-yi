package com.yxx.common.yxx.redis;

import com.yxx.common.utils.uuid.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Component
public class RedisLockSimple {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String KEY_PREFIX = "lock:";

    private static final String ID_PREFIX = UUID.randomUUID().toString(true) + "-";

    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;

    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("unlock.lua"));
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    /**
     * 尝试获取锁
     *
     * @param name    锁名称
     * @param timeout 锁持有的超时时间，单位毫秒，过期后自动释放
     * @return true代表获取锁成功; false代表获取锁失败
     */
    public boolean tryLock(String name, long timeout) {
//        String threadId = String.valueOf(Thread.currentThread().getId());
        // 获取线程标示，id前缀是防止多个JVM递增线程ID导致的重复
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        // 获取锁
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(KEY_PREFIX + name, threadId, timeout, TimeUnit.MILLISECONDS);
        return Boolean.TRUE.equals(success);
    }

    /**
     * 释放锁
     */
    public void unlock(String name) {
//        stringRedisTemplate.delete(KEY_PREFIX + name);
        // 调用lua脚本
        stringRedisTemplate.execute(UNLOCK_SCRIPT,
                Collections.singletonList(KEY_PREFIX + name),
                ID_PREFIX + Thread.currentThread().getId());
    }
}
