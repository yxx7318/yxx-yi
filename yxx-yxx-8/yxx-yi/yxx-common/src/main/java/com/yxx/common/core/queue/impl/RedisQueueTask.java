package com.yxx.common.core.queue.impl;

import com.yxx.common.core.queue.Task;
import lombok.Getter;

/**
 * Redis队列任务
 *
 * @author yxx
 */
@Getter
public class RedisQueueTask implements Task {

    private final Task task;

    public RedisQueueTask(Task task) {
        this.task = task;
    }

    @Override
    public boolean isActive() {
        return this.task.isActive();
    }

    @Override
    public void cancel() {
        this.task.cancel();
    }

    @Override
    public boolean isLongLived() {
        return task.isLongLived();
    }

    @Override
    public void run() {
        task.run();
    }

}
