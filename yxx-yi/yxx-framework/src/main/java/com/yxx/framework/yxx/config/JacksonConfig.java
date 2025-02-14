package com.yxx.framework.yxx.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 数字类型转换字符串配置类
 */

@Configuration
@ConditionalOnProperty(name = "spring.enabled.numToStr", havingValue = "true")
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> {
            // 将 Long 类型序列化为字符串(Long类型使用范围广，影响范围大，可能影响前端)
//            jacksonObjectMapperBuilder.serializerByType(Long.TYPE, ToStringSerializer.instance);
//            jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance);

            // 将 BigInteger 类型序列化为字符串
            jacksonObjectMapperBuilder.serializerByType(java.math.BigInteger.class, ToStringSerializer.instance);

            // 将 BigDecimal 类型序列化为字符串
            jacksonObjectMapperBuilder.serializerByType(java.math.BigDecimal.class, ToStringSerializer.instance);

            // 将 Float 类型序列化为字符串
            jacksonObjectMapperBuilder.serializerByType(Float.TYPE, ToStringSerializer.instance);
            jacksonObjectMapperBuilder.serializerByType(Float.class, ToStringSerializer.instance);

            // 将 Double 类型序列化为字符串
            jacksonObjectMapperBuilder.serializerByType(Double.TYPE, ToStringSerializer.instance);
            jacksonObjectMapperBuilder.serializerByType(Double.class, ToStringSerializer.instance);
        };
    }
}