package com.yxx.common.core.utils;

import com.yxx.common.utils.spring.SpringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.env.Environment;

/**
 * 获取环境变量值工具类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnvironmentUtils {

    private static final Environment ENVIRONMENT = SpringUtils.getBean(Environment.class);

    /**
     * 获取配置文件中的属性值
     *
     * @param key 键
     * @return 值
     */
    public static String getEnvValue(String key) {
        return ENVIRONMENT.getProperty(key);
    }

    /**
     * 获取配置文件中的属性值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    public static String getEnvValue(String key, String defaultValue) {
        return ENVIRONMENT.getProperty(key, defaultValue);
    }

    /**
     * 获取配置文件中的属性值
     *
     * @param key        键
     * @param targetType 转换类型
     * @return 值
     */
    public static <T> T getEnvValue(String key, Class<T> targetType) {
        return ENVIRONMENT.getProperty(key, targetType);
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
        return ENVIRONMENT.getProperty(key, targetType, defaultValue);
    }

}
