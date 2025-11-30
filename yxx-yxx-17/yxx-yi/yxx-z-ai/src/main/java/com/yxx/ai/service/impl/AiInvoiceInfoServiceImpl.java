package com.yxx.ai.service.impl;

import java.util.List;
import com.yxx.common.core.domain.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import com.yxx.common.core.service.impl.ServiceImplPlus;
import com.yxx.ai.mapper.AiInvoiceInfoMapper;
import com.yxx.ai.entity.AiInvoiceInfoDO;
import com.yxx.ai.entity.AiInvoiceInfoVO;
import com.yxx.ai.entity.AiInvoiceInfoQueryDTO;
import com.yxx.ai.entity.AiInvoiceInfoEditDTO;
import com.yxx.ai.service.IAiInvoiceInfoService;

/**
 * 发票信息Service业务层处理
 *
 * @author yxx
 * @date 2025-11-30
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class AiInvoiceInfoServiceImpl extends ServiceImplPlus<AiInvoiceInfoMapper, AiInvoiceInfoDO> implements IAiInvoiceInfoService {

    private final IAiInvoiceInfoService self;

    private final AiInvoiceInfoMapper aiInvoiceInfoMapper;

    /**
     * 查询发票信息分页结果
     *
     * @param aiInvoiceInfoQueryDTO 发票信息查询实体
     * @return 发票信息分页
     */
    @Override
    public PageResult<AiInvoiceInfoVO> selectAiInvoiceInfoVOPage(AiInvoiceInfoQueryDTO aiInvoiceInfoQueryDTO) {
        startPage();
        PageResult<AiInvoiceInfoVO> page
                = super.getMyBatisPageResult(self.selectAiInvoiceInfoDOList(aiInvoiceInfoQueryDTO), AiInvoiceInfoVO.class);
        clearPage();
        return page;
    }

    /**
     * 查询发票信息VO列表
     *
     * @param aiInvoiceInfoQueryDTO 发票信息查询实体
     * @return 发票信息集合
     */
    @Override
    public List<AiInvoiceInfoVO> selectAiInvoiceInfoVOList(AiInvoiceInfoQueryDTO aiInvoiceInfoQueryDTO) {
        return super.convertList(aiInvoiceInfoMapper.selectAiInvoiceInfoList(aiInvoiceInfoQueryDTO), AiInvoiceInfoVO.class);
    }

    /**
     * 查询发票信息DO列表
     *
     * @param aiInvoiceInfoQueryDTO 发票信息查询实体
     * @return 发票信息集合
     */
    @Override
    public List<AiInvoiceInfoDO> selectAiInvoiceInfoDOList(AiInvoiceInfoQueryDTO aiInvoiceInfoQueryDTO) {
        return aiInvoiceInfoMapper.selectAiInvoiceInfoList(aiInvoiceInfoQueryDTO);
    }

    /**
     * 查询单个发票信息
     *
     * @param invoiceInfoId 发票信息主键
     * @return 发票信息单个
     */
    @Override
    public AiInvoiceInfoVO selectAiInvoiceInfoVOByInvoiceInfoId(Long invoiceInfoId) {
        return super.convertBean(aiInvoiceInfoMapper.selectAiInvoiceInfoByInvoiceInfoId(invoiceInfoId), AiInvoiceInfoVO.class);
    }

    /**
     * 新增发票信息
     *
     * @param aiInvoiceInfoEditDTO 发票信息编辑实体
     * @return 结果
     */
    @Override
    public int insertAiInvoiceInfo(AiInvoiceInfoEditDTO aiInvoiceInfoEditDTO) {
        aiInvoiceInfoEditDTO.fieldFillInsert();
        return aiInvoiceInfoMapper.insertAiInvoiceInfo(super.convertT(aiInvoiceInfoEditDTO));
    }

    /**
     * 修改发票信息
     *
     * @param invoiceInfoId 发票信息主键
     * @param aiInvoiceInfoEditDTO 发票信息编辑实体
     * @return 结果
     */
    @Override
    public int updateAiInvoiceInfo(Long invoiceInfoId, AiInvoiceInfoEditDTO aiInvoiceInfoEditDTO) {
        aiInvoiceInfoEditDTO.fieldFillUpdate();
        return aiInvoiceInfoMapper.updateAiInvoiceInfo(super.convertT(aiInvoiceInfoEditDTO).setInvoiceInfoId(invoiceInfoId));
    }

    /**
     * 批量删除发票信息
     *
     * @param invoiceInfoIds 需要删除的发票信息主键集合
     * @return 结果
     */
    @Override
    public int deleteAiInvoiceInfoByInvoiceInfoIds(List<Long> invoiceInfoIds) {
        return aiInvoiceInfoMapper.deleteAiInvoiceInfoByInvoiceInfoIds(invoiceInfoIds);
    }

    /**
     * 删除单个发票信息信息
     *
     * @param invoiceInfoId 发票信息主键
     * @return 结果
     */
    @Override
    public int deleteAiInvoiceInfoByInvoiceInfoId(Long invoiceInfoId) {
        return aiInvoiceInfoMapper.deleteAiInvoiceInfoByInvoiceInfoId(invoiceInfoId);
    }
}
