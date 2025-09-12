package com.yxx.common.core.queue;

/**
 * 监听器容器接口
 *
 * @author yxx
 */
public interface QueueListenerContainer {

    /**
     * 注册监听器
     *
     * @param queue    队列名称
     * @param listener 监听器
     * @return 订阅信息
     */
    Task register(String queue, QueueListener<?> listener);
}
