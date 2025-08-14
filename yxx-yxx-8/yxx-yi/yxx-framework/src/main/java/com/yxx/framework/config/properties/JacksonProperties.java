package com.yxx.framework.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 数字类型转换字符串配置类
 */

@Component
@ConfigurationProperties(prefix = "spring.json")
public class JacksonProperties {

    /** LocalDateTime 时间戳序列化和反序列化 */
    public boolean timestampLocalDateTime;

    /** 数字全部转化字符串 */
    public boolean numToStr;

    /** null值字段忽略 */
    public boolean nonNull;

    /** null和empty值字段忽略 */
    public boolean nonEmpty;

    public boolean isTimestampLocalDateTime() {
        return timestampLocalDateTime;
    }

    public void setTimestampLocalDateTime(boolean timestampLocalDateTime) {
        this.timestampLocalDateTime = timestampLocalDateTime;
    }

    public boolean isNumToStr() {
        return numToStr;
    }

    public void setNumToStr(boolean numToStr) {
        this.numToStr = numToStr;
    }

    public boolean isNonNull() {
        return nonNull;
    }

    public void setNonNull(boolean nonNull) {
        this.nonNull = nonNull;
    }

    public boolean isNonEmpty() {
        return nonEmpty;
    }

    public void setNonEmpty(boolean nonEmpty) {
        this.nonEmpty = nonEmpty;
    }
}