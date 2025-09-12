package com.yxx.common.core.queue;

import org.springframework.scheduling.SchedulingAwareRunnable;

/**
 * 任务接口
 *
 * @author yxx
 */
public interface Task extends SchedulingAwareRunnable {

    /**
     * 是否激活
     *
     * @return 激活返回 true，否则返回 false
     */
    boolean isActive();

    /**
     * 取消任务
     */
    void cancel();
}
