package com.yxx.ai.mapper;

import java.util.List;

import com.yxx.ai.entity.AiChatDetailDO;
import com.yxx.ai.entity.AiChatDetailQueryDTO;
import com.yxx.common.core.mapper.BaseMapperPlus;
import org.springframework.stereotype.Repository;

/**
 * 会话详细Mapper接口
 *
 * @author yxx
 * @date 2026-01-21
 */
@Repository
public interface AiChatDetailMapper extends BaseMapperPlus<AiChatDetailDO> {

    /**
     * 查询会话详细列表
     *
     * @param aiChatDetailQueryDTO 会话详细查询实体
     * @return 会话详细集合
     */
    public List<AiChatDetailDO> selectAiChatDetailList(AiChatDetailQueryDTO aiChatDetailQueryDTO);

    /**
     * 查询会话详细
     *
     * @param chatDetailId 会话详细主键
     * @return 会话详细单个
     */
    public AiChatDetailDO selectAiChatDetailByChatDetailId(Long chatDetailId);

    /**
     * 新增会话详细
     *
     * @param aiChatDetailDO 会话详细数据库实体
     * @return 结果
     */
    public int insertAiChatDetail(AiChatDetailDO aiChatDetailDO);

    /**
     * 修改会话详细
     *
     * @param aiChatDetailDO 会话详细数据库实体
     * @return 结果
     */
    public int updateAiChatDetail(AiChatDetailDO aiChatDetailDO);

    /**
     * 删除会话详细
     *
     * @param chatDetailId 会话详细主键
     * @return 结果
     */
    public int deleteAiChatDetailByChatDetailId(Long chatDetailId);

    /**
     * 批量删除会话详细
     *
     * @param chatDetailIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAiChatDetailByChatDetailIds(List<Long> chatDetailIds);
}
