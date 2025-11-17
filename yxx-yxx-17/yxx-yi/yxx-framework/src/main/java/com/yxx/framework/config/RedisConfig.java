package com.yxx.framework.config;

import com.yxx.common.constant.Constants;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Role;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置
 */
@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport
{
    @Bean(name = Constants.REDIS_TEMPLATE)
    @Primary
    @SuppressWarnings(value = { "unchecked", "rawtypes" })
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory)
    {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        // Hash的key也采用StringRedisSerializer的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        // 执行初始化操作
        template.afterPropertiesSet();
        return template;
    }

    @Bean(name = Constants.LUA_REDIS_TEMPLATE)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public RedisTemplate<String, String> luaRedisTemplate(RedisConnectionFactory redisFactory)
    {
        StringRedisTemplate template = new StringRedisTemplate(redisFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());

        // 执行初始化操作
        template.afterPropertiesSet();
        return template;
    }
}
