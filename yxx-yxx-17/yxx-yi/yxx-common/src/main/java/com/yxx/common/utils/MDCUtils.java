package com.yxx.common.utils;

import com.yxx.common.constant.Constants;
import com.yxx.common.utils.uuid.UUID;
import org.slf4j.MDC;

import java.util.Map;

/**
 * MDC工具类
 */
public class MDCUtils {

    /**
     * 设置traceId的值
     */
    public static void setTraceIdValue(String value) {
        MDC.put(Constants.TRACE_ID, value);
    }

    /**
     * 设置traceId如果不存在
     */
    public static void setTraceIdIfAbsent() {
        // 如果父线程没有则生成一个
        if (MDC.get(Constants.TRACE_ID) == null) {
            MDC.put(Constants.TRACE_ID, UUID.fastUUID().toString());
        }
    }

    /**
     * 设置上下文如果父线程存在
     */
    public static void setContextIfExist(final Map<String, String> context) {
        if (context == null) {
            MDC.clear();
        } else {
            // 将父线程的上下文复制到子线程
            MDC.setContextMap(context);
        }
    }

}
