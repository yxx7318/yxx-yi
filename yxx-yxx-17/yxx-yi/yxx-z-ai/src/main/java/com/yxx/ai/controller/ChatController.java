package com.yxx.ai.controller;

import com.yxx.ai.config.storage.ChatHistoryRepository;
import com.yxx.ai.domain.ChatEditDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.model.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

@Tag(name = "Ai会话接口-AiController")
@RestController
@RequestMapping("/ai/session")
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class ChatController {

    private final ChatHistoryRepository chatHistoryRepository;

    private final ChatClient chatClient;

    @PostMapping(value = "/chat", produces = "text/html;charset=utf-8")
    public Flux<String> chat(
            @RequestBody ChatEditDTO chatEditDTO,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        // 1.保存会话id
        chatHistoryRepository.save("chat", chatEditDTO.getChatId());
        // 2.请求模型
        if (files == null || files.isEmpty()) {
            // 没有附件，纯文本聊天
            return textChat(chatEditDTO.getPrompt(), chatEditDTO.getChatId());
        } else {
            // 有附件，多模态聊天
            return multiModalChat(chatEditDTO.getPrompt(), chatEditDTO.getChatId(), files);
        }
    }

    private Flux<String> multiModalChat(String prompt, String chatId, List<MultipartFile> files) {
        // 1.解析多媒体
        List<Media> medias = files.stream()
                .map(file -> new Media(
                                MimeType.valueOf(Objects.requireNonNull(file.getContentType())),
                                file.getResource()
                        )
                )
                .toList();
        // 2.请求模型
        return chatClient.prompt()
                .user(p -> p.text(prompt).media(medias.toArray(Media[]::new)))
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .stream()
                .content();
    }

    private Flux<String> textChat(String prompt, String chatId) {
        return chatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .stream()
                .content();
    }
}
