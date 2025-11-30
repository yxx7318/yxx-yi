package com.yxx.ai.config.tools;

import com.yxx.ai.domain.tools.InvoiceToolEditDTO;
import com.yxx.ai.domain.tools.InvoiceToolQueryDTO;
import com.yxx.ai.entity.AiInvoiceInfoDO;
import com.yxx.ai.entity.AiInvoiceInfoEditDTO;
import com.yxx.ai.entity.AiInvoiceInfoQueryDTO;
import com.yxx.ai.service.IAiInvoiceInfoService;
import com.yxx.common.utils.bean.BeanUtils;
import com.yxx.common.utils.spring.SpringUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 发票助手AI FunctionCalling
 */
@Component
public class InvoiceTool {

    @Autowired
    private IAiInvoiceInfoService aiInvoiceInfoService = SpringUtils.getBean(IAiInvoiceInfoService.class);

    @Tool(description = "根据条件查询发票信息")
    public List<AiInvoiceInfoDO> queryInvoiceInfo(@ToolParam(description = "发票查询条件") InvoiceToolQueryDTO queryDTO) {
        AiInvoiceInfoQueryDTO aiInvoiceInfoQueryDTO = BeanUtils.convertBean(queryDTO, AiInvoiceInfoQueryDTO.class);
        aiInvoiceInfoQueryDTO.getParams().put("beginCreateTime", queryDTO.getBeginCreateTime());
        aiInvoiceInfoQueryDTO.getParams().put("endCreateTime", queryDTO.getEndCreateTime());
        aiInvoiceInfoQueryDTO.setOrderByColumn(queryDTO.getSort().getOrderByColumn());
        aiInvoiceInfoQueryDTO.setIsAsc(queryDTO.getSort().getIsAsc());
        return aiInvoiceInfoService.selectAiInvoiceInfoDOList(aiInvoiceInfoQueryDTO);
    }

    @Tool(description = "根据条件新增发票信息")
    public Boolean addInvoiceInfo(@ToolParam(description = "发票新增条件") InvoiceToolEditDTO editDTO) {
        AiInvoiceInfoEditDTO aiInvoiceInfoEditDTO = BeanUtils.convertBean(editDTO, AiInvoiceInfoEditDTO.class);
        return aiInvoiceInfoService.insertAiInvoiceInfo(aiInvoiceInfoEditDTO) > 0;
    }
}