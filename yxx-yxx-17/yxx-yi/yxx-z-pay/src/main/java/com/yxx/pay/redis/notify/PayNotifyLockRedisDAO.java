package com.yxx.pay.redis.notify;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

import static com.yxx.pay.redis.RedisKeyConstants.PAY_NOTIFY_LOCK;


/**
 * 支付通知的锁 Redis DAO
 *
 * @author yxx
 */
@Repository
@RequiredArgsConstructor
public class PayNotifyLockRedisDAO {

    private final RedissonClient redissonClient;

    public void lock(Long id, Long timeoutMillis, Runnable runnable) {
        String lockKey = formatKey(id);
        RLock lock = redissonClient.getLock(lockKey);
        try {
            lock.lock(timeoutMillis, TimeUnit.MILLISECONDS);
            // 执行逻辑
            runnable.run();
        } finally {
            lock.unlock();
        }
    }

    private static String formatKey(Long id) {
        return String.format(PAY_NOTIFY_LOCK, id);
    }

}
