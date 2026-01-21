package com.yxx.ai.service;

import java.util.List;

import com.yxx.common.core.domain.PageResult;
import com.yxx.ai.entity.AiChatConversationDO;
import com.yxx.ai.entity.AiChatConversationVO;
import com.yxx.ai.entity.AiChatConversationQueryDTO;
import com.yxx.ai.entity.AiChatConversationEditDTO;
import com.yxx.common.core.service.IServicePlus;

/**
 * 会话Service接口
 *
 * @author yxx
 * @date 2026-01-21
 */
public interface IAiChatConversationService extends IServicePlus<AiChatConversationDO> {

    /**
     * 查询会话分页结果
     *
     * @param aiChatConversationQueryDTO 会话查询实体
     * @return 会话分页
     */
    public PageResult<AiChatConversationVO> selectAiChatConversationVOPage(AiChatConversationQueryDTO aiChatConversationQueryDTO);

    /**
     * 查询会话VO列表
     *
     * @param aiChatConversationQueryDTO 会话查询实体
     * @return 会话集合
     */
    public List<AiChatConversationVO> selectAiChatConversationVOList(AiChatConversationQueryDTO aiChatConversationQueryDTO);

    /**
     * 查询会话DO列表
     *
     * @param aiChatConversationQueryDTO 会话查询实体
     * @return 会话集合
     */
    public List<AiChatConversationDO> selectAiChatConversationDOList(AiChatConversationQueryDTO aiChatConversationQueryDTO);

    /**
     * 查询单个会话
     *
     * @param chatConversationId 会话主键
     * @return 会话单个
     */
    public AiChatConversationVO selectAiChatConversationVOByChatConversationId(Long chatConversationId);

    /**
     * 新增会话
     *
     * @param aiChatConversationEditDTO 会话编辑实体
     * @return 结果
     */
    public int insertAiChatConversation(AiChatConversationEditDTO aiChatConversationEditDTO);

    /**
     * 修改会话
     *
     * @param chatConversationId 主键
     * @param aiChatConversationEditDTO 会话编辑实体
     * @return 结果
     */
    public int updateAiChatConversation(Long chatConversationId, AiChatConversationEditDTO aiChatConversationEditDTO);

    /**
     * 批量删除会话
     *
     * @param chatConversationIds 会话主键集合
     * @return 结果
     */
    public int deleteAiChatConversationByChatConversationIds(List<Long> chatConversationIds);

    /**
     * 删除单个会话信息
     *
     * @param chatConversationId 会话主键
     * @return 结果
     */
    public int deleteAiChatConversationByChatConversationId(Long chatConversationId);
}
