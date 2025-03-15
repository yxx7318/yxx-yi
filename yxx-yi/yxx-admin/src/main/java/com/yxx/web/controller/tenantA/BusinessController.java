package com.yxx.web.controller.tenantA;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.yxx.tenantA.domain.Business;
import com.yxx.tenantA.service.IBusinessService;
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
 * xxx业务Controller
 * 
 * @author yxx
 * @date 2025-03-15
 */
@Api("xxx业务管理")
@RestController
@RequestMapping("/tenantA/business")
@RequiredArgsConstructor
public class BusinessController extends BaseControllerPlus {

    @Autowired
    private  IBusinessService businessService;

    @ApiOperation("查询xxx业务列表")
    @PreAuthorize("@ss.hasPermi('tenantA:business:list')")
    @GetMapping("/list")
    public PageResult<?> list(@Parameter(description = "DTO对象", in = ParameterIn.QUERY) Business business)
    {
        startPage();
        List<Business> list = businessService.selectBusinessList(business);
        return getDataTableToPR(list, business);
    }

    @ApiOperation("获取xxx业务详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "businessId", value = "主键Id", required = true, dataType = "Long", dataTypeClass = Long.class, paramType = "path"),
    })
    @PreAuthorize("@ss.hasPermi('tenantA:business:query')")
    @GetMapping(value = "/{businessId}")
    public R<Business> getInfo(@PathVariable("businessId") Long businessId)
    {
        return R.ok(businessService.selectBusinessByBusinessId(businessId));
    }

    @ApiOperation("新增xxx业务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "business", value = "DTO对象", required = true, dataType = "Business", dataTypeClass = Business.class, paramType = "body"),
    })
    @PreAuthorize("@ss.hasPermi('tenantA:business:add')")
    @Log(title = "xxx业务", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@RequestBody Business business)
    {
        return toResult(businessService.insertBusiness(business));
    }

    @ApiOperation("修改xxx业务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "business", value = "DTO对象", required = true, dataType = "Business", dataTypeClass = Business.class, paramType = "body"),
    })
    @PreAuthorize("@ss.hasPermi('tenantA:business:edit')")
    @Log(title = "xxx业务", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<String> update(@RequestBody Business business)
    {
        return toResult(businessService.updateBusiness(business));
    }

    @ApiOperation("删除xxx业务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "businessIds", value = "主键集合", required = true, dataType = "List<Long>", dataTypeClass = List.class, paramType = "path", allowMultiple = true),
    })
    @PreAuthorize("@ss.hasPermi('tenantA:business:remove')")
    @Log(title = "xxx业务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{businessIds}")
    public R<String> delete(@PathVariable List<Long> businessIds)
    {
        return toResult(businessService.deleteBusinessByBusinessIds(businessIds));
    }

    @ApiOperation("导出xxx业务列表")
    @PreAuthorize("@ss.hasPermi('tenantA:business:export')")
    @Log(title = "xxx业务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @Parameter(description = "DTO对象", in = ParameterIn.QUERY) Business business)
    {
        List<Business> list = businessService.selectBusinessList(business);
        ExcelUtil<Business> util = new ExcelUtil<>(Business.class);
        util.exportExcel(response, list, "xxx业务数据");
    }

    @ApiOperation("导入xxx业务模板")
    @PreAuthorize("@ss.hasPermi('tenantA:business:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<Business> util = new ExcelUtil<>(Business.class);
        util.importTemplateExcel(response, "导入xxx业务模板数据");
    }

    @ApiOperation("导入xxx业务列表")
    @PreAuthorize("@ss.hasPermi('tenantA:business:import')")
    @Log(title = "xxx业务", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public R<Boolean> importData(MultipartFile file) throws IOException
    {
        ExcelUtil<Business> util = new ExcelUtil<>(Business.class);
        List<Business> list = util.importExcel(file.getInputStream());
        return R.ok(businessService.saveBatch(list), "成功导入 " + list.size() + " 条记录");
    }
}
