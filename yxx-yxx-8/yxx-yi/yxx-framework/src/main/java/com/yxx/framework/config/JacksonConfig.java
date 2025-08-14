package com.yxx.framework.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.yxx.common.core.utils.JacksonUtils;
import com.yxx.framework.config.databind.NumberSerializer;
import com.yxx.framework.config.databind.TimestampLocalDateTimeDeserializer;
import com.yxx.framework.config.databind.TimestampLocalDateTimeSerializer;
import com.yxx.framework.config.properties.JacksonProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 数字类型转换字符串配置类
 */

@Configuration
public class JacksonConfig {

    @Autowired
    public JacksonConfig(ObjectMapper objectMapper, JacksonProperties jacksonProperties) {
        // 尝试序列化（即转换为JSON）没有 getter 方法或公共字段的类（空bean），不会抛出异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 在反序列化（即从JSON转换为Java对象）过程中，如果遇到JSON中存在但目标Java对象中不存在的属性时，不会抛出异常，提高灵活性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 注册 JavaTimeModule 支持Java 8时间类型（如 LocalDateTime, ZonedDateTime 等）的序列化和反序列化
        objectMapper.registerModules(new JavaTimeModule());
        if (jacksonProperties.isNonNull()) {
            // 指定在序列化时忽略所有值为 null 的属性
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        if (jacksonProperties.isNonEmpty()) {
            // 指定在序列化时忽略所有值为 null 和 empty 的属性
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        }
        // 创建 SimpleModule 对象
        SimpleModule simpleModule = new SimpleModule();
        simpleModule
                // 新增 Long 类型序列化规则，数值超过 2^53-1，在 JS 会出现精度丢失问题，因此 Long 自动序列化为字符串类型
                .addSerializer(Long.class, NumberSerializer.INSTANCE)
                .addSerializer(Long.TYPE, NumberSerializer.INSTANCE)
                .addSerializer(LocalDate.class, LocalDateSerializer.INSTANCE)
                .addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE)
                .addSerializer(LocalTime.class, LocalTimeSerializer.INSTANCE)
                .addDeserializer(LocalTime.class, LocalTimeDeserializer.INSTANCE);

        if (jacksonProperties.isTimestampLocalDateTime()) {
            // LocalDateTime 序列化、反序列化规则，使用 Long 时间戳
            simpleModule
                    .addSerializer(LocalDateTime.class, TimestampLocalDateTimeSerializer.INSTANCE)
                    .addDeserializer(LocalDateTime.class, TimestampLocalDateTimeDeserializer.INSTANCE);
        }

        if (jacksonProperties.isNumToStr()) {
            simpleModule
                    .addSerializer(Long.class, ToStringSerializer.instance)
                    .addSerializer(Long.TYPE, ToStringSerializer.instance)
                    .addSerializer(BigInteger.class, ToStringSerializer.instance)
                    .addSerializer(BigDecimal.class, ToStringSerializer.instance)
                    .addSerializer(Float.class, ToStringSerializer.instance)
                    .addSerializer(Float.TYPE, ToStringSerializer.instance)
                    .addSerializer(Double.class, ToStringSerializer.instance)
                    .addSerializer(Double.TYPE, ToStringSerializer.instance);
        }
        // 注册到 objectMapper
        objectMapper.registerModule(simpleModule);

        // 设置 objectMapper 到 JacksonUtils
        JacksonUtils.init(objectMapper);
    }
}