package com.yxx.framework.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson2.JSON;
import com.yxx.common.constant.DataSourceConst;
import com.yxx.common.utils.spring.SpringUtils;
import com.yxx.system.domain.SysResources;
import com.yxx.system.service.ISysResourcesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 数据元缓存池
 */
@Slf4j
public class DataSourceCachePool {
    /** 数据源连接池缓存【本地内存缓存 - 不支持分布式】 */
    public static Map<Object, Object> dbSources = new ConcurrentHashMap<>(10);

    private static RedisTemplate<String, Object> redisTemplate;

    private static RedisTemplate<String, Object> getRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = SpringUtils.getBean("redisTemplate");
        }
        return redisTemplate;
    }

    /**
     * 加载数据源
     * @param dbKey 是否加载成功
     * @return
     */
    public static boolean loadDynamicDataSource(String dbKey){
        //数据源
        DruidDataSource dataSource = (DruidDataSource) dbSources.get(dbKey);
        //如果本地不存在数据源
        if (dataSource == null || dataSource.isClosed()) {

            //查缓存或数据库数据源配置
            SysResources sysResources = getCacheDynamicDataSourceModel(dbKey);

            if (sysResources==null) {
                return false;
            }

            //生成数据源
             dataSource = getJdbcDataSource(sysResources);

             //如果数据源不可用
            if(!dataSource.isEnable()){
                return false;
            }

            //本地添加数据源
            dbSources.put(dbKey, dataSource);

            log.info("--------getDbSourceBydbKey------------------创建DB数据库连接"+dbKey+"-------------------");

        }else {
            log.debug("--------getDbSourceBydbKey------------------从缓存中获取DB连接"+dbKey+"-------------------");
        }

        return true;

    }

    /**
     * 获取数据源【最底层方法，不要随便调用】
     *
     * @param dbSource 数据库数据源
     * @return
     */
        private static DruidDataSource getJdbcDataSource( SysResources dbSource) {
        DruidDataSource dataSource = new DruidDataSource();

        String driverClassName = dbSource.getDriverClassname();
        String url = dbSource.getUrl();
        String dbUser = dbSource.getUsername();
        String dbPassword = dbSource.getPassword();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        //dataSource.setValidationQuery("SELECT 1 FROM DUAL");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setBreakAfterAcquireFailure(true);
        dataSource.setConnectionErrorRetryAttempts(0);
        dataSource.setUsername(dbUser);
        dataSource.setMaxWait(60000);
        dataSource.setPassword(dbPassword);

        log.info("******************************************");
        log.info("*                                        *");
        log.info("*====【"+dbSource.getCode()+"】=====Druid连接池已启用 ====*");
        log.info("*                                        *");
        log.info("******************************************");
        return dataSource;
    }

    /**
     * 查找数据源配置
     * @param dbKey
     * @return
     */
    private static SysResources getCacheDynamicDataSourceModel(String dbKey) {
        //数据源缓存key
        String redisCacheKey = DataSourceConst.SYSRESOURCES + dbKey;

        RedisTemplate<String, Object> template = getRedisTemplate();

        //系统数据源
        SysResources sysResources;

        //如果缓存命中
        if (Boolean.TRUE.equals(template.hasKey(redisCacheKey))) {
            String model = (String)template.opsForValue().get(redisCacheKey);
            sysResources= JSON.parseObject(model, SysResources.class);
            // 刷新缓存过期时间
            template.expire(redisCacheKey, 1, TimeUnit.HOURS);
            return sysResources;
        }

        //数据源服务
        ISysResourcesService sysTenantService = SpringUtils.getBean("sysResourcesServiceImpl");

        sysResources = sysTenantService.getDynamicDbSourceByCode(dbKey);

        //如果数据源信息不为空，则存入缓存
        if (sysResources != null) {
            template.opsForValue().set(redisCacheKey, JSON.toJSONString(sysResources));
            // 缓存过期时间
            template.expire(redisCacheKey, 1, TimeUnit.HOURS);
        }

        return sysResources ;
    }

    /**
     * 清空动态数据源缓存
     */
    public static void cleanAllDynamicCache() {
        //关闭数据源连接
        for(Map.Entry<Object, Object> entry : dbSources.entrySet()){
            String dbkey = (String) entry.getKey();
            //排除主从库
            if(!Objects.equals(dbkey, DataSourceConst.MASTER) && !Objects.equals(dbkey, DataSourceConst.SLAVE)){
                removeCache(dbkey);
            }
        }
    }

    /**
     * 根据key删除动态数据源
     * @param dbKey
     * @throws Exception
     */
    public static void removeDynamicCache(String dbKey) throws Exception {
        if(Objects.equals(dbKey, DataSourceConst.MASTER)|| Objects.equals(dbKey, DataSourceConst.SLAVE)){
            throw new Exception("不可删除"+dbKey);
        }
        removeCache(dbKey);
    }

    /**
     * 移除缓存
     * @param dbKey
     */
    private static void  removeCache(String dbKey){
        //关闭数据源连接
        DruidDataSource druidDataSource = (DruidDataSource) dbSources.get(dbKey);
        if(druidDataSource!=null && druidDataSource.isEnable()){
            druidDataSource.close();
        }

        //清空redis缓存
        getRedisTemplate().delete(DataSourceConst.SYSRESOURCES+ dbKey);
        //清空本地缓存
        dbSources.remove(dbKey);
    }

}

