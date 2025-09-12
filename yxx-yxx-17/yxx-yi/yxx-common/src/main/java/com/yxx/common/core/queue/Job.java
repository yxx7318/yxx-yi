package com.yxx.common.core.queue;

/**
 * 任务实体接口
 *
 * @author yxx
 */
public interface Job<T> {

    /**
     * 工作ID
     *
     * @return String
     */
    String getJobId();

    /**
     * 工作内容
     *
     * @return T
     */
    T getPayload();

    /**
     * 归属队列名称
     *
     * @return String
     */
    String getQueue();
}
