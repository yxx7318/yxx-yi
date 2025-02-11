package com.yxx.quartz;


public class TestTask {

    public String success(String param) {
        System.out.println(param);
        return param;
    }

    public String fail() {
        throw new RuntimeException("错误");
    }
}