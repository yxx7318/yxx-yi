package com.yxx.ai.config.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.yxx.ai.domain.Msg;
import com.yxx.common.utils.spring.SpringUtils;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.yxx.ai.constant.Constants.CHAT_MEMORY_KEY_PREFIX;
import static com.yxx.common.constant.Constants.REDIS_TEMPLATE;

//@Component
public class RedisChatMemory implements ChatMemory {

    private final RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper;

    public RedisChatMemory() {
        this.redisTemplate = SpringUtils.getBean(REDIS_TEMPLATE);
        this.objectMapper = SpringUtils.getBean(ObjectMapper.class);
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        if (messages == null || messages.isEmpty()) {
            return;
        }
        List<String> list = messages.stream().map(Msg::new).map(msg -> {
            try {
                return objectMapper.writeValueAsString(msg);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();
        redisTemplate.opsForList().leftPushAll(CHAT_MEMORY_KEY_PREFIX + conversationId, list);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        List<String> list = redisTemplate.opsForList().range(CHAT_MEMORY_KEY_PREFIX + conversationId, 0, lastN);
        if (list == null || list.isEmpty()) {
            return List.of();
        }
        List<Message> messageList = list.stream().map(s -> {
            try {
                return objectMapper.readValue(s, Msg.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).map(Msg::toMessage).collect(Collectors.toList());
        // 反转列表
        Collections.reverse(messageList);
        return messageList;
    }

    @Override
    public void clear(String conversationId) {
        redisTemplate.delete(CHAT_MEMORY_KEY_PREFIX + conversationId);
    }
}
