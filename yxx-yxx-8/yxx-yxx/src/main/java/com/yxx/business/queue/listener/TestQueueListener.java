package com.yxx.business.queue.listener;

import com.yxx.common.core.queue.QueueListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class TestQueueListener implements QueueListener<String> {

    @Override
    public String queueName() {
        // 队列名称
        return "TestQueueListener";
    }

    @Override
    public void onMessage(String msg) {
        try {
            log.info(msg);
        } catch (Exception ex) {
            // 手动处理异常，不会触发 onError 方法
            log.error("TestQueueListener receive error: {}", ex.getMessage());
        }
    }

    @Override
    public void onError(String msg, Throwable e) {
        // 对于 onMessage 执行异常处理
        log.error(msg, e);
    }
}
