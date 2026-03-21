package com.yxx.pay.enums;

import com.yxx.common.exception.ErrorCode;

public interface ErrorCodeConstants {

    // ========== CHANNEL 模块 1-007-001-000 ==========
    ErrorCode CHANNEL_NOT_FOUND = new ErrorCode(1_007_001_000, "支付渠道的配置不存在");
    ErrorCode CHANNEL_IS_DISABLE = new ErrorCode(1_007_001_001, "支付渠道已经禁用");
    ErrorCode CHANNEL_EXIST_SAME_CHANNEL_ERROR = new ErrorCode(1_007_001_004, "已存在相同的渠道");

    // ========== ORDER 模块 1-007-002-000 ==========
    ErrorCode PAY_ORDER_NOT_FOUND = new ErrorCode(1_007_002_000, "支付订单不存在");
    ErrorCode PAY_ORDER_STATUS_IS_NOT_WAITING = new ErrorCode(1_007_002_001, "支付订单不处于待支付");
    ErrorCode PAY_ORDER_STATUS_IS_SUCCESS = new ErrorCode(1_007_002_002, "订单已支付，请刷新页面");
    ErrorCode PAY_ORDER_IS_EXPIRED = new ErrorCode(1_007_002_003, "支付订单已经过期");
    ErrorCode PAY_ORDER_SUBMIT_CHANNEL_ERROR = new ErrorCode(1_007_002_004, "发起支付报错，错误码：{}，错误提示：{}");
    ErrorCode PAY_ORDER_REFUND_FAIL_STATUS_ERROR = new ErrorCode(1_007_002_005, "支付订单退款失败，原因：状态不是已支付或已退款");
    ErrorCode PAY_ORDER_MERCHANT_ORDER_EXISTS = new ErrorCode(1_007_002_006, "商户订单号已存在");

    // ========== ORDER 模块(拓展单) 1-007-003-000 ==========
    ErrorCode PAY_ORDER_EXTENSION_NOT_FOUND = new ErrorCode(1_007_003_000, "支付交易拓展单不存在");
    ErrorCode PAY_ORDER_EXTENSION_STATUS_IS_NOT_WAITING = new ErrorCode(1_007_003_001, "支付交易拓展单不处于待支付");
    ErrorCode PAY_ORDER_EXTENSION_IS_PAID = new ErrorCode(1_007_003_002, "订单已支付，请等待支付结果");

    // ========== 支付模块(退款) 1-007-006-000 ==========
    ErrorCode REFUND_PRICE_EXCEED = new ErrorCode(1_007_006_000, "退款金额超过订单可退款金额");
    ErrorCode REFUND_HAS_REFUNDING = new ErrorCode(1_007_006_002, "已经有退款在处理中");
    ErrorCode REFUND_EXISTS = new ErrorCode(1_007_006_003, "已经存在退款单");
    ErrorCode REFUND_NOT_FOUND = new ErrorCode(1_007_006_004, "支付退款单不存在");
    ErrorCode REFUND_STATUS_IS_NOT_WAITING = new ErrorCode(1_007_006_005, "支付退款单不处于待退款");
    ErrorCode REFUND_MERCHANT_REFUND_ID_EXISTS = new ErrorCode(1_007_006_006, "商户退款单号已存在");

}
