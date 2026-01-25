package com.yxx.ai.controller;


import com.yxx.ai.config.storage.ChatConversationListRepository;
import com.yxx.ai.domain.ChatConversationEditDTO;
import com.yxx.ai.domain.ChatConversationListVO;
import com.yxx.ai.domain.MessageVO;
import com.yxx.common.core.domain.R;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/history")
public class ChatConversationController {

    private final ChatConversationListRepository chatConversationListRepository;

    private final ChatMemory chatMemory;

    @Operation(summary = "新增--会话历史")
    @PostMapping("")
    public R<String> getConversationList(@RequestBody ChatConversationEditDTO conversationEditDTO) {
        chatConversationListRepository.saveChatConversation(conversationEditDTO);
        return R.ok();
    }

    @Operation(summary = "查询--会话历史")
    @GetMapping("")
    public R<List<ChatConversationListVO>> getConversationList() {
        return R.ok(chatConversationListRepository.getChatConversationList());
    }

    @Operation(summary = "查询--会话历史单个")
    @GetMapping("/detail/{conversationId}")
    public R<List<MessageVO>> getConversationDetailById(@PathVariable String conversationId) {
        List<Message> messages = chatMemory.get(conversationId);

        List<MessageVO> result = messages.stream()
                .map(MessageVO::new)
                .collect(Collectors.toList());

        return R.ok(result);
    }
}
