package com.yxx.quartz;


import com.yxx.common.utils.spring.SpringUtils;
import com.yxx.example.controller.TbTestUserController;
import org.quartz.JobExecutionException;

public class TestTask {

    public String success(String param) {
        System.out.println(param);
        return param;
    }

    public String fail() {
        System.out.println(SpringUtils.getBean(TbTestUserController.class));
        throw new RuntimeException("错误");
    }

    public void warn() throws JobExecutionException {
        throw new JobExecutionException("警告");
    }
}