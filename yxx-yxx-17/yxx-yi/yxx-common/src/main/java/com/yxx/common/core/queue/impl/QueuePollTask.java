package com.yxx.common.core.queue.impl;

import com.yxx.common.core.queue.Job;
import com.yxx.common.core.queue.Queue;
import com.yxx.common.core.queue.QueueDispatcher;
import com.yxx.common.core.queue.QueueListener;
import com.yxx.common.core.queue.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 队列轮询任务
 *
 * @author yxx
 */
public class QueuePollTask implements Task {

    private static final Logger log = LoggerFactory.getLogger(QueuePollTask.class);

    private boolean active;

    private final QueueDispatcher dispatcher;

    private final ThreadPoolTaskExecutor executor;

    private String queueName;

    private QueueListener<?> listener;

    public QueuePollTask(QueueDispatcher dispatcher, ThreadPoolTaskExecutor executor) {
        this.active = true;
        this.dispatcher = dispatcher;
        this.executor = executor;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public void cancel() {
        this.active = false;
    }

    @Override
    public boolean isLongLived() {
        return true;
    }

    @Override
    public void run() {
        do {
            try {
                Queue<?> queue = this.dispatcher.queue(this.queueName);
                // 如果队列有数据并且任务处理器空闲，则从队列中取出数据交给任务处理器处理
                while (queue.size() > 0 && this.executor.getActiveCount() < this.executor.getCorePoolSize()) {
                    // 从队列中取出消息
                    Job<?> job = queue.pop();
                    // 提交给线程池处理
                    this.executor.execute(HandleJobTask.create(listener, (Job) job));
                }
                // 短暂休眠，避免CPU空转
                Thread.sleep(700);
            } catch (InterruptedException e) {
                this.cancel();
                Thread.currentThread().interrupt();
            } catch (Throwable e) {
                log.error("队列监听期间发生异常", e);
            }
        } while (this.isActive());
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public QueueDispatcher getDispatcher() {
        return dispatcher;
    }

    public ThreadPoolTaskExecutor getExecutor() {
        return executor;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public QueueListener<?> getListener() {
        return listener;
    }

    public void setListener(QueueListener<?> listener) {
        this.listener = listener;
    }
}
