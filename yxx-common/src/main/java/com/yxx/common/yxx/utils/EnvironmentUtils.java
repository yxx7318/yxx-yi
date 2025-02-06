package com.yxx.common.yxx.utils;

import com.yxx.common.utils.spring.SpringUtils;
import org.springframework.core.env.Environment;

public class EnvironmentUtils {

    private static final Environment environment = SpringUtils.getBean(Environment.class);

    /**
     * 获取配置文件中的属性值
     *
     * @param key 键
     * @return 值
     */
    public static String getEnvValue(String key) {
        return environment.getProperty(key);
    }

    /**
     * 获取配置文件中的属性值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    public static String getEnvValue(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }

    /**
     * 获取配置文件中的属性值
     *
     * @param key        键
     * @param targetType 转换类型
     * @return 值
     */
    public static <T> T getEnvValue(String key, Class<T> targetType) {
        return environment.getProperty(key, targetType);
    }

    /**
     * 获取配置文件中的属性值
     *
     * @param key          键
     * @param targetType   转换类型
     * @param defaultValue 默认值
     * @return 值
     */
    public static <T> T getEnvValue(String key, Class<T> targetType, T defaultValue) {
        return environment.getProperty(key, targetType, defaultValue);
    }

}
