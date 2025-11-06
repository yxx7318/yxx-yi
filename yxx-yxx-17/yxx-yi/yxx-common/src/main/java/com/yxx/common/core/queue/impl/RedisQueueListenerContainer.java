package com.yxx.common.core.queue.impl;

import com.yxx.common.core.queue.QueueListener;
import com.yxx.common.core.queue.QueueListenerContainer;
import com.yxx.common.core.queue.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Redis队列监听容器
 *
 * @author yxx
 */
public class RedisQueueListenerContainer implements QueueListenerContainer, SmartLifecycle {

    private static final Logger log = LoggerFactory.getLogger(RedisQueueListenerContainer.class);

    private volatile boolean running;

    private final Object lifecycleMonitor;

    private final Collection<RedisQueueTask> subscriptions;

    private final RedisQueueDispatcher dispatcher;

    private final ThreadPoolTaskExecutor taskExecutor;

    public RedisQueueListenerContainer(RedisQueueDispatcher dispatcher, ThreadPoolTaskExecutor taskExecutor) {
        this.lifecycleMonitor = new Object();
        this.running = false;
        this.subscriptions = new ArrayList<>();
        this.dispatcher = dispatcher;
        this.taskExecutor = taskExecutor;
    }

    @Override
    public Task register(String queueName, QueueListener<?> listener) {
        RedisQueueTask subscription = new RedisQueueTask(this.createQueuePollTask(queueName, listener));
        synchronized (this.lifecycleMonitor) {
            this.subscriptions.add(subscription);
            if (this.running) {
                this.taskExecutor.execute(subscription.getTask());
            }
            return subscription;
        }
    }

    /**
     * 创建队列轮询任务(和监听器公用一个线程池)
     */
    protected Task createQueuePollTask(String queueName, QueueListener<?> listener) {
        // 注册监听器(监听推入队列的消息)
        QueuePollTask task = new QueuePollTask(this.dispatcher, this.taskExecutor);
        task.setQueueName(queueName);
        task.setListener(listener);
        return task;
    }

    @Override
    public void start() {
        synchronized (this.lifecycleMonitor) {
            if (!this.running) {
                log.debug("{} starting...", this.getClass().getSimpleName());
                this.subscriptions.stream()
                        .filter(Task::isActive)
                        .map(RedisQueueTask::getTask)
                        .forEach(this.taskExecutor::execute);
                this.running = true;
                log.debug("{} started", this.getClass().getSimpleName());
            }
        }
    }

    @Override
    public void stop() {
        synchronized (this.lifecycleMonitor) {
            log.debug("{} stopping...", this.getClass().getSimpleName());
            if (this.running) {
                this.subscriptions.forEach(Task::cancel);
                this.running = false;
            }
            log.debug("{} stopped", this.getClass().getSimpleName());
        }
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }
}
