package com.yxx.ai.mapper;

import java.util.List;

import com.yxx.ai.entity.AiChatConversationDO;
import com.yxx.ai.entity.AiChatConversationQueryDTO;
import com.yxx.common.core.mapper.BaseMapperPlus;
import org.springframework.stereotype.Repository;

/**
 * 会话Mapper接口
 *
 * @author yxx
 * @date 2026-01-21
 */
@Repository
public interface AiChatConversationMapper extends BaseMapperPlus<AiChatConversationDO> {

    /**
     * 查询会话列表
     *
     * @param aiChatConversationQueryDTO 会话查询实体
     * @return 会话集合
     */
    public List<AiChatConversationDO> selectAiChatConversationList(AiChatConversationQueryDTO aiChatConversationQueryDTO);

    /**
     * 查询会话
     *
     * @param chatConversationId 会话主键
     * @return 会话单个
     */
    public AiChatConversationDO selectAiChatConversationByChatConversationId(Long chatConversationId);

    /**
     * 新增会话
     *
     * @param aiChatConversationDO 会话数据库实体
     * @return 结果
     */
    public int insertAiChatConversation(AiChatConversationDO aiChatConversationDO);

    /**
     * 修改会话
     *
     * @param aiChatConversationDO 会话数据库实体
     * @return 结果
     */
    public int updateAiChatConversation(AiChatConversationDO aiChatConversationDO);

    /**
     * 删除会话
     *
     * @param chatConversationId 会话主键
     * @return 结果
     */
    public int deleteAiChatConversationByChatConversationId(Long chatConversationId);

    /**
     * 批量删除会话
     *
     * @param chatConversationIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAiChatConversationByChatConversationIds(List<Long> chatConversationIds);
}
