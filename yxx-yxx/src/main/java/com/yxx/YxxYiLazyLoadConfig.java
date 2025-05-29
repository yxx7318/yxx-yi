package com.yxx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.ClassUtils;

/**
 * 懒加载配置
 *
 * @author yxx
 */
@Slf4j
//@Configuration
@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "dev")
public class YxxYiLazyLoadConfig {

    // 目标包名
    private static final String TARGET_PACKAGE = "com.yxx.business";

    // 显式注册 BeanFactoryPostProcessor
    @Bean
    public static BeanFactoryPostProcessor lazyLoadBeanFactoryPostProcessor() {
        return factory -> {
            String[] beanNames = factory.getBeanDefinitionNames();
            for (String beanName : beanNames) {
                BeanDefinition beanDef = factory.getBeanDefinition(beanName);
                String className = beanDef.getBeanClassName();
                if (className != null) {
                    try {
                        Class<?> clazz = ClassUtils.forName(className, null);
                        if (clazz.getPackage() != null && clazz.getPackage().getName().startsWith(TARGET_PACKAGE)) {
                            Lazy annotation = clazz.getAnnotation(Lazy.class);
                            // 如果没有设置@Lazy注解，或者注解值为true
                            if (annotation == null || annotation.value()) {
                                // 设置懒加载
                                beanDef.setLazyInit(true);
                            } else {
                                log.info("Bean " + beanName + " is not already lazy-initialized\n");
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        // 处理异常（例如：类未找到，可能由动态代理导致）
                        throw new RuntimeException(e);
                    }
                }
            }
        };
    }
}