package com.yxx.ai.service;

import java.util.List;

import com.yxx.common.core.domain.PageResult;
import com.yxx.ai.entity.AiInvoiceInfoDO;
import com.yxx.ai.entity.AiInvoiceInfoVO;
import com.yxx.ai.entity.AiInvoiceInfoQueryDTO;
import com.yxx.ai.entity.AiInvoiceInfoEditDTO;
import com.yxx.common.core.service.IServicePlus;

/**
 * 发票信息Service接口
 *
 * @author yxx
 * @date 2026-01-27
 */
public interface IAiInvoiceInfoService extends IServicePlus<AiInvoiceInfoDO> {

    /**
     * 查询发票信息分页结果
     *
     * @param aiInvoiceInfoQueryDTO 发票信息查询实体
     * @return 发票信息分页
     */
    public PageResult<AiInvoiceInfoVO> selectAiInvoiceInfoVOPage(AiInvoiceInfoQueryDTO aiInvoiceInfoQueryDTO);

    /**
     * 查询发票信息VO列表
     *
     * @param aiInvoiceInfoQueryDTO 发票信息查询实体
     * @return 发票信息集合
     */
    public List<AiInvoiceInfoVO> selectAiInvoiceInfoVOList(AiInvoiceInfoQueryDTO aiInvoiceInfoQueryDTO);

    /**
     * 查询发票信息DO列表
     *
     * @param aiInvoiceInfoQueryDTO 发票信息查询实体
     * @return 发票信息集合
     */
    public List<AiInvoiceInfoDO> selectAiInvoiceInfoDOList(AiInvoiceInfoQueryDTO aiInvoiceInfoQueryDTO);

    /**
     * 查询单个发票信息
     *
     * @param invoiceInfoId 发票信息主键
     * @return 发票信息单个
     */
    public AiInvoiceInfoVO selectAiInvoiceInfoVOByInvoiceInfoId(Long invoiceInfoId);

    /**
     * 新增发票信息
     *
     * @param aiInvoiceInfoEditDTO 发票信息编辑实体
     * @return 结果
     */
    public int insertAiInvoiceInfo(AiInvoiceInfoEditDTO aiInvoiceInfoEditDTO);

    /**
     * 修改发票信息
     *
     * @param invoiceInfoId 主键
     * @param aiInvoiceInfoEditDTO 发票信息编辑实体
     * @return 结果
     */
    public int updateAiInvoiceInfo(Long invoiceInfoId, AiInvoiceInfoEditDTO aiInvoiceInfoEditDTO);

    /**
     * 批量删除发票信息
     *
     * @param invoiceInfoIds 发票信息主键集合
     * @return 结果
     */
    public int deleteAiInvoiceInfoByInvoiceInfoIds(List<Long> invoiceInfoIds);

    /**
     * 删除单个发票信息信息
     *
     * @param invoiceInfoId 发票信息主键
     * @return 结果
     */
    public int deleteAiInvoiceInfoByInvoiceInfoId(Long invoiceInfoId);
}
