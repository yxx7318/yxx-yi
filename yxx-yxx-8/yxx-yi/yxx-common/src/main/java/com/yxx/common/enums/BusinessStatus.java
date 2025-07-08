package com.yxx.common.enums;

/**
 * 操作状态
 */
public enum BusinessStatus {
    /**
     * 成功
     */
    SUCCESS(0, "成功"),

    /**
     * 失败
     */
    FAIL(1, "失败");

    private final int code;

    private final String describe;

    BusinessStatus(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public int getCode() {
        return code;
    }

    public String getDescribe() {
        return describe;
    }
}
