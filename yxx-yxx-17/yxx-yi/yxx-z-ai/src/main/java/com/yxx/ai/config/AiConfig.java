package com.yxx.ai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 读取ai相关配置
 */
@Component
@ConfigurationProperties(prefix = "ai")
@PropertySource(value = {"classpath:application-ai.yml"})
@Lazy(value = false)
public class AiConfig {

    /** 作者 */
    public static String author;

    public static String getAuthor()
    {
        return author;
    }

    @Value("${author}")
    public void setAuthor(String author)
    {
        AiConfig.author = author;
    }
}
