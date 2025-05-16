package com.yxx.business.example.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.yxx.common.annotation.Log;
import com.yxx.common.core.controller.BaseControllerPlus;
import com.yxx.common.enums.BusinessType;
import com.yxx.business.example.domain.TbTestUser;
import com.yxx.business.example.service.ITbTestUserService;
import com.yxx.common.utils.poi.ExcelUtil;
import com.yxx.common.core.domain.R;
import com.yxx.common.core.domain.PageResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

import org.springframework.validation.annotation.Validated;


/**
 * 测试用户Controller
 *
 * @author yxx
 * @date 2025-05-13
 */
@Tag(name = "测试用户管理-TbTestUser)")
@RestController
@RequestMapping("/business/user")
@RequiredArgsConstructor
public class TbTestUserController extends BaseControllerPlus {

    private final ITbTestUserService tbTestUserService;

    @Operation(summary = "查询--测试用户列表")
    @PreAuthorize("@ss.hasPermi('business:user:list')")
    @GetMapping("/list")
    public PageResult<?> list(@Parameter(description = "DTO对象", in = ParameterIn.QUERY) TbTestUser tbTestUser) {
        return tbTestUserService.selectTbTestUserPage(tbTestUser);
    }

    @Operation(summary = "查询--测试用户单个详细")
    @PreAuthorize("@ss.hasPermi('business:user:query')")
    @GetMapping(value = "/{userId}")
    public R<TbTestUser> getInfo(@Parameter(description = "主键Id", required = true, in = ParameterIn.QUERY)
                                       @PathVariable Long userId) {
        return R.ok(tbTestUserService.selectTbTestUserByUserId(userId));
    }

    @Operation(summary = "新增--测试用户")
    @PreAuthorize("@ss.hasPermi('business:user:add')")
    @Log(title = "测试用户", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@Parameter(description = "DTO对象", required = true, in = ParameterIn.QUERY)
                             @RequestBody @Validated TbTestUser tbTestUser) {
        return toResult(tbTestUserService.insertTbTestUser(tbTestUser));
    }

    @Operation(summary = "修改--测试用户")
    @PreAuthorize("@ss.hasPermi('business:user:edit')")
    @Log(title = "测试用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> update(@Parameter(description = "DTO对象")
                                @RequestBody @Validated TbTestUser tbTestUser) {
        return toResult(tbTestUserService.updateTbTestUser(tbTestUser));
    }

    @Operation(summary = "删除--测试用户")
    @PreAuthorize("@ss.hasPermi('business:user:remove')")
    @Log(title = "测试用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public R<String> delete(@Parameter(description = "主键集合", required = true, in = ParameterIn.QUERY)
                                @PathVariable List<Long> userIds) {
        return toResult(tbTestUserService.deleteTbTestUserByUserIds(userIds));
    }

    @Operation(summary = "导出--测试用户列表")
    @PreAuthorize("@ss.hasPermi('business:user:export')")
    @Log(title = "测试用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response,
                       @Parameter(description = "DTO对象", in = ParameterIn.QUERY) TbTestUser tbTestUser) {
        List<TbTestUser> list = tbTestUserService.selectTbTestUserList(tbTestUser);
        ExcelUtil<TbTestUser> util = new ExcelUtil<>(TbTestUser.class);
        util.exportExcel(response, list, "测试用户数据");
    }

    @Operation(summary = "导出--测试用户模板")
    @PreAuthorize("@ss.hasPermi('business:user:import')")
    @PostMapping("/importTemplate")
    public void exportTemplate(HttpServletResponse response) {
        ExcelUtil<TbTestUser> util = new ExcelUtil<>(TbTestUser.class);
        util.importTemplateExcel(response, "导入测试用户模板数据");
    }

    @Operation(summary = "导入--测试用户列表")
    @PreAuthorize("@ss.hasPermi('business:user:import')")
    @Log(title = "测试用户", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public R<Boolean> importData(@Parameter(description = "导出模板的二进制xlsx文件", required = true)
                                     MultipartFile file) throws IOException {
        ExcelUtil<TbTestUser> util = new ExcelUtil<>(TbTestUser.class);
        List<TbTestUser> list = util.importExcel(file.getInputStream());
        return R.ok(tbTestUserService.saveBatch(list), "成功导入 " + list.size() + " 条记录");
    }
}
