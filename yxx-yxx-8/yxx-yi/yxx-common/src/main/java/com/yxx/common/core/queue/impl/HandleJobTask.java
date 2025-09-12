package com.yxx.common.core.queue.impl;

import com.yxx.common.core.queue.Job;
import com.yxx.common.core.queue.QueueListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 接收任务后的Handle处理器
 *
 * @author yxx
 */
@Data
@Slf4j
public class HandleJobTask<T> implements Runnable {

    private QueueListener<T> listener;

    private Job<T> job;

    public static <T> HandleJobTask<T> create(QueueListener<T> listener, Job<T> job) {
        HandleJobTask<T> handleJobTask = new HandleJobTask<>();
        handleJobTask.setListener(listener);
        handleJobTask.setJob(job);
        return handleJobTask;
    }

    @Override
    public void run() {
        try {
            if (job == null) {
                return;
            }
            this.listener.onMessage(job.getPayload());
        } catch (Throwable e) {
            log.error("[Queue] Raise Error When Handle Job [{}] From [{}]", job.getJobId(), job.getQueue(), e);
            this.listener.onError(job.getPayload(), e);
        }
    }
}
