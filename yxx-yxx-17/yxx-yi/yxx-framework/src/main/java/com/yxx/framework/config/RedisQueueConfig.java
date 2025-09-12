package com.yxx.framework.config;

import com.yxx.common.core.queue.QueueDispatcher;
import com.yxx.common.core.queue.QueueListener;
import com.yxx.common.core.queue.impl.RedisQueueDispatcher;
import com.yxx.common.core.queue.impl.RedisQueueListenerContainer;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Redis队列监听器配置类
 *
 * @author yxx
 */
@Slf4j
@Configuration
public class RedisQueueConfig {

    @Value("${spring.queue.name:queueName}")
    private String prefix;

    @Bean
    @ConditionalOnMissingBean({RedisQueueDispatcher.class, QueueDispatcher.class})
    public RedisQueueDispatcher redisQueueDispatcher(RedissonClient client) {
        log.info("RedisQueueDispatcher init ");
        return new RedisQueueDispatcher(client, this.prefix);
    }

    @Bean
    public RedisQueueListenerContainer redisQueueListenerContainer(RedisQueueDispatcher dispatcher,
                                                                   @Qualifier("listenerExecutor") ThreadPoolTaskExecutor taskExecutor,
                                                                   ObjectProvider<QueueListener<?>> listeners) {
        log.info("RedisQueueListenerContainer init ");
        RedisQueueListenerContainer container = new RedisQueueListenerContainer(dispatcher, taskExecutor);
        listeners.forEach(listener -> container.register(dispatcher.warpKey(listener.queueName()), listener));
        return container;
    }

}
