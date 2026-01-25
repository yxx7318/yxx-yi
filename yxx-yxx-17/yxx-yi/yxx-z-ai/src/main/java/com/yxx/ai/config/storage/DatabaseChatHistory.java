package com.yxx.ai.config.storage;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yxx.ai.domain.ChatConversationEditDTO;
import com.yxx.ai.domain.ChatConversationListVO;
import com.yxx.ai.entity.AiChatConversationDO;
import com.yxx.ai.entity.AiChatConversationEditDTO;
import com.yxx.ai.entity.AiChatConversationQueryDTO;
import com.yxx.ai.service.IAiChatConversationService;
import com.yxx.common.core.text.Convert;
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
    public void saveChatConversation(ChatConversationEditDTO conversationEditDTO) {
        // 是否保存过会话
        boolean exists = aiChatConversationService.exists(new LambdaQueryWrapper<AiChatConversationDO>()
                .eq(AiChatConversationDO::getChatConversationId, Convert.toLong(conversationEditDTO.getChatConversationId())));
        if (!exists) {
            AiChatConversationDO aiChatConversationDO = BeanUtils.convertBean(conversationEditDTO, AiChatConversationDO.class);
            aiChatConversationService.save(aiChatConversationDO);
        }
    }

    @Override
    public List<ChatConversationListVO> getChatConversationList() {
        AiChatConversationQueryDTO aiChatConversationQueryDTO = new AiChatConversationQueryDTO();
        aiChatConversationQueryDTO.setCreateById(SecurityUtils.getUserId());
        List<AiChatConversationDO> aiChatConversationDOS =
                aiChatConversationService.selectAiChatConversationDOList(aiChatConversationQueryDTO);
        List<ChatConversationListVO> chatConversationListVOS =
                BeanUtils.convertList(aiChatConversationDOS, ChatConversationListVO.class);
        Collections.reverse(chatConversationListVOS);
        return chatConversationListVOS;
    }
}
