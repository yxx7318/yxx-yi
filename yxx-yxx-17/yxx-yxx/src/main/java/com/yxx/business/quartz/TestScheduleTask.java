package com.yxx.business.quartz;

import org.quartz.JobExecutionException;

public class TestScheduleTask {

    public String success(String param) {
        return param;
    }

    public String fail() {
        throw new RuntimeException("错误");
    }

    public void warn() throws JobExecutionException {
        throw new JobExecutionException("警告");
    }
}