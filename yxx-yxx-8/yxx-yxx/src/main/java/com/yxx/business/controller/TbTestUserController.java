package com.yxx.business.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import com.yxx.common.annotation.Log;
import com.yxx.common.core.controller.BaseControllerPlus;
import com.yxx.common.enums.BusinessType;
import com.yxx.business.entity.TbTestUserDo;
import com.yxx.business.entity.TbTestUserVo;
import com.yxx.business.entity.TbTestUserQueryDto;
import com.yxx.business.entity.TbTestUserEditDto;
import com.yxx.business.service.ITbTestUserService;
import com.yxx.common.utils.poi.ExcelUtil;
import com.yxx.common.core.domain.R;
import com.yxx.common.core.domain.PageResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.api.annotations.ParameterObject;

import org.springframework.validation.annotation.Validated;


/**
 * 测试用户Controller
 *
 * @author yxx
 * @date 2025-08-04
 */
@Tag(name = "测试用户管理-TbTestUser")
@RestController
@RequestMapping("/business/user")
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class TbTestUserController extends BaseControllerPlus {

    private final ITbTestUserService tbTestUserService;

    @Operation(summary = "查询--测试用户列表")
    @PreAuthorize("@ss.hasPermi('business:user:list')")
    @GetMapping("/list")
    public PageResult<TbTestUserVo> list(@ParameterObject TbTestUserQueryDto tbTestUserQueryDto) {
        return tbTestUserService.selectTbTestUserVoPage(tbTestUserQueryDto);
    }

    @Operation(summary = "查询--测试用户单个")
    @PreAuthorize("@ss.hasPermi('business:user:query')")
    @GetMapping(value = "/{userId}")
    public R<TbTestUserVo> getInfo(@PathVariable Long userId) {
        return R.ok(tbTestUserService.selectTbTestUserVoByUserId(userId));
    }

    @Operation(summary = "新增--测试用户")
    @PreAuthorize("@ss.hasPermi('business:user:add')")
    @Log(title = "测试用户", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@RequestBody @Validated TbTestUserEditDto tbTestUserQueryDto) {
        return toResult(tbTestUserService.insertTbTestUser(tbTestUserQueryDto));
    }

    @Operation(summary = "修改--测试用户")
    @PreAuthorize("@ss.hasPermi('business:user:edit')")
    @Log(title = "测试用户", businessType = BusinessType.UPDATE)
    @PutMapping("/{userId}")
    public R<String> update(@PathVariable Long userId, @RequestBody @Validated TbTestUserEditDto tbTestUserQueryDto) {
        return toResult(tbTestUserService.updateTbTestUser(userId, tbTestUserQueryDto));
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
