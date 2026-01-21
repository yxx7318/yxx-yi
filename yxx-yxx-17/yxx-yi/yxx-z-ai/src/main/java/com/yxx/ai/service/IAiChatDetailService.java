package com.yxx.ai.service;

import java.util.List;

import com.yxx.common.core.domain.PageResult;
import com.yxx.ai.entity.AiChatDetailDO;
import com.yxx.ai.entity.AiChatDetailVO;
import com.yxx.ai.entity.AiChatDetailQueryDTO;
import com.yxx.ai.entity.AiChatDetailEditDTO;
import com.yxx.common.core.service.IServicePlus;

/**
 * 会话详细Service接口
 *
 * @author yxx
 * @date 2026-01-21
 */
public interface IAiChatDetailService extends IServicePlus<AiChatDetailDO> {

    /**
     * 查询会话详细分页结果
     *
     * @param aiChatDetailQueryDTO 会话详细查询实体
     * @return 会话详细分页
     */
    public PageResult<AiChatDetailVO> selectAiChatDetailVOPage(AiChatDetailQueryDTO aiChatDetailQueryDTO);

    /**
     * 查询会话详细VO列表
     *
     * @param aiChatDetailQueryDTO 会话详细查询实体
     * @return 会话详细集合
     */
    public List<AiChatDetailVO> selectAiChatDetailVOList(AiChatDetailQueryDTO aiChatDetailQueryDTO);

    /**
     * 查询会话详细DO列表
     *
     * @param aiChatDetailQueryDTO 会话详细查询实体
     * @return 会话详细集合
     */
    public List<AiChatDetailDO> selectAiChatDetailDOList(AiChatDetailQueryDTO aiChatDetailQueryDTO);

    /**
     * 查询单个会话详细
     *
     * @param chatDetailId 会话详细主键
     * @return 会话详细单个
     */
    public AiChatDetailVO selectAiChatDetailVOByChatDetailId(Long chatDetailId);

    /**
     * 新增会话详细
     *
     * @param aiChatDetailEditDTO 会话详细编辑实体
     * @return 结果
     */
    public int insertAiChatDetail(AiChatDetailEditDTO aiChatDetailEditDTO);

    /**
     * 修改会话详细
     *
     * @param chatDetailId 主键
     * @param aiChatDetailEditDTO 会话详细编辑实体
     * @return 结果
     */
    public int updateAiChatDetail(Long chatDetailId, AiChatDetailEditDTO aiChatDetailEditDTO);

    /**
     * 批量删除会话详细
     *
     * @param chatDetailIds 会话详细主键集合
     * @return 结果
     */
    public int deleteAiChatDetailByChatDetailIds(List<Long> chatDetailIds);

    /**
     * 删除单个会话详细信息
     *
     * @param chatDetailId 会话详细主键
     * @return 结果
     */
    public int deleteAiChatDetailByChatDetailId(Long chatDetailId);
}
