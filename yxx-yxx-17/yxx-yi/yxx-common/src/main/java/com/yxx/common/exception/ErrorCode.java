package com.yxx.common.exception;

/**
 * 错误码对象
 * <p>
 * 业务异常错误码，占用 [1 000 000 000, +∞)
 * 模块 pay 错误码区间 [1-007-000-000 ~ 1-008-000-000)
 * <p>
 */
public class ErrorCode {

    /**
     * 错误码
     */
    private final Integer code;
    /**
     * 错误提示
     */
    private final String msg;

    public ErrorCode(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
