package com.yxx.ai.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

public class AiEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // 在标准配置文件加载之前激活profile
        environment.addActiveProfile("ai");
    }

    /**
     * 优先级修改前：
     * 启动流程：
     * 1. ConfigFileApplicationListener (先执行)
     * ↓ 只加载了 application.yml 和 application-dev.yml
     * ↓ 此时 ai profile 还没激活，所以不加载 application-ai.yml
     * 2. EnvironmentPostProcessor (后执行)
     * ↓ 虽然激活了 ai profile，但配置文件已经加载完成
     * 3. 自动配置类使用不完整的配置
     * <p>
     * 优先级修改后:
     * 启动流程：
     * 1. EnvironmentPostProcessor (高优先级，如 Ordered.HIGHEST_PRECEDENCE + 5)
     * ↓ 此时激活了 "ai" profile
     * 2. ConfigFileApplicationListener (Ordered.HIGHEST_PRECEDENCE + 10)
     * ↓ 加载 application.yml, application-ai.yml, application-dev.yml
     * 3. 其他 EnvironmentPostProcessor (默认优先级)
     * 4. 自动配置类
     */
    @Override
    public int getOrder() {
        // 在 ConfigFileApplicationListener 之前执行
        return Ordered.HIGHEST_PRECEDENCE;
    }

}