package com.yxx.common.core.queue.impl;

import cn.hutool.core.util.IdUtil;
import com.yxx.common.core.queue.Job;

import java.util.Objects;

/**
 * 分发的任务实体
 *
 * @author yxx
 */
public class DispatchJob<T> implements Job<T> {

    private String jobId;

    private T payload;

    private String queue;

    public static <T> DispatchJob<T> create(String queue, T payload) {
        DispatchJob<T> job = new DispatchJob<>();
        job.setJobId(IdUtil.objectId());
        job.setQueue(queue);
        job.setPayload(payload);
        return job;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DispatchJob<?> that = (DispatchJob<?>) o;
        return Objects.equals(jobId, that.jobId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId);
    }

    @Override
    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    @Override
    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    @Override
    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }
}
