package com.yxx.generator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 读取代码生成相关配置
 */
@Component
@ConfigurationProperties(prefix = "gen")
@PropertySource(value = { "classpath:generator.yml" })
public class GenConfig
{
    /** 作者 */
    public static String author;

    /** 生成包路径 */
    public static String packageName;

    /** 自动去除表前缀 */
    public static boolean autoRemovePre;

    /** 表前缀 */
    public static String tablePrefix;

    /** 是否允许生成文件覆盖到本地（自定义路径） */
    public static boolean allowOverwrite;

    /** 激活的代码生成模板 */
    public static String vmType;

    /** 否开启代码生成基类属性(开启后可以提高生成的代码对MP的兼容性) */
    public static boolean entitySwitch;

    public static String getAuthor()
    {
        return author;
    }

    @Value("${author}")
    public void setAuthor(String author)
    {
        GenConfig.author = author;
    }

    public static String getPackageName()
    {
        return packageName;
    }

    @Value("${packageName}")
    public void setPackageName(String packageName)
    {
        GenConfig.packageName = packageName;
    }

    public static boolean getAutoRemovePre()
    {
        return autoRemovePre;
    }

    @Value("${autoRemovePre}")
    public void setAutoRemovePre(boolean autoRemovePre)
    {
        GenConfig.autoRemovePre = autoRemovePre;
    }

    public static String getTablePrefix()
    {
        return tablePrefix;
    }

    @Value("${tablePrefix}")
    public void setTablePrefix(String tablePrefix)
    {
        GenConfig.tablePrefix = tablePrefix;
    }

    public static boolean isAllowOverwrite()
    {
        return allowOverwrite;
    }

    @Value("${allowOverwrite}")
    public void setAllowOverwrite(boolean allowOverwrite)
    {
        GenConfig.allowOverwrite = allowOverwrite;
    }

    public static String getVmType()
    {
        return vmType;
    }

    @Value("${vmType}")
    public void setVmType(String vmType)
    {
        GenConfig.vmType = vmType;
    }

    public static boolean isEntitySwitch()
    {
        return entitySwitch;
    }

    @Value("${entitySwitch}")
    public void setEntitySwitch(boolean entitySwitch)
    {
        GenConfig.entitySwitch = entitySwitch;
    }
}
