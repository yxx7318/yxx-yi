package com.yxx.ai.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import com.yxx.common.annotation.Log;
import com.yxx.common.core.controller.BaseControllerPlus;
import com.yxx.common.enums.BusinessType;
import com.yxx.ai.entity.AiInvoiceInfoDO;
import com.yxx.ai.entity.AiInvoiceInfoVO;
import com.yxx.ai.entity.AiInvoiceInfoQueryDTO;
import com.yxx.ai.entity.AiInvoiceInfoEditDTO;
import com.yxx.ai.service.IAiInvoiceInfoService;
import com.yxx.common.utils.poi.ExcelUtil;
import com.yxx.common.core.domain.R;
import com.yxx.common.core.domain.PageResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;

import org.springframework.validation.annotation.Validated;


/**
 * 发票信息Controller
 *
 * @author yxx
 * @date 2025-11-30
 */
@Tag(name = "发票信息管理-AiInvoiceInfo")
@RestController
@RequestMapping("/ai/invoiceInfo")
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class AiInvoiceInfoController extends BaseControllerPlus {

    private final IAiInvoiceInfoService aiInvoiceInfoService;

    @Operation(summary = "查询--发票信息列表")
    @PreAuthorize("@ss.hasPermi('ai:invoiceInfo:list')")
    @GetMapping("/list")
    public PageResult<AiInvoiceInfoVO> list(@ParameterObject AiInvoiceInfoQueryDTO aiInvoiceInfoQueryDTO) {
        return aiInvoiceInfoService.selectAiInvoiceInfoVOPage(aiInvoiceInfoQueryDTO);
    }

    @Operation(summary = "查询--发票信息单个")
    @PreAuthorize("@ss.hasPermi('ai:invoiceInfo:query')")
    @GetMapping(value = "/{invoiceInfoId}")
    public R<AiInvoiceInfoVO> getInfo(@PathVariable Long invoiceInfoId) {
        return R.ok(aiInvoiceInfoService.selectAiInvoiceInfoVOByInvoiceInfoId(invoiceInfoId));
    }

    @Operation(summary = "新增--发票信息")
    @PreAuthorize("@ss.hasPermi('ai:invoiceInfo:add')")
    @Log(title = "发票信息", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@RequestBody @Validated AiInvoiceInfoEditDTO aiInvoiceInfoEditDTO) {
        return toResult(aiInvoiceInfoService.insertAiInvoiceInfo(aiInvoiceInfoEditDTO));
    }

    @Operation(summary = "修改--发票信息")
    @PreAuthorize("@ss.hasPermi('ai:invoiceInfo:edit')")
    @Log(title = "发票信息", businessType = BusinessType.UPDATE)
    @PutMapping("/{invoiceInfoId}")
    public R<String> update(@PathVariable Long invoiceInfoId, @RequestBody @Validated AiInvoiceInfoEditDTO aiInvoiceInfoEditDTO) {
        return toResult(aiInvoiceInfoService.updateAiInvoiceInfo(invoiceInfoId, aiInvoiceInfoEditDTO));
    }

    @Operation(summary = "删除--发票信息")
    @PreAuthorize("@ss.hasPermi('ai:invoiceInfo:remove')")
    @Log(title = "发票信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{invoiceInfoIds}")
    public R<String> delete(@PathVariable List<Long> invoiceInfoIds) {
        return toResult(aiInvoiceInfoService.deleteAiInvoiceInfoByInvoiceInfoIds(invoiceInfoIds));
    }

    @Operation(summary = "导出--发票信息列表")
    @PreAuthorize("@ss.hasPermi('ai:invoiceInfo:export')")
    @Log(title = "发票信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @ParameterObject AiInvoiceInfoQueryDTO aiInvoiceInfoQueryDTO) {
        List<AiInvoiceInfoDO> list = aiInvoiceInfoService.selectAiInvoiceInfoDOList(aiInvoiceInfoQueryDTO);
        ExcelUtil<AiInvoiceInfoDO> util = new ExcelUtil<>(AiInvoiceInfoDO.class);
        util.exportExcel(response, list, "发票信息数据");
    }

    @Operation(summary = "导出--发票信息模板")
    @PreAuthorize("@ss.hasPermi('ai:invoiceInfo:import')")
    @PostMapping("/importTemplate")
    public void exportTemplate(HttpServletResponse response) {
        ExcelUtil<AiInvoiceInfoDO> util = new ExcelUtil<>(AiInvoiceInfoDO.class);
        util.importTemplateExcel(response, "导入发票信息模板数据");
    }

    @Operation(summary = "导入--发票信息列表")
    @PreAuthorize("@ss.hasPermi('ai:invoiceInfo:import')")
    @Log(title = "发票信息", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public R<Boolean> importData(MultipartFile file) throws IOException {
        ExcelUtil<AiInvoiceInfoDO> util = new ExcelUtil<>(AiInvoiceInfoDO.class);
        List<AiInvoiceInfoDO> list = util.importExcel(file.getInputStream());
        return R.ok(aiInvoiceInfoService.saveBatch(list), "成功导入 " + list.size() + " 条记录");
    }
}
