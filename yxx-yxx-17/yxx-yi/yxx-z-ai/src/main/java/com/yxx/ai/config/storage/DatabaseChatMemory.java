package com.yxx.ai.config.storage;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yxx.ai.domain.MessageRecordDTO;
import com.yxx.ai.entity.AiChatDetailDO;
import com.yxx.ai.enums.MessageTypeEnum;
import com.yxx.ai.service.IAiChatDetailService;
import com.yxx.common.core.text.Convert;
import com.yxx.common.core.utils.JacksonUtils;
import com.yxx.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.yxx.ai.constant.Constants.RESOURCE_MEDIA_DATA;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class DatabaseChatMemory implements ChatMemory {

    private final IAiChatDetailService aiChatDetailService;

    @Override
    public void add(String conversationId, List<Message> messages) {
        if (messages == null || messages.isEmpty()) {
            return;
        }
        Long id = Convert.toLong(conversationId);
        List<AiChatDetailDO> chatDetailDOS = messages.stream().map(message -> {
            MessageRecordDTO messageRecordDTO = new MessageRecordDTO(message);
            AiChatDetailDO aiChatDetailDO = new AiChatDetailDO();
            aiChatDetailDO.setChatConversationId(id);
            aiChatDetailDO.setMessageType(MessageTypeEnum.fromValue(messageRecordDTO.getMessageType()));
            aiChatDetailDO.setContent(JacksonUtils.toJsonString(messageRecordDTO));
            aiChatDetailDO.setAttachment(JacksonUtils.toJsonString(messageRecordDTO.getMetadata().get(RESOURCE_MEDIA_DATA)));
            return aiChatDetailDO;
        }).collect(Collectors.toList());
        aiChatDetailService.saveBatch(chatDetailDOS);
    }

    @Override
    public List<Message> get(String conversationId) {
        List<AiChatDetailDO> chatDetailDOS = aiChatDetailService.getMpDOPage(0, Integer.MAX_VALUE).getRecords();
        List<MessageRecordDTO> messageRecordDTOList = chatDetailDOS.stream()
                .map(item -> JacksonUtils.parseObject(item.getContent(), MessageRecordDTO.class))
                .filter(StringUtils::isNotNull)
                .collect(Collectors.toList());
        return messageRecordDTOList.stream()
                .map(messageRecordDTO -> messageRecordDTO.toMessage())
                .collect(Collectors.toList());
    }

    @Override
    public void clear(String conversationId) {
        aiChatDetailService.remove(new LambdaQueryWrapper<AiChatDetailDO>().eq(AiChatDetailDO::getChatConversationId, conversationId));
    }
}
