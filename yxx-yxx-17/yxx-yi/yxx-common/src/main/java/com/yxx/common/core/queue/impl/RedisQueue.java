package com.yxx.common.core.queue.impl;


import com.yxx.common.core.queue.Job;
import com.yxx.common.core.queue.Queue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Redis 队列
 *
 * @author yxx
 */
public class RedisQueue<T> implements Queue<T> {

    private final RedissonClient redissonClient;

    private final String key;

    private final RQueue<Job<T>> rQueue;

    private final RDelayedQueue<Job<T>> rDelayedQueue;

    public RedisQueue(RedissonClient redissonClient, String key) {
        this.redissonClient = redissonClient;
        this.key = key;
        // 从 Redisson 取出队列
        this.rQueue = this.redissonClient.getQueue(this.key, new JsonJacksonCodec());
        // 从 Redisson 取出延迟队列
        this.rDelayedQueue = this.redissonClient.getDelayedQueue(this.rQueue);
    }

    @Override
    public long size() {
        return this.queue().size();
    }

    @Override
    public boolean push(Job<T> job) {
        return this.queue().offer(job);
    }

    @Override
    public boolean laterPush(Job<T> job, Duration delay) {
        // 将任务（job）和延迟时间（delay）交给 rDelayedQueue
        this.delayQueue().offer(job, delay.toMillis(), TimeUnit.MILLISECONDS);
        return true;
    }

    @Override
    public Job<T> pop() {
        return this.queue().poll();
    }

    @Override
    public String getQueueName() {
        return this.key;
    }

    protected RQueue<Job<T>> queue() {
        return this.rQueue;
    }

    protected RDelayedQueue<Job<T>> delayQueue() {
        return this.rDelayedQueue;
    }
}
