package com.yxx.quartz.util;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import com.yxx.quartz.domain.SysJob;

/**
 * 定时任务处理（禁止并发执行）
 * 
 * @author ruoyi
 *
 */
@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob
{
    @Override
    protected String doExecute(JobExecutionContext context, SysJob sysJob) throws Exception
    {
        return JobInvokeUtil.invokeMethod(sysJob);
    }
}
