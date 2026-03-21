package com.yxx.framework.manager.factory;

import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yxx.common.constant.Constants;
import com.yxx.common.utils.LogUtils;
import com.yxx.common.utils.ServletUtils;
import com.yxx.common.utils.StringUtils;
import com.yxx.common.utils.ip.AddressUtils;
import com.yxx.common.utils.ip.IpUtils;
import com.yxx.common.utils.spring.SpringUtils;
import com.yxx.system.domain.SysLoginInfo;
import com.yxx.system.domain.SysOperLog;
import com.yxx.system.service.ISysLoginInfoService;
import com.yxx.system.service.ISysOperLogService;
import com.yxx.common.utils.http.UserAgentUtils;

/**
 * 异步工厂（产生任务用）
 */
public class AsyncFactory
{
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 记录登录信息
     * 
     * @param username 用户名
     * @param status 状态
     * @param message 消息
     * @param args 列表
     * @return 任务task
     */
    public static TimerTask recordLoginInfo(final String username, final String status, final String message,
                                            final Object... args)
    {
        final String userAgent = ServletUtils.getRequest().getHeader("User-Agent");
        final String ip = IpUtils.getIpAddr();
        return new TimerTask()
        {
            @Override
            public void run()
            {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                sys_user_logger.info(s.toString(), args);
                // 获取客户端操作系统
                String os = UserAgentUtils.getOperatingSystem(userAgent);
                // 获取客户端浏览器
                String browser = UserAgentUtils.getBrowser(userAgent);
                // 封装对象
                SysLoginInfo loginInfo = new SysLoginInfo();
                loginInfo.setUserName(username);
                loginInfo.setIpaddr(ip);
                loginInfo.setLoginLocation(address);
                loginInfo.setBrowser(browser);
                loginInfo.setOs(os);
                loginInfo.setMsg(message);
                // 日志状态
                if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER))
                {
                    loginInfo.setStatus(Constants.SUCCESS);
                }
                else if (Constants.LOGIN_FAIL.equals(status))
                {
                    loginInfo.setStatus(Constants.FAIL);
                }
                // 填充公共字段(系统访问日志没有创建人)
                loginInfo.fieldFillInsert();
                // 插入数据
                SpringUtils.getBean(ISysLoginInfoService.class).insertLoginInfo(loginInfo);
            }
        };
    }

    /**
     * 操作日志记录
     * 
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final SysOperLog operLog)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(ISysOperLogService.class).insertOperLog(operLog);
            }
        };
    }
}
