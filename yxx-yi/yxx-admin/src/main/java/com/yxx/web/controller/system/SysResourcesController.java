package com.yxx.web.controller.system;

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
import com.yxx.system.domain.SysResources;
import com.yxx.system.service.ISysResourcesService;
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
 * 数据源Controller
 * 
 * @author yxx
 * @date 2025-03-15
 */
@Api("数据源管理")
@RestController
@RequestMapping("/system/resources")
@RequiredArgsConstructor
public class SysResourcesController extends BaseControllerPlus {

    private final ISysResourcesService sysResourcesService;

    @ApiOperation("查询数据源列表")
    @PreAuthorize("@ss.hasPermi('system:resources:list')")
    @GetMapping("/list")
    public PageResult<?> list(@Parameter(description = "DTO对象", in = ParameterIn.QUERY) SysResources sysResources)
    {
        startPage();
        List<SysResources> list = sysResourcesService.selectSysResourcesList(sysResources);
        return getDataTableToPR(list, sysResources);
    }

    @ApiOperation("获取数据源详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceId", value = "主键Id", required = true, dataType = "Long", dataTypeClass = Long.class, paramType = "path"),
    })
    @PreAuthorize("@ss.hasPermi('system:resources:query')")
    @GetMapping(value = "/{resourceId}")
    public R<SysResources> getInfo(@PathVariable("resourceId") Long resourceId)
    {
        return R.ok(sysResourcesService.selectSysResourcesByResourceId(resourceId));
    }

    @ApiOperation("新增数据源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysResources", value = "DTO对象", required = true, dataType = "SysResources", dataTypeClass = SysResources.class, paramType = "body"),
    })
    @PreAuthorize("@ss.hasPermi('system:resources:add')")
    @Log(title = "数据源", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@RequestBody SysResources sysResources)
    {
        return toResult(sysResourcesService.insertSysResources(sysResources));
    }

    @ApiOperation("修改数据源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysResources", value = "DTO对象", required = true, dataType = "SysResources", dataTypeClass = SysResources.class, paramType = "body"),
    })
    @PreAuthorize("@ss.hasPermi('system:resources:edit')")
    @Log(title = "数据源", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> update(@RequestBody SysResources sysResources)
    {
        return toResult(sysResourcesService.updateSysResources(sysResources));
    }

    @ApiOperation("删除数据源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceIds", value = "主键集合", required = true, dataType = "List<Long>", dataTypeClass = List.class, paramType = "path", allowMultiple = true),
    })
    @PreAuthorize("@ss.hasPermi('system:resources:remove')")
    @Log(title = "数据源", businessType = BusinessType.DELETE)
    @DeleteMapping("/{resourceIds}")
    public R<String> delete(@PathVariable List<Long> resourceIds)
    {
        return toResult(sysResourcesService.deleteSysResourcesByResourceIds(resourceIds));
    }

    @ApiOperation("导出数据源列表")
    @PreAuthorize("@ss.hasPermi('system:resources:export')")
    @Log(title = "数据源", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @Parameter(description = "DTO对象", in = ParameterIn.QUERY) SysResources sysResources)
    {
        List<SysResources> list = sysResourcesService.selectSysResourcesList(sysResources);
        ExcelUtil<SysResources> util = new ExcelUtil<>(SysResources.class);
        util.exportExcel(response, list, "数据源数据");
    }

    @ApiOperation("导入数据源模板")
    @PreAuthorize("@ss.hasPermi('system:resources:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<SysResources> util = new ExcelUtil<>(SysResources.class);
        util.importTemplateExcel(response, "导入数据源模板数据");
    }

    @ApiOperation("导入数据源列表")
    @PreAuthorize("@ss.hasPermi('system:resources:import')")
    @Log(title = "数据源", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public R<Boolean> importData(MultipartFile file) throws IOException
    {
        ExcelUtil<SysResources> util = new ExcelUtil<>(SysResources.class);
        List<SysResources> list = util.importExcel(file.getInputStream());
        return R.ok(sysResourcesService.saveBatch(list), "成功导入 " + list.size() + " 条记录");
    }
}
