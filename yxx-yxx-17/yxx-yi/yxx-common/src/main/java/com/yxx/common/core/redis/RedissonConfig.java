package com.yxx.common.core.redis;

import com.yxx.common.utils.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    private final RedisProperties redisProperties;

    public RedissonConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean
    public RedissonClient redissonClient() {

        Config config = new Config();

        // 构建连接地址
        String address = String.format("redis://%s:%d", redisProperties.getHost(), redisProperties.getPort());

        // 单节点配置
        SingleServerConfig singleServerConfig = config.useSingleServer()
                .setAddress(address)
                .setDatabase(redisProperties.getDatabase())
                .setConnectionPoolSize(64) // 连接池大小
                .setConnectionMinimumIdleSize(redisProperties.getLettuce().getPool().getMinIdle()) // 最小空闲连接数
                .setSubscriptionConnectionPoolSize(redisProperties.getLettuce().getPool().getMaxActive()) // 订阅连接池大小
                .setSubscriptionConnectionMinimumIdleSize(redisProperties.getLettuce().getPool().getMinIdle() / 2) // 订阅最小空闲连接
                .setDnsMonitoringInterval(5000) // DNS监控间隔(ms)
                .setConnectTimeout(10000) // 连接超时时间(ms)
                .setTimeout(3000) // 命令等待超时(ms)
                .setRetryAttempts(3) // 命令重试次数
                .setRetryInterval(1500) // 命令重试间隔(ms)
                .setPingConnectionInterval(30000) // 心跳检测间隔(ms)
                .setKeepAlive(true); // 启用TCP keepalive

        // 密码设置
        if (StringUtils.hasText(redisProperties.getPassword())) {
            singleServerConfig.setPassword(redisProperties.getPassword());
        }

        // 线程池配置
        config.setThreads(redisProperties.getLettuce().getPool().getMinIdle() * 2) // 处理Redis命令的线程数
                .setNettyThreads(redisProperties.getLettuce().getPool().getMinIdle() * 3); // Netty I/O线程数

        // 序列化配置
        config.setCodec(new JsonJacksonCodec()); // 使用JSON序列化

        // 创建Redisson客户端
        return Redisson.create(config);
    }
}
