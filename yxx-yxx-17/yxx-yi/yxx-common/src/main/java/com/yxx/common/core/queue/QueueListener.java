package com.yxx.common.core.queue;

import org.springframework.lang.Nullable;

/**
 * 消息监听器接口
 *
 * @author yxx
 */
public interface QueueListener<T> {

    /**
     * 监听的队列名称
     *
     * @return 队列名称
     */
    String queueName();

    /**
     * 有消息推入队列时触发
     *
     * @param msg 消息体
     */
    void onMessage(T msg);

    /**
     * 发生错误时触发
     *
     * @param msg 消息体
     * @param e   异常
     */
    default void onError(@Nullable T msg, Throwable e) {
    }
}
