package com.yxx.ai.config.storage;

import com.yxx.ai.domain.ChatConversationEditDTO;
import com.yxx.ai.domain.ChatConversationListVO;

import java.util.List;

public interface ChatConversationListRepository {

    /**
     * 保存本次会话信息
     * @param conversationEditDTO 本次会话信息
     */
    void saveChatConversation(ChatConversationEditDTO conversationEditDTO);

    /**
     * 获取历史会话信息
     * @return 会话历史信息
     */
    List<ChatConversationListVO> getChatConversationList();
}
