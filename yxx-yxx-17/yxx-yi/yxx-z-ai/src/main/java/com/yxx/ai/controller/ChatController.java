package com.yxx.ai.controller;

import com.yxx.ai.config.storage.ChatHistoryRepository;
import com.yxx.ai.domain.ChatDTO;
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

    private final VectorStore vectorStore;

    private void writeToVectorStore(Resource resource) {
        // 1.创建PDF的读取器
        PagePdfDocumentReader reader = new PagePdfDocumentReader(
                resource, // 文件源
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults())
                        .withPagesPerDocument(1) // 每1页PDF作为一个Document
                        .build()
        );
        // 2.读取PDF文档，拆分为Document
        List<Document> documents = reader.read();
        // 3.写入向量库
        vectorStore.add(documents);
    }

    private Flux<String> resourceModalChat(String prompt, String chatId, List<FileUploadDTO> files) {
        List<Resource> resources = files.stream().map(i -> new FileSystemResource(i.getLocalPath())).collect(Collectors.toList());

        resources.forEach(this::writeToVectorStore);
        // 2.请求模型
        return chatClient.prompt()
                .user(prompt)
                .user(p -> resources.forEach(r -> p.media(MimeType.valueOf("application/pdf"), r)))
                .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId))
                .advisors(a -> resources.forEach(r -> {
                    try {
                        a.param(FILTER_EXPRESSION, String.format("file_name == '%s'", r.getFile().getName()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }))
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
