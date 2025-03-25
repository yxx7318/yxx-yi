package com.yxx.example.controller;

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
import com.yxx.common.yxx.controllre.BaseControllerPlus;
import com.yxx.common.enums.BusinessType;
import com.yxx.example.domain.TbTestUser;
import com.yxx.example.service.ITbTestUserService;
import com.yxx.common.utils.poi.ExcelUtil;
import com.yxx.common.core.domain.R;
import com.yxx.common.yxx.domain.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

import org.springframework.validation.annotation.Validated;


/**
 * 测试用户Controller
 * 
 * @author yxx
 * @date 2025-03-24
 */
@Api(tags = "测试用户管理")
@RestController
@RequestMapping("/example/user")
@RequiredArgsConstructor
public class TbTestUserController extends BaseControllerPlus {

    private final ITbTestUserService tbTestUserService;

    @ApiOperation("查询测试用户列表")
    @PreAuthorize("@ss.hasPermi('example:user:list')")
    @GetMapping("/list")
    public PageResult<?> list(@Parameter(description = "DTO对象", in = ParameterIn.QUERY) TbTestUser tbTestUser)
    {
        startPage();
        List<TbTestUser> list = tbTestUserService.selectTbTestUserList(tbTestUser);
        return getDataTableToPR(list, tbTestUser);
    }

    @ApiOperation("获取测试用户详细信息")
    @PreAuthorize("@ss.hasPermi('example:user:query')")
    @GetMapping(value = "/{userId}")
    public R<TbTestUser> getInfo(@Parameter(description = "主键Id", required = true, in = ParameterIn.QUERY) @PathVariable("userId") Long userId)
    {
        return R.ok(tbTestUserService.selectTbTestUserByUserId(userId));
    }

    @ApiOperation("新增测试用户")
    @PreAuthorize("@ss.hasPermi('example:user:add')")
    @Log(title = "测试用户", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@RequestBody @Validated TbTestUser tbTestUser)
    {
        return toResult(tbTestUserService.insertTbTestUser(tbTestUser));
    }

    @ApiOperation("修改测试用户")
    @PreAuthorize("@ss.hasPermi('example:user:edit')")
    @Log(title = "测试用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> update(@Parameter(description = "DTO对象") @RequestBody @Validated TbTestUser tbTestUser)
    {
        return toResult(tbTestUserService.updateTbTestUser(tbTestUser));
    }

    @ApiOperation("删除测试用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userIds", value = "主键集合", required = true, dataType = "List<Long>", dataTypeClass = List.class, paramType = "path", allowMultiple = true),
    })
    @PreAuthorize("@ss.hasPermi('example:user:remove')")
    @Log(title = "测试用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public R<String> delete(@PathVariable List<Long> userIds)
    {
        return toResult(tbTestUserService.deleteTbTestUserByUserIds(userIds));
    }

    @ApiOperation("导出测试用户列表")
    @PreAuthorize("@ss.hasPermi('example:user:export')")
    @Log(title = "测试用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @Parameter(description = "DTO对象", in = ParameterIn.QUERY) TbTestUser tbTestUser)
    {
        List<TbTestUser> list = tbTestUserService.selectTbTestUserList(tbTestUser);
        ExcelUtil<TbTestUser> util = new ExcelUtil<>(TbTestUser.class);
        util.exportExcel(response, list, "测试用户数据");
    }

    @ApiOperation("导入测试用户模板")
    @PreAuthorize("@ss.hasPermi('example:user:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<TbTestUser> util = new ExcelUtil<>(TbTestUser.class);
        util.importTemplateExcel(response, "导入测试用户模板数据");
    }

    @ApiOperation("导入测试用户列表")
    @PreAuthorize("@ss.hasPermi('example:user:import')")
    @Log(title = "测试用户", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public R<Boolean> importData(MultipartFile file) throws IOException
    {
        ExcelUtil<TbTestUser> util = new ExcelUtil<>(TbTestUser.class);
        List<TbTestUser> list = util.importExcel(file.getInputStream());
        return R.ok(tbTestUserService.saveBatch(list), "成功导入 " + list.size() + " 条记录");
    }
}
