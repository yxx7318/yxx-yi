package com.yxx.web.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.yxx.common.core.controller.BaseController;
import com.yxx.common.core.domain.AjaxResult;
import com.yxx.common.core.domain.model.RegisterBody;
import com.yxx.common.utils.StringUtils;
import com.yxx.framework.web.service.SysRegisterService;
import com.yxx.system.service.ISysConfigService;

/**
 * 注册验证
 */
@RestController
public class SysRegisterController extends BaseController
{
    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody user)
    {
        if (!configService.selectRegisterEnabled())
        {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }

    @GetMapping("/register")
    public AjaxResult register()
    {
        return success(configService.selectRegisterEnabled());
    }
}
