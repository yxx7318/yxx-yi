package com.yxx.common.core.redis;

import com.yxx.common.utils.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@ConditionalOnProperty(name = "redis.data.redisson", havingValue = "true")
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.database}")
    private int redisDatabase;

    // 如果有密码的话，添加以下属性
    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();

        // 构建单个Redis节点的连接地址
        String address = String.format("redis://%s:%d", redisHost, redisPort);

        // 设置单个Redis节点的配置
        config.useSingleServer()
                .setAddress(address)
                .setDatabase(redisDatabase);

        // 如果设置了密码，则启用密码验证
        if (StringUtils.hasText(redisPassword)) {
            config.useSingleServer().setPassword(redisPassword);
        }

        // 创建并返回RedissonClient实例
        return Redisson.create(config);
    }
}
