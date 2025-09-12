package com.yxx.common.core.queue.impl;

import com.yxx.common.core.queue.Job;
import com.yxx.common.core.queue.Queue;
import com.yxx.common.core.queue.QueueDispatcher;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis 队列分发器
 *
 * @author yxx
 */
public class RedisQueueDispatcher implements QueueDispatcher {

    private final String prefix;

    private final RedissonClient redissonClient;

    private final Map<String, Queue<?>> queues;

    public RedisQueueDispatcher(RedissonClient redissonClient, String prefix) {
        this.redissonClient = redissonClient;
        this.queues = new HashMap<>();
        this.prefix = prefix;
    }

    @Override
    public <T> boolean dispatch(String queue, T payload) {
        Job job = DispatchJob.create(warpKey(queue), payload);
        return this.queue(job.getQueue()).push(job);
    }

    @Override
    public <T> boolean dispatch(String queue, T payload, Duration delay) {
        Job job = DispatchJob.create(warpKey(queue), payload);
        this.queue(job.getQueue()).later(job, delay);
        return true;
    }

    public String warpKey(String key) {
        if (this.prefix != null) {
            return this.prefix.concat(":").concat(key);
        }
        return key;
    }

    @Override
    public Queue<?> queue(String key) {
        if (this.queues.containsKey(key)) {
            return this.queues.get(key);
        }
        RedisQueue<?> queue = new RedisQueue<>(redissonClient, key);
        this.queues.put(key, queue);
        return queue;
    }
}
