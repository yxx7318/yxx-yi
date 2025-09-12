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
     * 获取当前上下文的复制
     */
    public static Map<String, String> getContext() {
        return MDC.getCopyOfContextMap();
    }

    /**
     * 获取traceId
     */
    public static String getTraceId() {
        return MDC.get(Constants.TRACE_ID);
    }

    /**
     * 设置traceId的值
     */
    public static void setTraceId(String value) {
        MDC.put(Constants.TRACE_ID, value);
    }

    /**
     * 设置traceId如果不存在
     */
    public static String setTraceIdIfAbsent() {
        // 如果没有traceId则生成一个
        String traceId = MDC.get(Constants.TRACE_ID);
        if (traceId == null) {
            traceId = DateUtils.dateTimeNow() + "-" + UUID.fastUUID().toString().substring(0, 6);
            setTraceId(traceId);
        }
        return traceId;
    }

    /**
     * 移除traceId
     */
    public static void removeTraceId() {
        MDC.remove(Constants.TRACE_ID);
    }

    /**
     * 清除上下文
     */
    public static void clean() {
        MDC.clear();
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
