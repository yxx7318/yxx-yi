package com.yxx.business.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.yxx.common.annotation.RateLimiter;
import com.yxx.common.enums.LimitImpl;
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
import com.yxx.business.entity.TbTestUserDO;
import com.yxx.business.entity.TbTestUserVO;
import com.yxx.business.entity.TbTestUserQueryDTO;
import com.yxx.business.entity.TbTestUserEditDTO;
import com.yxx.business.service.ITbTestUserService;
import com.yxx.common.utils.poi.ExcelUtil;
import com.yxx.common.core.domain.R;
import com.yxx.common.core.domain.PageResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.api.annotations.ParameterObject;

import org.springframework.validation.annotation.Validated;


/**
 * 测试单表生成Controller
 *
 * @author yxx
 * @date 2025-10-13
 */
@Tag(name = "测试单表生成管理-TbTestUser")
@RestController
@RequestMapping("/business/user")
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
@RateLimiter
public class TbTestUserController extends BaseControllerPlus {

    private final ITbTestUserService tbTestUserService;

    @Operation(summary = "查询--测试单表生成列表")
    @PreAuthorize("@ss.hasPermi('business:user:list')")
    @GetMapping("/list")
    public PageResult<TbTestUserVO> list(@ParameterObject TbTestUserQueryDTO tbTestUserQueryDTO) {
        return tbTestUserService.selectTbTestUserVOPage(tbTestUserQueryDTO);
    }

    @Operation(summary = "查询--测试单表生成单个")
    @PreAuthorize("@ss.hasPermi('business:user:query')")
    @GetMapping(value = "/{userId}")
    public R<TbTestUserVO> getInfo(@PathVariable Long userId) {
        return R.ok(tbTestUserService.selectTbTestUserVOByUserId(userId));
    }

    @Operation(summary = "新增--测试单表生成")
    @PreAuthorize("@ss.hasPermi('business:user:add')")
    @Log(title = "测试单表生成", businessType = BusinessType.INSERT)
    @PostMapping
    public R<String> add(@RequestBody @Validated TbTestUserEditDTO tbTestUserEditDTO) {
        return toResult(tbTestUserService.insertTbTestUser(tbTestUserEditDTO));
    }

    @Operation(summary = "修改--测试单表生成")
    @PreAuthorize("@ss.hasPermi('business:user:edit')")
    @Log(title = "测试单表生成", businessType = BusinessType.UPDATE)
    @PutMapping("/{userId}")
    public R<String> update(@PathVariable Long userId, @RequestBody @Validated TbTestUserEditDTO tbTestUserEditDTO) {
        return toResult(tbTestUserService.updateTbTestUser(userId, tbTestUserEditDTO));
    }

    @Operation(summary = "删除--测试单表生成")
    @PreAuthorize("@ss.hasPermi('business:user:remove')")
    @Log(title = "测试单表生成", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public R<String> delete(@PathVariable List<Long> userIds) {
        return toResult(tbTestUserService.deleteTbTestUserByUserIds(userIds));
    }

    @Operation(summary = "导出--测试单表生成列表")
    @PreAuthorize("@ss.hasPermi('business:user:export')")
    @Log(title = "测试单表生成", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @ParameterObject TbTestUserQueryDTO tbTestUserQueryDTO) {
        List<TbTestUserDO> list = tbTestUserService.selectTbTestUserDOList(tbTestUserQueryDTO);
        ExcelUtil<TbTestUserDO> util = new ExcelUtil<>(TbTestUserDO.class);
        util.exportExcel(response, list, "测试单表生成数据");
    }

    @Operation(summary = "导出--测试单表生成模板")
    @PreAuthorize("@ss.hasPermi('business:user:import')")
    @PostMapping("/importTemplate")
    public void exportTemplate(HttpServletResponse response) {
        ExcelUtil<TbTestUserDO> util = new ExcelUtil<>(TbTestUserDO.class);
        util.importTemplateExcel(response, "导入测试单表生成模板数据");
    }

    @Operation(summary = "导入--测试单表生成列表")
    @PreAuthorize("@ss.hasPermi('business:user:import')")
    @Log(title = "测试单表生成", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public R<Boolean> importData(MultipartFile file) throws IOException {
        ExcelUtil<TbTestUserDO> util = new ExcelUtil<>(TbTestUserDO.class);
        List<TbTestUserDO> list = util.importExcel(file.getInputStream());
        return R.ok(tbTestUserService.saveBatch(list), "成功导入 " + list.size() + " 条记录");
    }
}
