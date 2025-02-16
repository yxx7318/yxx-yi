package com.yxx.framework.manager;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.yxx.common.utils.Threads;
import com.yxx.common.utils.spring.SpringUtils;

/**
 * 异步任务管理器
 * 
 * @author ruoyi
 */
public class AsyncManager
{
    /**
     * 操作延迟10毫秒
     */
    private final int OPERATE_DELAY_TIME = 100;

    /**
     * 异步操作任务调度线程池
     */
    private final ScheduledExecutorService executor = SpringUtils.getBean("scheduledExecutorService");

    /**
     * 单例模式
     */
    private AsyncManager(){}

    private static final AsyncManager me = new AsyncManager();

    public static AsyncManager me()
    {
        return me;
    }

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(Runnable task)
    {
        execute(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * 执行任务
     *
     * @param task 任务
     * @param delayTime 延迟时间
     */
    public void execute(Runnable task, int delayTime)
    {
        executor.schedule(task, delayTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 执行任务
     *
     * @param task 任务
     * @param delayTime 延迟时间
     * @param timeUnit 时间单位
     */
    public void execute(Runnable task, int delayTime, TimeUnit timeUnit)
    {
        executor.schedule(task, delayTime, timeUnit);
    }

    /**
     * 停止任务线程池
     */
    public void shutdown()
    {
        Threads.shutdownAndAwaitTermination(executor);
    }
}
