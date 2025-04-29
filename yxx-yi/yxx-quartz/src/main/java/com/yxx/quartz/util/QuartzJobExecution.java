package com.yxx.quartz.util;

import org.quartz.JobExecutionContext;
import com.yxx.quartz.domain.SysJob;

/**
 * 定时任务处理（允许并发执行）
 */
public class QuartzJobExecution extends AbstractQuartzJob
{
    @Override
    protected String doExecute(JobExecutionContext context, SysJob sysJob) throws Exception
    {
        return JobInvokeUtil.invokeMethod(sysJob);
    }
}
