package com.yxx.example.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.annotations.ApiOperation;

/**
 * 代码生成测试Controller
 * 
 * @author yxx
 * @date 2025-02-08
 */
@Api("代码生成测试管理")
@RestController
@RequestMapping("/example/testUser")
@RequiredArgsConstructor
public class TbTestUserController extends BaseControllerPlus
{
    private final ITbTestUserService tbTestUserService;

    @ApiOperation("查询代码生成测试列表")
    @PreAuthorize("@ss.hasPermi('example:testUser:list')")
    @GetMapping("/list")
    public PageResult<?> list(@Parameter(description = "DTO对象") TbTestUser tbTestUser)
    {
        startPage();
        List<TbTestUser> list = tbTestUserService.selectTbTestUserList(tbTestUser);
        return getDataTableToPR(list);
    }

    @ApiOperation("导出代码生成测试列表")
    @PreAuthorize("@ss.hasPermi('example:testUser:export')")
    @Log(title = "代码生成测试", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @Parameter(description = "DTO对象", in = ParameterIn.QUERY) TbTestUser tbTestUser)
    {
        List<TbTestUser> list = tbTestUserService.selectTbTestUserList(tbTestUser);
        ExcelUtil<TbTestUser> util = new ExcelUtil<TbTestUser>(TbTestUser.class);
        util.exportExcel(response, list, "代码生成测试数据");
    }

    @ApiOperation("获取代码生成测试详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "主键Id", required = true, dataType = "Long", dataTypeClass = Long.class, paramType = "path"),
    })
    @PreAuthorize("@ss.hasPermi('example:testUser:query')")
    @GetMapping(value = "/{userId}")
    public R<TbTestUser> getInfo(@PathVariable("userId") Long userId)
    {
        return R.ok(tbTestUserService.selectTbTestUserByUserId(userId));
    }

    @ApiOperation("新增代码生成测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tbTestUser", value = "DTO对象", required = true, dataType = "TbTestUser", dataTypeClass = TbTestUser.class, paramType = "body"),
    })
    @PreAuthorize("@ss.hasPermi('example:testUser:add')")
    @Log(title = "代码生成测试", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@RequestBody TbTestUser tbTestUser)
    {
        return toResult(tbTestUserService.insertTbTestUser(tbTestUser));
    }

    @ApiOperation("修改代码生成测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tbTestUser", value = "DTO对象", required = true, dataType = "TbTestUser", dataTypeClass = TbTestUser.class, paramType = "body"),
    })
    @PreAuthorize("@ss.hasPermi('example:testUser:edit')")
    @Log(title = "代码生成测试", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> edit(@RequestBody TbTestUser tbTestUser)
    {
        return toResult(tbTestUserService.updateTbTestUser(tbTestUser));
    }

    @ApiOperation("删除代码生成测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userIds", value = "主键数组", required = true, dataType = "Long[]", dataTypeClass = Long[].class, paramType = "path", allowMultiple = true),
    })
    @PreAuthorize("@ss.hasPermi('example:testUser:remove')")
    @Log(title = "代码生成测试", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public R<String> remove(@PathVariable Long[] userIds)
    {
        return toResult(tbTestUserService.deleteTbTestUserByUserIds(userIds));
    }
}
