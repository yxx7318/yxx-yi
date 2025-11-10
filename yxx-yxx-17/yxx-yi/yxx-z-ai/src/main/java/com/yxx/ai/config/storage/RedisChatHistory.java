package com.yxx.ai.config.storage;

import com.yxx.common.utils.spring.SpringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.yxx.ai.constant.Constants.CHAT_HISTORY_KEY_PREFIX;
import static com.yxx.common.constant.Constants.REDIS_TEMPLATE;

@Component
public class RedisChatHistory implements ChatHistoryRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisChatHistory() {
        this.redisTemplate = SpringUtils.getBean(REDIS_TEMPLATE);
    }

    @Override
    public void save(String type, String chatId) {
        redisTemplate.opsForSet().add(CHAT_HISTORY_KEY_PREFIX + type, chatId);
    }

    @Override
    public List<String> getChatIds(String type) {
        Set<String> chatIds = redisTemplate.opsForSet().members(CHAT_HISTORY_KEY_PREFIX + type);
        if (chatIds == null || chatIds.isEmpty()) {
            return Collections.emptyList();
        }
        return chatIds.stream().sorted(String::compareTo).toList();
    }
}
