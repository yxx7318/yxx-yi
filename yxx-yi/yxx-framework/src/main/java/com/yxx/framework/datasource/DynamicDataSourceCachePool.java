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
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据元缓存池
 */
@Slf4j
@Component
@Primary
public class DynamicDataSourceCachePool extends AbstractRoutingDataSource {
    /** 数据源连接池缓存【本地内存缓存 - 不支持分布式】 */
    private static final Map<Object, Object> targetDataSources = new ConcurrentHashMap<>(10);

    private static RedisCache redisCache;

    /**
     * 构造注入
     * @param defaultTargetDataSource 默认数据源
     * @param redisCache redis缓存
     */
    public DynamicDataSourceCachePool(DataSource defaultTargetDataSource,RedisCache redisCache)
    {
        DynamicDataSourceCachePool.redisCache = redisCache;
        //设置默认数据源
        super.setDefaultTargetDataSource(defaultTargetDataSource);

        //设置可切换的数据源
        super.setTargetDataSources(targetDataSources);

        loadDynamicDataSourceByObject(DataSourceConst.MASTER,defaultTargetDataSource);

        //使得数据源设置生效
        super.afterPropertiesSet();
    }

    /**
     *  数据源切换实现
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }

    /**
     * 根据对象和key添加数据源
     * @param dataSource 数据源对象
     */
    public static  void loadDynamicDataSourceByObject(String dbKey,DataSource dataSource){
        targetDataSources.put(dbKey, dataSource);

    }

    /**
     * 移除数据源-清空本地+redis数据源缓存(主从库除外)
     */
    public static void cleanAllDynamicCache() {
        //关闭数据源连接
        for(Map.Entry<Object, Object> entry : targetDataSources.entrySet()){
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
        DruidDataSource localDataSource = (DruidDataSource) targetDataSources.get(dbKey);

        //如果本地存在数据源并且可用
        if(localDataSource != null &&!localDataSource.isClosed()){
            log.debug("------------------从本地缓存中获取DB连接{}-------------------", dbKey);
            return true;
        }


        //查缓存数据源配置
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
        targetDataSources.put(dbKey, localDataSource);

        return true;

    }

    /**
     *  设置默认数据源,用于主从故障切换
     */
    public static boolean setDefaultDataSource(String dbKey){
        if(dbKey.equals(DataSourceConst.MASTER)||dbKey.equals(DataSourceConst.SLAVE)){
            //如果要切换的数据源不能用
            if(!DynamicDataSourceCachePool.isDatabaseAlive(dbKey)) return false;

            DynamicDataSourceCachePool pool = SpringUtils.getBean(DynamicDataSourceCachePool.class);

            pool.setDefaultTargetDataSource(targetDataSources.get(dbKey));
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
        DataSource dataSource= (DataSource) targetDataSources.get(dbKey);
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

        String driverClassName = dbSource.getDriverClassname();
        String url = dbSource.getUrl();
        String dbUser = dbSource.getUsername();
        String dbPassword = dbSource.getPassword();

        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPassword);


        log.info("******************************************");
        log.info("*====【"+dbSource.getCode()+"】=====Druid连接池已启用 ====*");
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
        ISysResourcesService sysTenantService = SpringUtils.getBean("sysResourcesServiceImpl");


        return sysTenantService.getDynamicDbSourceByCode(dbKey);
    }


    /**
     * 移除缓存(本地+redis)
     * @param dbKey 数据源编码
     */
    private static void  removeCache(String dbKey){

        //关闭数据源连接
        DruidDataSource druidDataSource = (DruidDataSource) targetDataSources.get(dbKey);
        if(druidDataSource!=null && druidDataSource.isEnable()){
            druidDataSource.close();
        }

        //移除redis缓存
        redisCache.deleteCacheMapValue(CacheConstants.DATA_SOURCE_KEY,dbKey);
        //清除本地缓存
        targetDataSources.remove(dbKey);
    }


}

