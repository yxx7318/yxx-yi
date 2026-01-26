package com.yxx.ai.config.storage;

import com.yxx.ai.domain.ChatConversationEditDTO;
import com.yxx.ai.domain.ChatConversationVO;
import com.yxx.ai.entity.AiChatConversationDO;
import com.yxx.ai.entity.AiChatConversationQueryDTO;
import com.yxx.ai.service.IAiChatConversationService;
import com.yxx.common.utils.SecurityUtils;
import com.yxx.common.utils.bean.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class DatabaseChatHistory implements ChatConversationListRepository {

    private final IAiChatConversationService aiChatConversationService;

    @Override
    public ChatConversationVO saveChatConversation(ChatConversationEditDTO conversationEditDTO) {
        AiChatConversationDO aiChatConversationDO = BeanUtils.convertBean(conversationEditDTO, AiChatConversationDO.class);
        aiChatConversationService.save(aiChatConversationDO);
        return BeanUtils.convertBean(aiChatConversationDO, ChatConversationVO.class);
    }

    @Override
    public List<ChatConversationVO> getChatConversationList() {
        AiChatConversationQueryDTO aiChatConversationQueryDTO = new AiChatConversationQueryDTO();
        aiChatConversationQueryDTO.setCreateById(SecurityUtils.getUserId());
        List<AiChatConversationDO> aiChatConversationDOS =
                aiChatConversationService.selectAiChatConversationDOList(aiChatConversationQueryDTO);
        List<ChatConversationVO> chatConversationVOS =
                BeanUtils.convertList(aiChatConversationDOS, ChatConversationVO.class);
        Collections.reverse(chatConversationVOS);
        return chatConversationVOS;
    }
}
