package com.yxx.ai.controller;


import com.yxx.ai.config.storage.ChatHistoryRepository;
import com.yxx.ai.domain.MessageVO;
import com.yxx.common.core.domain.R;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/history")
public class ChatHistoryController {

    private final ChatHistoryRepository chatHistoryRepository;

    private final ChatMemory chatMemory;

    /**
     * 查询会话历史列表
     * @param type 业务类型，如：chat,service,pdf
     * @return chatId列表
     */
    @GetMapping("/{type}")
    public R<List<String>> getChatIds(@PathVariable("type") String type) {
        return R.ok(chatHistoryRepository.getChatIds(type));
    }

    /**
     * 根据业务类型、chatId查询会话历史
     * @param type 业务类型，如：chat,service,pdf
     * @param chatId 会话id
     * @return 指定会话的历史消息
     */
    @GetMapping("/{type}/{chatId}")
    public R<List<MessageVO>> getChatHistory(@PathVariable("type") String type, @PathVariable("chatId") String chatId) {
        List<Message> messages = chatMemory.get(chatId);
        if(messages == null) {
            return R.ok();
        }

        List<MessageVO> result = messages.stream()
                .map(MessageVO::new)
                .collect(Collectors.toList());

        return R.ok(result);
    }
}
