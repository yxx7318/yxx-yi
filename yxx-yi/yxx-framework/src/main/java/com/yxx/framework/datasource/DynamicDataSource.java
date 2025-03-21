package com.yxx.framework.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.yxx.common.constant.CacheConstants;
import com.yxx.common.constant.DataSourceConst;
import com.yxx.common.core.redis.RedisCache;
import com.yxx.common.utils.spring.SpringUtils;
import com.yxx.framework.config.properties.DruidProperties;
import com.yxx.system.domain.SysResources;
import com.yxx.system.service.ISysResourcesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地动态数据源缓存池
 */
@Slf4j
@Component
@Primary
public class DynamicDataSource extends AbstractRoutingDataSource {
    /** 数据源连接池缓存【本地内存缓存】 */
    private static final Map<Object, Object> localDataSourcesCachePool = new ConcurrentHashMap<>(10);

    private static final RedisCache redisCache=SpringUtils.getBean(RedisCache.class);

    /**
     * 主库数据源
     */
    private static final DataSource master = SpringUtils.getBean("masterDataSource");

    /**
     * 从库数据源
     */
    private static DataSource slave;

    /**
     * 构造注入
     * @param slaveDataSource 从库数据源
     */
    public DynamicDataSource(@Qualifier("slaveDataSource") @Nullable DataSource slaveDataSource)
    {
        slave=slaveDataSource;

        //设置默认数据源
        super.setDefaultTargetDataSource(master);

        localDataSourcesCachePool.put(DataSourceConst.MASTER, master);

        if(slaveDataSource!=null){
            localDataSourcesCachePool.put(DataSourceConst.SLAVE,slaveDataSource);
        }

        //设置可切换数据源map集合
        super.setTargetDataSources(localDataSourcesCachePool);

        //使得数据源设置生效
        super.afterPropertiesSet();
    }

    /**
     *  数据源上下文切换实现
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }



    /**
     * 移除数据源-清空本地+redis数据源缓存(主从库除外)
     */
    public static void cleanAllDynamicCache() {
        //关闭数据源连接
        for(Map.Entry<Object, Object> entry : localDataSourcesCachePool.entrySet()){
            String dbkey = (String) entry.getKey();
            //排除主从库
            if(!Objects.equals(dbkey, DataSourceConst.MASTER) && !Objects.equals(dbkey, DataSourceConst.SLAVE)){
                removeCache(dbkey);
            }
        }
    }

    /**
     * 移除数据源-根据key删除本地+redis数据源(主从库除外)
     * @param dbKey 数据源编码
     * @throws Exception
     */
    public static void removeDynamicCache(String dbKey) throws Exception {
        if(Objects.equals(dbKey, DataSourceConst.MASTER)|| Objects.equals(dbKey, DataSourceConst.SLAVE)){
            throw new Exception("不可删除"+dbKey);
        }
        removeCache(dbKey);
    }


    /**
     * 根据编码加载数据源
     * @param dbKey 编码
     * @return
     */
    public static boolean loadDynamicDataSourceByKey(String dbKey){

        //本地数据源
        DruidDataSource localDataSource = (DruidDataSource) localDataSourcesCachePool.get(dbKey);

        //如果本地存在数据源并且可用
        if(localDataSource != null &&!localDataSource.isClosed()){
            log.debug("------------------从本地缓存中获取DB连接{}-------------------", dbKey);
            return true;
        }

        //加载缓存数据源配置
        SysResources  sysResources   = redisCache.getCacheMapValue(CacheConstants.DATA_SOURCE_KEY,dbKey);

        //如果缓存没有
        if(sysResources==null){
            //查数据库数据源配置
            sysResources = getDBDataSource(dbKey);

            //数据库没有
            if(sysResources==null) return false;

            //有就缓存下来
            redisCache.setCacheMapValue(CacheConstants.DATA_SOURCE_KEY,dbKey,sysResources);

        }

        //生成数据源
        localDataSource = getJdbcDataSource(sysResources);

        //如果数据源不可用
        if(!localDataSource.isEnable()){
            return false;
        }
        log.debug("------------------创建DB连接{}-------------------", dbKey);
        //本地添加数据源
        localDataSourcesCachePool.put(dbKey, localDataSource);

        return true;

    }

    /**
     *  设置默认数据源,用于主从故障切换
     */
    public static boolean setDefaultDataSource(String dbKey){
        if(dbKey.equals(DataSourceConst.MASTER)||dbKey.equals(DataSourceConst.SLAVE)){
            //如果要切换的数据源不能用
            if(!DynamicDataSource.isDatabaseAlive(dbKey)) return false;

            DynamicDataSource pool = SpringUtils.getBean(DynamicDataSource.class);

            pool.setDefaultTargetDataSource(localDataSourcesCachePool.get(dbKey));
            //使得数据源设置生效
            pool.afterPropertiesSet();
            return true;
        }
        return false;
    }

    /**
     * 判断数据源是否存活可用
     * @param dbKey 编码
     * @return
     */
    public static boolean isDatabaseAlive(String dbKey){
        DataSource dataSource= (DataSource) localDataSourcesCachePool.get(dbKey);
        try {
            Connection connection = dataSource.getConnection();
            connection.createStatement().execute("SELECT 1 FROM DUAL");
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 生成数据源【最底层方法，不要随便调用】
     *
     * @param dbSource 数据库数据源
     * @return
     */
    private static DruidDataSource getJdbcDataSource( SysResources dbSource) {
        DruidDataSource dataSource = new DruidDataSource();
        DruidProperties druidProperties= SpringUtils.getBean("druidProperties");

        dataSource.setDriverClassName(dbSource.getDriverClassname());
        dataSource.setUrl(dbSource.getUrl());
        dataSource.setUsername(dbSource.getUsername());
        dataSource.setPassword(dbSource.getPassword());

        log.info("******************************************");
        log.info("*====【{}】=====Druid连接池已启用 ====*", dbSource.getCode());
        log.info("******************************************");

        return druidProperties.dataSource(dataSource);
    }

    /**
     * 查找数据源配置
     * @param dbKey 数据源编码
     * @return
     */
    private static SysResources getDBDataSource(String dbKey) {
        //数据源服务
        ISysResourcesService sysResourcesService = SpringUtils.getBean("sysResourcesServiceImpl");


        return sysResourcesService.getDynamicDbSourceByCode(dbKey);
    }


    /**
     * 移除缓存(本地+redis)
     * @param dbKey 数据源编码
     */
    private static void  removeCache(String dbKey){

        //关闭数据源连接
        DruidDataSource druidDataSource = (DruidDataSource) localDataSourcesCachePool.get(dbKey);
        if(druidDataSource!=null && druidDataSource.isEnable()){
            druidDataSource.close();
        }

        //移除redis缓存
        redisCache.deleteCacheMapValue(CacheConstants.DATA_SOURCE_KEY,dbKey);
        //清除本地缓存
        localDataSourcesCachePool.remove(dbKey);
    }


}

