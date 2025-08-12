package com.yxx.business.controller;

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
import com.yxx.business.entity.TbTestUserSub;
import com.yxx.business.service.ITbTestUserSubService;
import com.yxx.common.utils.poi.ExcelUtil;
import com.yxx.common.core.domain.R;
import com.yxx.common.core.domain.PageResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;

import org.springframework.validation.annotation.Validated;


/**
 * 测试主表生成Controller
 *
 * @author yxx
 * @date 2025-08-11
 */
@Tag(name = "测试主表生成管理-TbTestUserSub")
@RestController
@RequestMapping("/business/sub")
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class TbTestUserSubController extends BaseControllerPlus {

    private final ITbTestUserSubService tbTestUserSubService;

    @Operation(summary = "查询--测试主表生成列表")
    @PreAuthorize("@ss.hasPermi('business:sub:list')")
    @GetMapping("/list")
    public PageResult<TbTestUserSub> list(@ParameterObject TbTestUserSub tbTestUserSub) {
        return tbTestUserSubService.selectTbTestUserSubPage(tbTestUserSub);
    }

    @Operation(summary = "查询--测试主表生成单个")
    @PreAuthorize("@ss.hasPermi('business:sub:query')")
    @GetMapping(value = "/{subId}")
    public R<TbTestUserSub> getInfo(@PathVariable Long subId) {
        return R.ok(tbTestUserSubService.selectTbTestUserSubBySubId(subId));
    }

    @Operation(summary = "新增--测试主表生成")
    @PreAuthorize("@ss.hasPermi('business:sub:add')")
    @Log(title = "测试主表生成", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@RequestBody @Validated TbTestUserSub tbTestUserSub) {
        return toResult(tbTestUserSubService.insertTbTestUserSub(tbTestUserSub));
    }

    @Operation(summary = "修改--测试主表生成")
    @PreAuthorize("@ss.hasPermi('business:sub:edit')")
    @Log(title = "测试主表生成", businessType = BusinessType.UPDATE)
    @PutMapping("/{subId}")
    public R<String> update(@PathVariable Long subId, @RequestBody @Validated TbTestUserSub tbTestUserSub) {
        return toResult(tbTestUserSubService.updateTbTestUserSub(subId, tbTestUserSub));
    }

    @Operation(summary = "删除--测试主表生成")
    @PreAuthorize("@ss.hasPermi('business:sub:remove')")
    @Log(title = "测试主表生成", businessType = BusinessType.DELETE)
    @DeleteMapping("/{subIds}")
    public R<String> delete(@PathVariable List<Long> subIds) {
        return toResult(tbTestUserSubService.deleteTbTestUserSubBySubIds(subIds));
    }

    @Operation(summary = "导出--测试主表生成列表")
    @PreAuthorize("@ss.hasPermi('business:sub:export')")
    @Log(title = "测试主表生成", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @ParameterObject TbTestUserSub tbTestUserSub) {
        List<TbTestUserSub> list = tbTestUserSubService.selectTbTestUserSubList(tbTestUserSub);
        ExcelUtil<TbTestUserSub> util = new ExcelUtil<>(TbTestUserSub.class);
        util.exportExcel(response, list, "测试主表生成数据");
    }

    @Operation(summary = "导出--测试主表生成模板")
    @PreAuthorize("@ss.hasPermi('business:sub:import')")
    @PostMapping("/importTemplate")
    public void exportTemplate(HttpServletResponse response) {
        ExcelUtil<TbTestUserSub> util = new ExcelUtil<>(TbTestUserSub.class);
        util.importTemplateExcel(response, "导入测试主表生成模板数据");
    }

    @Operation(summary = "导入--测试主表生成列表")
    @PreAuthorize("@ss.hasPermi('business:sub:import')")
    @Log(title = "测试主表生成", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public R<Boolean> importData(MultipartFile file) throws IOException {
        ExcelUtil<TbTestUserSub> util = new ExcelUtil<>(TbTestUserSub.class);
        List<TbTestUserSub> list = util.importExcel(file.getInputStream());
        return R.ok(tbTestUserSubService.saveBatch(list), "成功导入 " + list.size() + " 条记录");
    }
}
