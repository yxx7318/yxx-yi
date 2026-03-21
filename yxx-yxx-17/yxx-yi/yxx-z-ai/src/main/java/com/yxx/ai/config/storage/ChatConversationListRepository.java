package com.yxx.ai.config.storage;

import com.yxx.ai.domain.ChatConversationEditDTO;
import com.yxx.ai.domain.ChatConversationVO;

import java.util.List;

public interface ChatConversationListRepository {

    /**
     * 保存本次会话信息
     * @param conversationEditDTO 保存会话信息
     * @return 当前会话信息
     */
    ChatConversationVO saveChatConversation(ChatConversationEditDTO conversationEditDTO);

    /**
     * 获取历史会话信息
     * @return 会话历史信息
     */
    List<ChatConversationVO> getChatConversationList();
}
