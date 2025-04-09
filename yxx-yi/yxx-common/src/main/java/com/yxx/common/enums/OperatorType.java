package com.yxx.common.enums;

/**
 * 操作人类别
 */
public enum OperatorType {
    /**
     * 其它
     */
    OTHER(0, "其它"),

    /**
     * 后台用户
     */
    MANAGE(1, "后台用户"),

    /**
     * 手机端用户
     */
    MOBILE(2, "手机端用户");

    private final int code;

    private final String describe;

    OperatorType(int code, String describe) {
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
