package com.yxx.erp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 读取erp相关配置
 */
@Component
@ConfigurationProperties(prefix = "erp")
@PropertySource(value = { "classpath:erp.yml" })
@Lazy(value = false)
public class ErpConfig {

    /** 作者 */
    public static String author;

    public static String getAuthor()
    {
        return author;
    }

    @Value("${author}")
    public void setAuthor(String author)
    {
        ErpConfig.author = author;
    }
}
