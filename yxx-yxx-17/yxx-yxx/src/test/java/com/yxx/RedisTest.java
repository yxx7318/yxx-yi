package com.yxx;

import com.yxx.common.core.redis.RedisIdWorker;
import com.yxx.common.core.redis.RedisLockSimple;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisIdWorker redisIdWorker;

    @Test
    public void testRedisId() throws InterruptedException {
        // 创建一个固定大小为500的线程池
        ExecutorService es = Executors.newFixedThreadPool(500);

        // 初始化CountDownLatch，计数器初始值设为300
        CountDownLatch latch = new CountDownLatch(300);

        // 记录开始时间
        long begin = System.currentTimeMillis();

        // 循环提交300个任务到线程池中执行
        for (int i = 0; i < 300; i++) {
            es.submit(() -> {
                // 每个任务内部循环100次生成ID并打印
                for (int j = 0; j < 100; j++) {
                    long id = redisIdWorker.nextId("order");
                    System.out.println("id = " + id);
                }
                // 任务完成后，减少CountDownLatch的计数
                latch.countDown();
            });
        }

        // 等待所有任务完成（即CountDownLatch计数归零）
        latch.await();

        // 记录结束时间，并计算和打印总耗时
        long end = System.currentTimeMillis();
        System.out.println("time = " + (end - begin));
    }

    @Autowired
    private RedisLockSimple redisLockSimple;

    @Test
    public void testLock() throws InterruptedException {
        String name = "lockTest";
        boolean lock = redisLockSimple.tryLock(name, 150000);
        if (lock) {
            System.out.println("------获取锁成功------");
            Thread.sleep(3000);
            redisLockSimple.unlock(name);
            System.out.println("------释放锁成功------");
        } else {
            System.out.println("------获取锁失败------");
        }
    }

    @Resource
    private RedissonClient redissonClient;

    @Test
    public void testRedisson() throws InterruptedException {
        // 获取锁（可重入），指定锁的名称
        RLock lock = redissonClient.getLock("anyLock");
        // 尝试获取锁，参数分别是：获取锁的最大等待时间（期间会重试），锁自动释放时间，时间单位
        boolean isLock = lock.tryLock(1, 10, TimeUnit.SECONDS);
        // 判断释放获取成功
        if (isLock) {
            try {
                System.out.println("------获取锁成功------");
            } finally {
                // 释放锁
                lock.unlock();
            }
        }
    }
}
