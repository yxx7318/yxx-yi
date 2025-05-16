package com.yxx.business.quartz;


import com.yxx.common.utils.spring.SpringUtils;
import org.quartz.JobExecutionException;

public class TestTask {

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