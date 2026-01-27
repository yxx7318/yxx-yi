package com.yxx.ai.mapper;

import java.util.List;

import com.yxx.ai.entity.AiInvoiceInfoDO;
import com.yxx.ai.entity.AiInvoiceInfoQueryDTO;
import com.yxx.common.core.mapper.BaseMapperPlus;
import org.springframework.stereotype.Repository;

/**
 * 发票信息Mapper接口
 *
 * @author yxx
 * @date 2026-01-27
 */
@Repository
public interface AiInvoiceInfoMapper extends BaseMapperPlus<AiInvoiceInfoDO> {

    /**
     * 查询发票信息列表
     *
     * @param aiInvoiceInfoQueryDTO 发票信息查询实体
     * @return 发票信息集合
     */
    public List<AiInvoiceInfoDO> selectAiInvoiceInfoList(AiInvoiceInfoQueryDTO aiInvoiceInfoQueryDTO);

    /**
     * 查询发票信息
     *
     * @param invoiceInfoId 发票信息主键
     * @return 发票信息单个
     */
    public AiInvoiceInfoDO selectAiInvoiceInfoByInvoiceInfoId(Long invoiceInfoId);

    /**
     * 新增发票信息
     *
     * @param aiInvoiceInfoDO 发票信息数据库实体
     * @return 结果
     */
    public int insertAiInvoiceInfo(AiInvoiceInfoDO aiInvoiceInfoDO);

    /**
     * 修改发票信息
     *
     * @param aiInvoiceInfoDO 发票信息数据库实体
     * @return 结果
     */
    public int updateAiInvoiceInfo(AiInvoiceInfoDO aiInvoiceInfoDO);

    /**
     * 删除发票信息
     *
     * @param invoiceInfoId 发票信息主键
     * @return 结果
     */
    public int deleteAiInvoiceInfoByInvoiceInfoId(Long invoiceInfoId);

    /**
     * 批量删除发票信息
     *
     * @param invoiceInfoIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAiInvoiceInfoByInvoiceInfoIds(List<Long> invoiceInfoIds);
}
