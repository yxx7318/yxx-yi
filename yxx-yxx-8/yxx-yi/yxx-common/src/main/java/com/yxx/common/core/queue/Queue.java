package com.yxx.common.core.queue;

import java.time.Duration;

/**
 * 队列接口
 *
 * @author yxx
 */
public interface Queue<T> {

    /**
     * 队列长度
     *
     * @return long
     */
    long size();

    /**
     * 推入队列
     *
     * @param job 任务
     * @return 成功返回 true, 否则返回 false
     */
    boolean push(Job<T> job);

    /**
     * 延迟指定时间后推入队列, laterPush 的别名
     *
     * @param job   任务
     * @param delay 延迟时间
     * @return 成功返回 true, 否则返回 false
     */
    default boolean later(Job<T> job, Duration delay) {
        return this.laterPush(job, delay);
    }

    /**
     * 延迟指定时间后推入队列
     *
     * @param job   任务
     * @param delay 延迟时间
     * @return 成功返回 true, 否则返回 false
     */
    boolean laterPush(Job<T> job, Duration delay);

    /**
     * 从队列中取出任务
     *
     * @return 任务
     */
    Job<T> pop();

    /**
     * 获取队列名称
     *
     * @return 队列名称
     */
    String getQueueName();
}
