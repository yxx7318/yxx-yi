package com.yxx.framework.datasource;

import java.util.Map;
import javax.sql.DataSource;

import com.yxx.common.utils.spring.SpringUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 * 
 * @author ruoyi
 */
public class DynamicDataSource extends AbstractRoutingDataSource
{

    public DynamicDataSource(DataSource defaultTargetDataSource)
    {
        //设置默认数据源
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        //设置可切换的数据源
        super.setTargetDataSources(DataSourceCachePool.dbSources);
        //使得数据源设置生效
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey()
    {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }
}