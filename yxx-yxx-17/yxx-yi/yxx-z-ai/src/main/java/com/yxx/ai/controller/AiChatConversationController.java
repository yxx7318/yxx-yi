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
import com.yxx.ai.entity.AiChatConversationDO;
import com.yxx.ai.entity.AiChatConversationVO;
import com.yxx.ai.entity.AiChatConversationQueryDTO;
import com.yxx.ai.entity.AiChatConversationEditDTO;
import com.yxx.ai.service.IAiChatConversationService;
import com.yxx.common.utils.poi.ExcelUtil;
import com.yxx.common.core.domain.R;
import com.yxx.common.core.domain.PageResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;

import org.springframework.validation.annotation.Validated;


/**
 * 会话Controller
 *
 * @author yxx
 * @date 2026-01-21
 */
@Tag(name = "会话管理-AiChatConversation")
@RestController
@RequestMapping("/ai/chatConversation")
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class AiChatConversationController extends BaseControllerPlus {

    private final IAiChatConversationService aiChatConversationService;

    @Operation(summary = "查询--会话列表")
    @PreAuthorize("@ss.hasPermi('ai:chatConversation:list')")
    @GetMapping("/list")
    public PageResult<AiChatConversationVO> list(@ParameterObject AiChatConversationQueryDTO aiChatConversationQueryDTO) {
        return aiChatConversationService.selectAiChatConversationVOPage(aiChatConversationQueryDTO);
    }

    @Operation(summary = "查询--会话单个")
    @PreAuthorize("@ss.hasPermi('ai:chatConversation:query')")
    @GetMapping(value = "/{chatConversationId}")
    public R<AiChatConversationVO> getInfo(@PathVariable Long chatConversationId) {
        return R.ok(aiChatConversationService.selectAiChatConversationVOByChatConversationId(chatConversationId));
    }

    @Operation(summary = "新增--会话")
    @PreAuthorize("@ss.hasPermi('ai:chatConversation:add')")
    @Log(title = "会话", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@RequestBody @Validated AiChatConversationEditDTO aiChatConversationEditDTO) {
        return toResult(aiChatConversationService.insertAiChatConversation(aiChatConversationEditDTO));
    }

    @Operation(summary = "修改--会话")
    @PreAuthorize("@ss.hasPermi('ai:chatConversation:edit')")
    @Log(title = "会话", businessType = BusinessType.UPDATE)
    @PutMapping("/{chatConversationId}")
    public R<String> update(@PathVariable Long chatConversationId, @RequestBody @Validated AiChatConversationEditDTO aiChatConversationEditDTO) {
        return toResult(aiChatConversationService.updateAiChatConversation(chatConversationId, aiChatConversationEditDTO));
    }

    @Operation(summary = "删除--会话")
    @PreAuthorize("@ss.hasPermi('ai:chatConversation:remove')")
    @Log(title = "会话", businessType = BusinessType.DELETE)
    @DeleteMapping("/{chatConversationIds}")
    public R<String> delete(@PathVariable List<Long> chatConversationIds) {
        return toResult(aiChatConversationService.deleteAiChatConversationByChatConversationIds(chatConversationIds));
    }

    @Operation(summary = "导出--会话列表")
    @PreAuthorize("@ss.hasPermi('ai:chatConversation:export')")
    @Log(title = "会话", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @ParameterObject AiChatConversationQueryDTO aiChatConversationQueryDTO) {
        List<AiChatConversationDO> list = aiChatConversationService.selectAiChatConversationDOList(aiChatConversationQueryDTO);
        ExcelUtil<AiChatConversationDO> util = new ExcelUtil<>(AiChatConversationDO.class);
        util.exportExcel(response, list, "会话数据");
    }

    @Operation(summary = "导出--会话模板")
    @PreAuthorize("@ss.hasPermi('ai:chatConversation:import')")
    @PostMapping("/importTemplate")
    public void exportTemplate(HttpServletResponse response) {
        ExcelUtil<AiChatConversationDO> util = new ExcelUtil<>(AiChatConversationDO.class);
        util.importTemplateExcel(response, "导入会话模板数据");
    }

    @Operation(summary = "导入--会话列表")
    @PreAuthorize("@ss.hasPermi('ai:chatConversation:import')")
    @Log(title = "会话", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public R<Boolean> importData(MultipartFile file) throws IOException {
        ExcelUtil<AiChatConversationDO> util = new ExcelUtil<>(AiChatConversationDO.class);
        List<AiChatConversationDO> list = util.importExcel(file.getInputStream());
        return R.ok(aiChatConversationService.saveBatch(list), "成功导入 " + list.size() + " 条记录");
    }
}
