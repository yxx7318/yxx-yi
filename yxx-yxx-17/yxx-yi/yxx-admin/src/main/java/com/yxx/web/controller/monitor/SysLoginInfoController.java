package com.yxx.web.controller.monitor;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yxx.common.annotation.Log;
import com.yxx.common.core.controller.BaseController;
import com.yxx.common.core.domain.AjaxResult;
import com.yxx.common.core.page.TableDataInfo;
import com.yxx.common.enums.BusinessType;
import com.yxx.common.utils.poi.ExcelUtil;
import com.yxx.framework.web.service.SysPasswordService;
import com.yxx.system.domain.SysLoginInfo;
import com.yxx.system.service.ISysLoginInfoService;

/**
 * 系统访问记录
 */
@RestController
@RequestMapping("/monitor/loginInfo")
public class SysLoginInfoController extends BaseController
{
    @Autowired
    private ISysLoginInfoService loginInfoService;

    @Autowired
    private SysPasswordService passwordService;

    @PreAuthorize("@ss.hasPermi('monitor:loginInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysLoginInfo loginInfo)
    {
        startPage();
        List<SysLoginInfo> list = loginInfoService.selectLoginInfoList(loginInfo);
        return getDataTable(list);
    }

    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('monitor:loginInfo:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLoginInfo loginInfo)
    {
        List<SysLoginInfo> list = loginInfoService.selectLoginInfoList(loginInfo);
        ExcelUtil<SysLoginInfo> util = new ExcelUtil<SysLoginInfo>(SysLoginInfo.class);
        util.exportExcel(response, list, "登录日志");
    }

    @PreAuthorize("@ss.hasPermi('monitor:loginInfo:remove')")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds)
    {
        return toAjax(loginInfoService.deleteLoginInfoByIds(infoIds));
    }

    @PreAuthorize("@ss.hasPermi('monitor:loginInfo:remove')")
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean()
    {
        loginInfoService.cleanLoginInfo();
        return success();
    }

    @PreAuthorize("@ss.hasPermi('monitor:loginInfo:unlock')")
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @GetMapping("/unlock/{userName}")
    public AjaxResult unlock(@PathVariable("userName") String userName)
    {
        passwordService.clearLoginRecordCache(userName);
        return success();
    }
}
