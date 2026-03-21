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
import com.yxx.ai.entity.AiChatDetailDO;
import com.yxx.ai.entity.AiChatDetailVO;
import com.yxx.ai.entity.AiChatDetailQueryDTO;
import com.yxx.ai.entity.AiChatDetailEditDTO;
import com.yxx.ai.service.IAiChatDetailService;
import com.yxx.common.utils.poi.ExcelUtil;
import com.yxx.common.core.domain.R;
import com.yxx.common.core.domain.PageResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;

import org.springframework.validation.annotation.Validated;


/**
 * 会话详细Controller
 *
 * @author yxx
 * @date 2026-01-21
 */
@Tag(name = "会话详细管理-AiChatDetail")
@RestController
@RequestMapping("/ai/chatDetail")
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class AiChatDetailController extends BaseControllerPlus {

    private final IAiChatDetailService aiChatDetailService;

    @Operation(summary = "查询--会话详细列表")
    @PreAuthorize("@ss.hasPermi('ai:chatDetail:list')")
    @GetMapping("/list")
    public PageResult<AiChatDetailVO> list(@ParameterObject AiChatDetailQueryDTO aiChatDetailQueryDTO) {
        return aiChatDetailService.selectAiChatDetailVOPage(aiChatDetailQueryDTO);
    }

    @Operation(summary = "查询--会话详细单个")
    @PreAuthorize("@ss.hasPermi('ai:chatDetail:query')")
    @GetMapping(value = "/{chatDetailId}")
    public R<AiChatDetailVO> getInfo(@PathVariable Long chatDetailId) {
        return R.ok(aiChatDetailService.selectAiChatDetailVOByChatDetailId(chatDetailId));
    }

    @Operation(summary = "新增--会话详细")
    @PreAuthorize("@ss.hasPermi('ai:chatDetail:add')")
    @Log(title = "会话详细", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@RequestBody @Validated AiChatDetailEditDTO aiChatDetailEditDTO) {
        return toResult(aiChatDetailService.insertAiChatDetail(aiChatDetailEditDTO));
    }

    @Operation(summary = "修改--会话详细")
    @PreAuthorize("@ss.hasPermi('ai:chatDetail:edit')")
    @Log(title = "会话详细", businessType = BusinessType.UPDATE)
    @PutMapping("/{chatDetailId}")
    public R<String> update(@PathVariable Long chatDetailId, @RequestBody @Validated AiChatDetailEditDTO aiChatDetailEditDTO) {
        return toResult(aiChatDetailService.updateAiChatDetail(chatDetailId, aiChatDetailEditDTO));
    }

    @Operation(summary = "删除--会话详细")
    @PreAuthorize("@ss.hasPermi('ai:chatDetail:remove')")
    @Log(title = "会话详细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{chatDetailIds}")
    public R<String> delete(@PathVariable List<Long> chatDetailIds) {
        return toResult(aiChatDetailService.deleteAiChatDetailByChatDetailIds(chatDetailIds));
    }

    @Operation(summary = "导出--会话详细列表")
    @PreAuthorize("@ss.hasPermi('ai:chatDetail:export')")
    @Log(title = "会话详细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @ParameterObject AiChatDetailQueryDTO aiChatDetailQueryDTO) {
        List<AiChatDetailDO> list = aiChatDetailService.selectAiChatDetailDOList(aiChatDetailQueryDTO);
        ExcelUtil<AiChatDetailDO> util = new ExcelUtil<>(AiChatDetailDO.class);
        util.exportExcel(response, list, "会话详细数据");
    }

    @Operation(summary = "导出--会话详细模板")
    @PreAuthorize("@ss.hasPermi('ai:chatDetail:import')")
    @PostMapping("/importTemplate")
    public void exportTemplate(HttpServletResponse response) {
        ExcelUtil<AiChatDetailDO> util = new ExcelUtil<>(AiChatDetailDO.class);
        util.importTemplateExcel(response, "导入会话详细模板数据");
    }

    @Operation(summary = "导入--会话详细列表")
    @PreAuthorize("@ss.hasPermi('ai:chatDetail:import')")
    @Log(title = "会话详细", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public R<Boolean> importData(MultipartFile file) throws IOException {
        ExcelUtil<AiChatDetailDO> util = new ExcelUtil<>(AiChatDetailDO.class);
        List<AiChatDetailDO> list = util.importExcel(file.getInputStream());
        return R.ok(aiChatDetailService.saveBatch(list), "成功导入 " + list.size() + " 条记录");
    }
}
