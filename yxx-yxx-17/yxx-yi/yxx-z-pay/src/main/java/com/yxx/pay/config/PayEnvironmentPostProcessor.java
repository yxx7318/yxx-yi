package com.yxx.pay.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

public class PayEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // 在标准配置文件加载之前激活profile
        environment.addActiveProfile("pay");
    }

    @Override
    public int getOrder() {
        // 在 ConfigFileApplicationListener 之前执行
        return Ordered.HIGHEST_PRECEDENCE;
    }

}