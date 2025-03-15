package com.yxx.framework.aspectj;

import java.util.Objects;

import com.yxx.common.utils.spring.SpringUtils;
import com.yxx.framework.datasource.DataSourceCachePool;
import com.yxx.framework.datasource.DynamicDataSource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.yxx.common.annotation.DataSource;
import com.yxx.common.utils.StringUtils;
import com.yxx.framework.datasource.DynamicDataSourceContextHolder;

/**
 * 多数据源处理
 * 
 * @author ruoyi
 */
@Aspect
@Order(1)
@Component
@Slf4j
public class DataSourceAspect
{
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(com.yxx.common.annotation.DataSource)"
            + "|| @within(com.yxx.common.annotation.DataSource)")
    public void dsPointCut()
    {

    }

    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable
    {
        DataSource dataSource = getDataSource(point);

        if (StringUtils.isNotNull(dataSource))
        {

            String key=dataSource.value();

            //加载数据源
            boolean flag=  DataSourceCachePool.loadDynamicDataSource(key);

            if(!flag)throw new Exception("数据源加载失败:"+key);


            //切换数据源
            DynamicDataSourceContextHolder.switchDataSource(key);

            DynamicDataSource dynamicDataSource =  SpringUtils.getBean("dynamicDataSource");
            //使得修改后的targetDataSources生效
            dynamicDataSource.afterPropertiesSet();

        }

        try
        {
            return point.proceed();
        }
        finally
        {
            // 销毁数据源 在执行方法之后
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }

    /**
     * 获取需要切换的数据源
     */
    public DataSource getDataSource(ProceedingJoinPoint point)
    {
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataSource dataSource = AnnotationUtils.findAnnotation(signature.getMethod(), DataSource.class);
        if (Objects.nonNull(dataSource))
        {
            return dataSource;
        }

        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataSource.class);
    }
}
