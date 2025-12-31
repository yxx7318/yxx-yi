package com.yxx.pay.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 读取pay相关配置
 */
@Component
@ConfigurationProperties(prefix = "pay")
@PropertySource(value = { "classpath:application-pay.yml" })
@Lazy(value = false)
public class PayConfig {

    /** 作者 */
    public static String author;

    public static String getAuthor()
    {
        return author;
    }

    @Value("${author}")
    public void setAuthor(String author)
    {
        PayConfig.author = author;
    }
}
