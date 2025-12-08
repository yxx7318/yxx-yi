package com.yxx.ai.controller;

import com.yxx.ai.config.storage.ChatHistoryRepository;
import com.yxx.ai.domain.ChatDTO;
import com.yxx.ai.domain.DocumentInfoDTO;
import com.yxx.ai.manager.VectorDocumentManager;
import com.yxx.common.core.domain.dto.FileUploadDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.Media;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor.FILTER_EXPRESSION;

@Tag(name = "Ai会话接口-AiController")
@RestController
@RequestMapping("/ai/session")
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class ChatController {

    private final VectorDocumentManager vectorDocumentManager;

    private final ChatHistoryRepository chatHistoryRepository;

    private final ChatClient chatClient;

    @PostMapping(value = "/chat", produces = "text/html;charset=utf-8")
    public Flux<String> chat(@RequestBody ChatDTO chatDTO) {
        // 1.保存会话id
        chatHistoryRepository.save("chat", chatDTO.getChatId());
        // 2.请求模型
        if (chatDTO.getFiles() == null || chatDTO.getFiles().isEmpty()) {
            // 没有附件，纯文本聊天
            return textChat(chatDTO.getPrompt(), chatDTO.getChatId());
        } else {
            // 有附件，多模态聊天
            return resourceModalChat(chatDTO.getPrompt(), chatDTO.getChatId(), chatDTO.getFiles());
        }
    }

    private Flux<String> resourceModalChat(String prompt, String chatId, List<FileUploadDTO> files) {
        List<Resource> resources = files.stream().map(FileUploadDTO::extractResource).collect(Collectors.toList());

        List<DocumentInfoDTO> documentInfoDTOS =
                resources.stream().map(vectorDocumentManager::writeToVectorStore).collect(Collectors.toList());
        // 请求模型
        return chatClient.prompt()
                .user(prompt)
                .user(p -> resources.forEach(r -> p.media(MimeType.valueOf("application/pdf"), r)))
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .advisors(a -> resources.forEach(r -> a.param(FILTER_EXPRESSION, String.format("file_name == '%s'", r.getFilename()))))
                .stream()
                .content();
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
