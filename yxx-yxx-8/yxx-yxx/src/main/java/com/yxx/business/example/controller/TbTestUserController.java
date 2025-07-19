package com.yxx.business.example.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import com.yxx.common.annotation.Log;
import com.yxx.common.core.controller.BaseControllerPlus;
import com.yxx.common.enums.BusinessType;
import com.yxx.business.example.domain.TbTestUserDo;
import com.yxx.business.example.domain.TbTestUserVo;
import com.yxx.business.example.domain.TbTestUserQueryDto;
import com.yxx.business.example.domain.TbTestUserEditDto;
import com.yxx.business.example.service.ITbTestUserService;
import com.yxx.common.utils.poi.ExcelUtil;
import com.yxx.common.core.domain.R;
import com.yxx.common.core.domain.PageResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.api.annotations.ParameterObject;

import org.springframework.validation.annotation.Validated;


/**
 * 测试用户Controller
 *
 * @author yxx
 * @date 2025-07-17
 */
@Tag(name = "测试用户管理-TbTestUser")
@RestController
@RequestMapping("/business/user")
@RequiredArgsConstructor
public class TbTestUserController extends BaseControllerPlus {

    private final ITbTestUserService tbTestUserService;

    @Operation(summary = "查询--测试用户列表")
    @PreAuthorize("@ss.hasPermi('business:user:list')")
    @GetMapping("/list")
    public PageResult<TbTestUserVo> list(TbTestUserQueryDto tbTestUserQueryDto) {
        return tbTestUserService.selectTbTestUserPage(tbTestUserQueryDto);
    }

    @Operation(summary = "查询--测试用户单个")
    @PreAuthorize("@ss.hasPermi('business:user:query')")
    @GetMapping(value = "/{userId}")
    public R<TbTestUserVo> getInfo(@PathVariable Long userId) {
        return R.ok(tbTestUserService.selectTbTestUserByUserId(userId));
    }

    @Operation(summary = "新增--测试用户")
    @PreAuthorize("@ss.hasPermi('business:user:add')")
    @Log(title = "测试用户", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@RequestBody @Validated TbTestUserEditDto tbTestUserEditDto) {
        return toResult(tbTestUserService.insertTbTestUser(tbTestUserEditDto));
    }

    @Operation(summary = "修改--测试用户")
    @PreAuthorize("@ss.hasPermi('business:user:edit')")
    @Log(title = "测试用户", businessType = BusinessType.UPDATE)
    @PutMapping("/{userId}")
    public R<String> update(@PathVariable Long userId, @RequestBody @Validated TbTestUserEditDto tbTestUserEditDto) {
        return toResult(tbTestUserService.updateTbTestUser(userId, tbTestUserEditDto));
    }

    @Operation(summary = "删除--测试用户")
    @PreAuthorize("@ss.hasPermi('business:user:remove')")
    @Log(title = "测试用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public R<String> delete(@PathVariable List<Long> userIds) {
        return toResult(tbTestUserService.deleteTbTestUserByUserIds(userIds));
    }

    @Operation(summary = "导出--测试用户列表")
    @PreAuthorize("@ss.hasPermi('business:user:export')")
    @Log(title = "测试用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @ParameterObject TbTestUserQueryDto tbTestUserQueryDto) {
        List<TbTestUserDo> list = tbTestUserService.selectTbTestUserDoList(tbTestUserQueryDto);
        ExcelUtil<TbTestUserDo> util = new ExcelUtil<>(TbTestUserDo.class);
        util.exportExcel(response, list, "测试用户数据");
    }

    @Operation(summary = "导出--测试用户模板")
    @PreAuthorize("@ss.hasPermi('business:user:import')")
    @PostMapping("/importTemplate")
    public void exportTemplate(HttpServletResponse response) {
        ExcelUtil<TbTestUserDo> util = new ExcelUtil<>(TbTestUserDo.class);
        util.importTemplateExcel(response, "导入测试用户模板数据");
    }

    @Operation(summary = "导入--测试用户列表")
    @PreAuthorize("@ss.hasPermi('business:user:import')")
    @Log(title = "测试用户", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public R<Boolean> importData(MultipartFile file) throws IOException {
        ExcelUtil<TbTestUserDo> util = new ExcelUtil<>(TbTestUserDo.class);
        List<TbTestUserDo> list = util.importExcel(file.getInputStream());
        return R.ok(tbTestUserService.saveBatch(list), "成功导入 " + list.size() + " 条记录");
    }
}
