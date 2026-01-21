package com.yxx.ai.service.impl;

import java.util.List;
import com.yxx.common.core.domain.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import com.yxx.common.core.service.impl.ServiceImplPlus;
import com.yxx.ai.mapper.AiChatConversationMapper;
import com.yxx.ai.entity.AiChatConversationDO;
import com.yxx.ai.entity.AiChatConversationVO;
import com.yxx.ai.entity.AiChatConversationQueryDTO;
import com.yxx.ai.entity.AiChatConversationEditDTO;
import com.yxx.ai.service.IAiChatConversationService;

/**
 * 会话Service业务层处理
 *
 * @author yxx
 * @date 2026-01-21
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class AiChatConversationServiceImpl extends ServiceImplPlus<AiChatConversationMapper, AiChatConversationDO> implements IAiChatConversationService {

    private final IAiChatConversationService self;

    private final AiChatConversationMapper aiChatConversationMapper;

    /**
     * 查询会话分页结果
     *
     * @param aiChatConversationQueryDTO 会话查询实体
     * @return 会话分页
     */
    @Override
    public PageResult<AiChatConversationVO> selectAiChatConversationVOPage(AiChatConversationQueryDTO aiChatConversationQueryDTO) {
        startPage();
        PageResult<AiChatConversationVO> page
                = super.getMyBatisPageResult(self.selectAiChatConversationDOList(aiChatConversationQueryDTO), AiChatConversationVO.class);
        clearPage();
        return page;
    }

    /**
     * 查询会话VO列表
     *
     * @param aiChatConversationQueryDTO 会话查询实体
     * @return 会话集合
     */
    @Override
    public List<AiChatConversationVO> selectAiChatConversationVOList(AiChatConversationQueryDTO aiChatConversationQueryDTO) {
        return super.convertList(aiChatConversationMapper.selectAiChatConversationList(aiChatConversationQueryDTO), AiChatConversationVO.class);
    }

    /**
     * 查询会话DO列表
     *
     * @param aiChatConversationQueryDTO 会话查询实体
     * @return 会话集合
     */
    @Override
    public List<AiChatConversationDO> selectAiChatConversationDOList(AiChatConversationQueryDTO aiChatConversationQueryDTO) {
        return aiChatConversationMapper.selectAiChatConversationList(aiChatConversationQueryDTO);
    }

    /**
     * 查询单个会话
     *
     * @param chatConversationId 会话主键
     * @return 会话单个
     */
    @Override
    public AiChatConversationVO selectAiChatConversationVOByChatConversationId(Long chatConversationId) {
        return super.convertBean(aiChatConversationMapper.selectAiChatConversationByChatConversationId(chatConversationId), AiChatConversationVO.class);
    }

    /**
     * 新增会话
     *
     * @param aiChatConversationEditDTO 会话编辑实体
     * @return 结果
     */
    @Override
    public int insertAiChatConversation(AiChatConversationEditDTO aiChatConversationEditDTO) {
        aiChatConversationEditDTO.fieldFillInsert();
        return aiChatConversationMapper.insertAiChatConversation(super.convertT(aiChatConversationEditDTO));
    }

    /**
     * 修改会话
     *
     * @param chatConversationId 会话主键
     * @param aiChatConversationEditDTO 会话编辑实体
     * @return 结果
     */
    @Override
    public int updateAiChatConversation(Long chatConversationId, AiChatConversationEditDTO aiChatConversationEditDTO) {
        aiChatConversationEditDTO.fieldFillUpdate();
        return aiChatConversationMapper.updateAiChatConversation(super.convertT(aiChatConversationEditDTO).setChatConversationId(chatConversationId));
    }

    /**
     * 批量删除会话
     *
     * @param chatConversationIds 需要删除的会话主键集合
     * @return 结果
     */
    @Override
    public int deleteAiChatConversationByChatConversationIds(List<Long> chatConversationIds) {
        return aiChatConversationMapper.deleteAiChatConversationByChatConversationIds(chatConversationIds);
    }

    /**
     * 删除单个会话信息
     *
     * @param chatConversationId 会话主键
     * @return 结果
     */
    @Override
    public int deleteAiChatConversationByChatConversationId(Long chatConversationId) {
        return aiChatConversationMapper.deleteAiChatConversationByChatConversationId(chatConversationId);
    }
}
