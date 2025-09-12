package com.yxx.common.core.queue.impl;

import com.yxx.common.core.queue.Job;
import com.yxx.common.core.queue.Queue;
import com.yxx.common.core.queue.QueueDispatcher;
import com.yxx.common.core.queue.QueueListener;
import com.yxx.common.core.queue.Task;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 队列轮询任务
 *
 * @author yxx
 */
@Slf4j
@Setter
public class QueuePollTask implements Task {

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
                Thread.sleep(100);
            } catch (InterruptedException e) {
                this.cancel();
                Thread.currentThread().interrupt();
            } catch (Throwable e) {
                log.error("队列监听期间发生异常", e);
            }
        } while (this.isActive());
    }
}
