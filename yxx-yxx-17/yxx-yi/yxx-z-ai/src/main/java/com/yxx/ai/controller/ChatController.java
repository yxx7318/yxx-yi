package com.yxx.ai.controller;

import com.yxx.ai.domain.ChatDTO;
import com.yxx.ai.domain.ConversationInfoDTO;
import com.yxx.ai.domain.DocumentInfoDTO;
import com.yxx.ai.manager.VectorDocumentManager;
import com.yxx.common.core.domain.dto.FileUploadDTO;
import com.yxx.common.core.utils.JacksonUtils;
import com.yxx.common.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.yxx.ai.constant.Constants.CONVERSATION_INFO_DATA;
import static org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor.FILTER_EXPRESSION;


@Tag(name = "Ai会话接口-AiController")
@RestController
@RequestMapping("/ai/session")
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class ChatController {

    private final VectorDocumentManager vectorDocumentManager;

    private final ChatClient chatClient;

    @PostMapping(value = "/chatStream", produces = "text/html;charset=utf-8")
    public Flux<String> chatStream(@RequestBody ChatDTO chatDTO) {
        ConversationInfoDTO conversationInfoDTO =
                new ConversationInfoDTO(chatDTO.getConversationId(), chatDTO.getFiles());
        // 请求模型
        if (chatDTO.getFiles() == null || chatDTO.getFiles().isEmpty()) {
            // 没有附件，纯文本聊天
            return textChat(chatDTO.getPrompt(), conversationInfoDTO);
        } else {
            // 有附件，多模态聊天
            return resourceModalChat(chatDTO.getPrompt(), conversationInfoDTO);
        }
    }

    private String wrapChunk(int id, String content, boolean finished) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", id);
        jsonMap.put("content", content);
        jsonMap.put("finished", finished);
        return JacksonUtils.toJsonString(jsonMap);
    }

    // "text/html;charset=utf-8"纯文本响应，"text/event-stream"SSE协议响应
    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatSSE(@RequestBody ChatDTO chatDTO) {
        Flux<String> responseFlux;

        ConversationInfoDTO conversationInfoDTO =
                new ConversationInfoDTO(chatDTO.getConversationId(), chatDTO.getFiles());
        // 请求模型
        if (chatDTO.getFiles() == null || chatDTO.getFiles().isEmpty()) {
            // 没有附件，纯文本聊天
            responseFlux = textChat(chatDTO.getPrompt(), conversationInfoDTO);
        } else {
            // 有附件，多模态聊天
            responseFlux =  resourceModalChat(chatDTO.getPrompt(), conversationInfoDTO);
        }
        AtomicInteger idCounter = new AtomicInteger(1);
        // 包装响应为 JSON 格式
        Flux<String> wrappedStream = responseFlux
                .map(chunk -> wrapChunk(idCounter.getAndIncrement(), chunk, false));

        String doneChunk = wrapChunk(0, "[DONE]", true);
        // 在响应流末尾添加结束标记
        return Flux.concat(wrappedStream, Flux.just(doneChunk));
    }

    private Flux<String> resourceModalChat(String prompt, ConversationInfoDTO conversationInfoDTO) {
        List<Resource> resources = conversationInfoDTO.getFiles()
                .stream().map(FileUploadDTO::extractResource).collect(Collectors.toList());
        // 写入向量库
        List<DocumentInfoDTO> documentInfoDTOS =
                resources.stream().map(vectorDocumentManager::writeToVectorStore).collect(Collectors.toList());
        // 请求模型
        return chatClient.prompt()
                .user(prompt)
                .user(p -> p.metadata(CONVERSATION_INFO_DATA, conversationInfoDTO))
                .user(p -> resources.forEach(r -> p.media(MimeType.valueOf("application/pdf"), r)))
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationInfoDTO.getConversationId()))
                .advisors(a -> resources.forEach(r -> a.param(FILTER_EXPRESSION, String.format("file_name == '%s'", r.getFilename()))))
                .stream()
                .content();
    }


    private Flux<String> multiModalChat(String prompt, String conversationId, List<MultipartFile> files) {
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
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content();
    }

    private Flux<String> textChat(String prompt, ConversationInfoDTO conversationInfoDTO) {
        return chatClient.prompt()
                .user(prompt)
                .user(p -> p.metadata(CONVERSATION_INFO_DATA, conversationInfoDTO))
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationInfoDTO.getConversationId()))
                .stream()
                .content();
    }
}
