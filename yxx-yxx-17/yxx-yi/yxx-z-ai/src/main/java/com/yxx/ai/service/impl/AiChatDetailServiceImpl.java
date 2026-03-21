package com.yxx.ai.service.impl;

import java.util.List;
import com.yxx.common.core.domain.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import com.yxx.common.core.service.impl.ServiceImplPlus;
import com.yxx.ai.mapper.AiChatDetailMapper;
import com.yxx.ai.entity.AiChatDetailDO;
import com.yxx.ai.entity.AiChatDetailVO;
import com.yxx.ai.entity.AiChatDetailQueryDTO;
import com.yxx.ai.entity.AiChatDetailEditDTO;
import com.yxx.ai.service.IAiChatDetailService;

/**
 * 会话详细Service业务层处理
 *
 * @author yxx
 * @date 2026-01-21
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class AiChatDetailServiceImpl extends ServiceImplPlus<AiChatDetailMapper, AiChatDetailDO> implements IAiChatDetailService {

    private final IAiChatDetailService self;

    private final AiChatDetailMapper aiChatDetailMapper;

    /**
     * 查询会话详细分页结果
     *
     * @param aiChatDetailQueryDTO 会话详细查询实体
     * @return 会话详细分页
     */
    @Override
    public PageResult<AiChatDetailVO> selectAiChatDetailVOPage(AiChatDetailQueryDTO aiChatDetailQueryDTO) {
        startPage();
        PageResult<AiChatDetailVO> page
                = super.getMyBatisPageResult(self.selectAiChatDetailDOList(aiChatDetailQueryDTO), AiChatDetailVO.class);
        clearPage();
        return page;
    }

    /**
     * 查询会话详细VO列表
     *
     * @param aiChatDetailQueryDTO 会话详细查询实体
     * @return 会话详细集合
     */
    @Override
    public List<AiChatDetailVO> selectAiChatDetailVOList(AiChatDetailQueryDTO aiChatDetailQueryDTO) {
        return super.convertList(aiChatDetailMapper.selectAiChatDetailList(aiChatDetailQueryDTO), AiChatDetailVO.class);
    }

    /**
     * 查询会话详细DO列表
     *
     * @param aiChatDetailQueryDTO 会话详细查询实体
     * @return 会话详细集合
     */
    @Override
    public List<AiChatDetailDO> selectAiChatDetailDOList(AiChatDetailQueryDTO aiChatDetailQueryDTO) {
        return aiChatDetailMapper.selectAiChatDetailList(aiChatDetailQueryDTO);
    }

    /**
     * 查询单个会话详细
     *
     * @param chatDetailId 会话详细主键
     * @return 会话详细单个
     */
    @Override
    public AiChatDetailVO selectAiChatDetailVOByChatDetailId(Long chatDetailId) {
        return super.convertBean(aiChatDetailMapper.selectAiChatDetailByChatDetailId(chatDetailId), AiChatDetailVO.class);
    }

    /**
     * 新增会话详细
     *
     * @param aiChatDetailEditDTO 会话详细编辑实体
     * @return 结果
     */
    @Override
    public int insertAiChatDetail(AiChatDetailEditDTO aiChatDetailEditDTO) {
        aiChatDetailEditDTO.fieldFillInsert();
        return aiChatDetailMapper.insertAiChatDetail(super.convertT(aiChatDetailEditDTO));
    }

    /**
     * 修改会话详细
     *
     * @param chatDetailId 会话详细主键
     * @param aiChatDetailEditDTO 会话详细编辑实体
     * @return 结果
     */
    @Override
    public int updateAiChatDetail(Long chatDetailId, AiChatDetailEditDTO aiChatDetailEditDTO) {
        aiChatDetailEditDTO.fieldFillUpdate();
        return aiChatDetailMapper.updateAiChatDetail(super.convertT(aiChatDetailEditDTO).setChatDetailId(chatDetailId));
    }

    /**
     * 批量删除会话详细
     *
     * @param chatDetailIds 需要删除的会话详细主键集合
     * @return 结果
     */
    @Override
    public int deleteAiChatDetailByChatDetailIds(List<Long> chatDetailIds) {
        return aiChatDetailMapper.deleteAiChatDetailByChatDetailIds(chatDetailIds);
    }

    /**
     * 删除单个会话详细信息
     *
     * @param chatDetailId 会话详细主键
     * @return 结果
     */
    @Override
    public int deleteAiChatDetailByChatDetailId(Long chatDetailId) {
        return aiChatDetailMapper.deleteAiChatDetailByChatDetailId(chatDetailId);
    }
}
