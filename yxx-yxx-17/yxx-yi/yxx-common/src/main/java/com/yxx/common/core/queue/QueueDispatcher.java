package com.yxx.common.core.queue;

import java.time.Duration;

/**
 * 队列分发器接口
 *
 * @author yxx
 */
public interface QueueDispatcher {

    /**
     * 推入队列
     *
     * @param queue   队列名称
     * @param payload 数据
     * @return 成功返回 true, 否则返回 false
     */
    <T> boolean dispatch(String queue, T payload);

    /**
     * 延迟指定时间推入队列
     *
     * @param queue   队列名称
     * @param payload 数据
     * @param delay   延迟时间
     * @return 成功返回 true, 否则返回 false
     */
    <T> boolean dispatch(String queue, T payload, Duration delay);

    /**
     * 队列
     *
     * @param key 队列标识
     * @return 队列
     */
    Queue<?> queue(String key);
}
