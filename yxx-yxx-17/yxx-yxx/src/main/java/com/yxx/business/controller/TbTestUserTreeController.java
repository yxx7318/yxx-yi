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
import com.yxx.business.entity.TbTestUserTree;
import com.yxx.business.service.ITbTestUserTreeService;
import com.yxx.common.utils.poi.ExcelUtil;
import com.yxx.common.core.domain.R;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;

import org.springframework.validation.annotation.Validated;


/**
 * 测试树表生成Controller
 *
 * @author yxx
 * @date 2025-08-11
 */
@Tag(name = "测试树表生成管理-TbTestUserTree")
@RestController
@RequestMapping("/business/tree")
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class TbTestUserTreeController extends BaseControllerPlus {

    private final ITbTestUserTreeService tbTestUserTreeService;

    @Operation(summary = "查询--测试树表生成列表")
    @PreAuthorize("@ss.hasPermi('business:tree:list')")
    @GetMapping("/list")
    public R<List<TbTestUserTree>> list(@ParameterObject TbTestUserTree tbTestUserTree) {
        List<TbTestUserTree> list = tbTestUserTreeService.selectTbTestUserTreeList(tbTestUserTree);
        return R.ok(list);
    }

    @Operation(summary = "查询--测试树表生成单个")
    @PreAuthorize("@ss.hasPermi('business:tree:query')")
    @GetMapping(value = "/{userId}")
    public R<TbTestUserTree> getInfo(@PathVariable Long userId) {
        return R.ok(tbTestUserTreeService.selectTbTestUserTreeByUserId(userId));
    }

    @Operation(summary = "新增--测试树表生成")
    @PreAuthorize("@ss.hasPermi('business:tree:add')")
    @Log(title = "测试树表生成", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@RequestBody @Validated TbTestUserTree tbTestUserTree) {
        return toResult(tbTestUserTreeService.insertTbTestUserTree(tbTestUserTree));
    }

    @Operation(summary = "修改--测试树表生成")
    @PreAuthorize("@ss.hasPermi('business:tree:edit')")
    @Log(title = "测试树表生成", businessType = BusinessType.UPDATE)
    @PutMapping("/{userId}")
    public R<String> update(@PathVariable Long userId, @RequestBody @Validated TbTestUserTree tbTestUserTree) {
        return toResult(tbTestUserTreeService.updateTbTestUserTree(userId, tbTestUserTree));
    }

    @Operation(summary = "删除--测试树表生成")
    @PreAuthorize("@ss.hasPermi('business:tree:remove')")
    @Log(title = "测试树表生成", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public R<String> delete(@PathVariable List<Long> userIds) {
        return toResult(tbTestUserTreeService.deleteTbTestUserTreeByUserIds(userIds));
    }

    @Operation(summary = "导出--测试树表生成列表")
    @PreAuthorize("@ss.hasPermi('business:tree:export')")
    @Log(title = "测试树表生成", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @ParameterObject TbTestUserTree tbTestUserTree) {
        List<TbTestUserTree> list = tbTestUserTreeService.selectTbTestUserTreeList(tbTestUserTree);
        ExcelUtil<TbTestUserTree> util = new ExcelUtil<>(TbTestUserTree.class);
        util.exportExcel(response, list, "测试树表生成数据");
    }

    @Operation(summary = "导出--测试树表生成模板")
    @PreAuthorize("@ss.hasPermi('business:tree:import')")
    @PostMapping("/importTemplate")
    public void exportTemplate(HttpServletResponse response) {
        ExcelUtil<TbTestUserTree> util = new ExcelUtil<>(TbTestUserTree.class);
        util.importTemplateExcel(response, "导入测试树表生成模板数据");
    }

    @Operation(summary = "导入--测试树表生成列表")
    @PreAuthorize("@ss.hasPermi('business:tree:import')")
    @Log(title = "测试树表生成", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public R<Boolean> importData(MultipartFile file) throws IOException {
        ExcelUtil<TbTestUserTree> util = new ExcelUtil<>(TbTestUserTree.class);
        List<TbTestUserTree> list = util.importExcel(file.getInputStream());
        return R.ok(tbTestUserTreeService.saveBatch(list), "成功导入 " + list.size() + " 条记录");
    }
}
