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
     * 使用Deque实现堆栈效果的好处：切换数据源时不影响数据库事务
     */
    private static final ThreadLocal<Deque<String>> CONTEXT_HOLDER = ThreadLocal.withInitial(ArrayDeque::new);

    /**
     * 切换数据源
     */
    public static void cutDataSource(String dbKey) throws Exception {
        DynamicDataSource dynamicDataSource =  SpringUtils.getBean(DynamicDataSource.class);
        boolean flag=  DynamicDataSource.loadDynamicDataSourceByKey(dbKey);
        if(!flag)throw new Exception("数据源加载失败:"+dbKey);
        log.info("切换到{}数据源", dbKey);

        CONTEXT_HOLDER.get().push(dbKey);

        //使得修改后的targetDataSources生效
        dynamicDataSource.afterPropertiesSet();

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
     *还原默认数据源 在执行方法之后
     */
    public static void BackDefaultDataSource()
    {
       // CONTEXT_HOLDER.remove();
        Deque<String> deque = CONTEXT_HOLDER.get();
        deque.poll();
        if (deque.isEmpty()) {
            CONTEXT_HOLDER.remove();
        }
    }
}
