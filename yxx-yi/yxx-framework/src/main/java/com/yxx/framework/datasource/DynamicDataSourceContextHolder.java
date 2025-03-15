package com.yxx.framework.datasource;

import com.yxx.common.utils.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 数据源切换处理
 * 
 * @author ruoyi
 */
public class DynamicDataSourceContextHolder
{
    public static final Logger log = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);

    /**
     * 使用ThreadLocal维护变量，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
     * 所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
     */
    private static final ThreadLocal<Deque<String>> CONTEXT_HOLDER = ThreadLocal.withInitial(ArrayDeque::new);

    /**
     * 切换数据源
     */
    public static void switchDataSource(String dsType)
    {
        log.info("切换到{}数据源", dsType);
        CONTEXT_HOLDER.get().push(dsType);
        //CONTEXT_HOLDER.set(dsType);

    }

    /**
     * 获得数据源的变量
     */
    public static String getDataSourceType()
    {
        //return CONTEXT_HOLDER.get();
        return CONTEXT_HOLDER.get().peek();
    }

    /**
     * 清空数据源变量
     */
    public static void clearDataSourceType()
    {
       // CONTEXT_HOLDER.remove();
        Deque<String> deque = CONTEXT_HOLDER.get();
        deque.poll();
        if (deque.isEmpty()) {
            CONTEXT_HOLDER.remove();
        }
    }
}
