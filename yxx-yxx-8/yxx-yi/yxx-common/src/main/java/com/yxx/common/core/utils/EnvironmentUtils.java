package com.yxx.common.core.utils;

import com.yxx.common.enums.EnvironmentType;
import com.yxx.common.utils.spring.SpringUtils;
import org.springframework.core.env.Environment;

/**
 * 获取环境变量值工具类
 */
public class EnvironmentUtils {

    private EnvironmentUtils() {
    }

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

    /**
     * 匹配环境，匹配成功返回true
     *
     * @param type 环境枚举
     * @return 是否存在匹配
     */
    private static boolean matchesEnvironment(EnvironmentType type) {
        String[] activeProfiles = SpringUtils.getActiveProfiles();
        for (String profile : activeProfiles) {
            if (type.getType().equals(profile)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前是否为dev环境
     */
    public static boolean isDevActive() {
        return matchesEnvironment(EnvironmentType.DEV);
    }

    /**
     * 判断当前是否为test环境
     */
    public static boolean isTestActive() {
        return matchesEnvironment(EnvironmentType.TEST);
    }

    /**
     * 判断当前是否为prod环境
     */
    public static boolean isProdActive() {
        return matchesEnvironment(EnvironmentType.PROD);
    }

}
