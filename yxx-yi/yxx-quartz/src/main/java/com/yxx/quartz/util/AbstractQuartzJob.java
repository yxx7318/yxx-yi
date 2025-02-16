package com.yxx.quartz.util;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yxx.common.constant.Constants;
import com.yxx.common.constant.ScheduleConstants;
import com.yxx.common.utils.ExceptionUtil;
import com.yxx.common.utils.StringUtils;
import com.yxx.common.utils.bean.BeanUtils;
import com.yxx.common.utils.spring.SpringUtils;
import com.yxx.quartz.domain.SysJob;
import com.yxx.quartz.domain.SysJobLog;
import com.yxx.quartz.service.ISysJobLogService;

/**
 * 抽象quartz调用
 *
 * @author ruoyi
 */
public abstract class AbstractQuartzJob implements Job
{
    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);

    /**
     * 线程本地变量
     */
    private static ThreadLocal<Date> threadLocal = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        SysJob sysJob = new SysJob();
        BeanUtils.copyBeanProp(sysJob, context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES));
        try
        {
            before(context, sysJob);
            String successInfo = doExecute(context, sysJob);
            after(context, sysJob, successInfo, null);
        }
        catch (JobExecutionException e)
        {
            log.warn("任务执行警告  - ：", e);
            after(context, sysJob, null, e);
        }
        catch (Exception e)
        {
            log.error("任务执行异常  - ：", e);
            after(context, sysJob, null, e);
        }
    }

    /**
     * 执行前
     *
     * @param context 工作执行上下文对象
     * @param sysJob 系统计划任务
     */
    protected void before(JobExecutionContext context, SysJob sysJob)
    {
        threadLocal.set(new Date());
    }

    /**
     * 执行后
     *
     * @param context 工作执行上下文对象
     * @param sysJob 系统计划任务
     * @param successInfo 成功信息
     * @param e 异常
     */
    protected void after(JobExecutionContext context, SysJob sysJob, String successInfo, Exception e)
    {
        Date startTime = threadLocal.get();
        threadLocal.remove();

        final SysJobLog sysJobLog = new SysJobLog();
        sysJobLog.setJobName(sysJob.getJobName());
        sysJobLog.setJobGroup(sysJob.getJobGroup());
        sysJobLog.setInvokeTarget(sysJob.getInvokeTarget());
        sysJobLog.setStartTime(startTime);
        sysJobLog.setStopTime(new Date());
        long runMs = sysJobLog.getStopTime().getTime() - sysJobLog.getStartTime().getTime();
        sysJobLog.setJobMessage(sysJobLog.getJobName() + " 总共耗时：" + runMs + "毫秒");
        if (e instanceof JobExecutionException)
        {
            sysJobLog.setStatus(Constants.OTHER);
            String otherMsg = StringUtils.substring(e.getMessage(), 0, 2000);
            sysJobLog.setOtherInfo(otherMsg);
        }
        else if (e != null)
        {
            sysJobLog.setStatus(Constants.FAIL);
            String errorMsg = StringUtils.substring(ExceptionUtil.getExceptionMessage(e), 0, 2000);
            sysJobLog.setExceptionInfo(errorMsg);
        }
        {
            sysJobLog.setStatus(Constants.SUCCESS);
            String successMsg = StringUtils.substring(successInfo, 0, 2000);
            sysJobLog.setSuccessInfo(successMsg);
        }

        // 写入数据库当中
        SpringUtils.getBean(ISysJobLogService.class).addJobLog(sysJobLog);
    }

    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param sysJob  系统计划任务
     * @return 方法返回结果
     * @throws Exception 执行过程中的异常
     */
    protected abstract String doExecute(JobExecutionContext context, SysJob sysJob) throws Exception;
}
