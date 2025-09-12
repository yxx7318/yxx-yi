package com.yxx.business.queue.dispatcher;

import com.yxx.common.core.queue.impl.RedisQueueDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class TestQueueDispatcher {

    private final RedisQueueDispatcher redisQueueDispatcher;

    public void testDispatcher() {
        // 分发延迟队列
        redisQueueDispatcher.dispatch("TestQueueListener", "TestPayload", Duration.ofSeconds(10));
    }
}
