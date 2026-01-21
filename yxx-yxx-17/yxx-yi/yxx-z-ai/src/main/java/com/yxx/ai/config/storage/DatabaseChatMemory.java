package com.yxx.ai.config.storage;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yxx.ai.domain.Msg;
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
            Msg msg = new Msg(message);
            AiChatDetailDO aiChatDetailDO = new AiChatDetailDO();
            aiChatDetailDO.setChatConversationId(id);
            aiChatDetailDO.setMessageType(MessageTypeEnum.fromValue(msg.getMessageType()));
            aiChatDetailDO.setContent(JacksonUtils.toJsonString(msg));
            aiChatDetailDO.setAttachment(JacksonUtils.toJsonString(msg.getMetadata().get(RESOURCE_MEDIA_DATA)));
            return aiChatDetailDO;
        }).collect(Collectors.toList());
        aiChatDetailService.saveBatch(chatDetailDOS);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        List<AiChatDetailDO> chatDetailDOS = aiChatDetailService.getMpDOPage(0, lastN).getRecords();
        List<Msg> msgList = chatDetailDOS.stream()
                .map(item -> JacksonUtils.parseObject(item.getContent(), Msg.class))
                .filter(StringUtils::isNotNull)
                .collect(Collectors.toList());
        return msgList.stream()
                .map(msg -> msg.toMessage())
                .collect(Collectors.toList());
    }

    @Override
    public void clear(String conversationId) {
        aiChatDetailService.remove(new LambdaQueryWrapper<AiChatDetailDO>().eq(AiChatDetailDO::getChatConversationId, conversationId));
    }
}
