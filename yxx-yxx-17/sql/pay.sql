-- ----------------------------
-- 支付渠道表
-- 注意：支付渠道配置已迁移至 application-pay.yml
-- 此表仅用于记录渠道基本信息，不存储敏感配置
-- ----------------------------
DROP TABLE IF EXISTS pay_channel;
CREATE TABLE pay_channel
(
    channel_id          BIGINT(20)        NOT NULL AUTO_INCREMENT   COMMENT '渠道编号',
    channel_code        VARCHAR(32)       NOT NULL                  COMMENT '渠道编码',
    channel_name        VARCHAR(64)       NOT NULL DEFAULT ''       COMMENT '渠道名称',
    channel_config      TEXT              NULL                      COMMENT '渠道配置(JSON格式)',
    channel_fee_rate    DECIMAL(5,2)      DEFAULT 0                 COMMENT '渠道手续费率(百分比)',
    status              TINYINT           DEFAULT 0                 COMMENT '状态(0正常 1停用)',
    create_by_id        BIGINT(20)        DEFAULT 0                 COMMENT '创建者Id',
    create_by_name      VARCHAR(64)       DEFAULT ''                COMMENT '创建者名称',
    create_time         DATETIME          NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by_id        BIGINT(20)        DEFAULT 0                 COMMENT '更新者Id',
    update_by_name      VARCHAR(64)       DEFAULT ''                COMMENT '更新者名称',
    update_time         DATETIME          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark              VARCHAR(500)      DEFAULT NULL              COMMENT '备注',
    PRIMARY KEY (channel_id)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='支付渠道表';

-- ----------------------------
-- 支付订单表
-- ----------------------------
DROP TABLE IF EXISTS pay_order;
CREATE TABLE pay_order
(
    order_id            BIGINT(20)        NOT NULL AUTO_INCREMENT   COMMENT '订单编号',
    order_no            VARCHAR(64)       NOT NULL                  COMMENT '支付订单号',
    user_id             BIGINT(20)        NOT NULL                  COMMENT '用户编号',
    merchant_order_id   VARCHAR(64)       NOT NULL                  COMMENT '商户订单编号',
    subject             VARCHAR(32)       NOT NULL                  COMMENT '商品标题',
    body                VARCHAR(128)      DEFAULT NULL              COMMENT '商品描述',
    channel_id          BIGINT(20)        DEFAULT NULL              COMMENT '渠道编号',
    channel_code        VARCHAR(32)       DEFAULT NULL              COMMENT '渠道编码',
    channel_user_id     VARCHAR(64)       DEFAULT NULL              COMMENT '渠道用户编号(openid等)',
    channel_order_no    VARCHAR(64)       DEFAULT NULL              COMMENT '渠道订单号',
    pay_price           INT               NOT NULL                  COMMENT '支付金额(分)',
    channel_fee_rate    DECIMAL(5,2)      DEFAULT 0                 COMMENT '渠道手续费率',
    channel_fee_price   INT               DEFAULT 0                 COMMENT '渠道手续费(分)',
    order_status        TINYINT           NOT NULL DEFAULT 0        COMMENT '支付状态(0待支付 10成功 20退款 30关闭)',
    expire_time         DATETIME          NOT NULL                  COMMENT '订单失效时间',
    success_time        DATETIME          DEFAULT NULL              COMMENT '支付成功时间',
    extension_id        BIGINT(20)        DEFAULT NULL              COMMENT '成功扩展单编号',
    refund_price        INT               NOT NULL DEFAULT 0        COMMENT '已退款金额(分)',
    create_by_id        BIGINT(20)        DEFAULT 0                 COMMENT '创建者Id',
    create_by_name      VARCHAR(64)       DEFAULT ''                COMMENT '创建者名称',
    create_time         DATETIME          NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by_id        BIGINT(20)        DEFAULT 0                 COMMENT '更新者Id',
    update_by_name      VARCHAR(64)       DEFAULT ''                COMMENT '更新者名称',
    update_time         DATETIME          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark              VARCHAR(500)      DEFAULT NULL              COMMENT '备注',
    PRIMARY KEY (order_id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_merchant_order_id (merchant_order_id),
    KEY idx_user_id (user_id),
    KEY idx_order_status (order_status),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='支付订单表';

-- ----------------------------
-- 支付订单扩展表
-- ----------------------------
DROP TABLE IF EXISTS pay_order_extension;
CREATE TABLE pay_order_extension
(
    extension_id        BIGINT(20)        NOT NULL AUTO_INCREMENT   COMMENT '扩展编号',
    order_id            BIGINT(20)        NOT NULL                  COMMENT '订单编号',
    order_no            VARCHAR(64)       NOT NULL                  COMMENT '支付订单号',
    channel_id          BIGINT(20)        NOT NULL                  COMMENT '渠道编号',
    channel_code        VARCHAR(32)       NOT NULL                  COMMENT '渠道编码',
    user_ip             VARCHAR(50)       DEFAULT NULL              COMMENT '用户IP',
    order_status        TINYINT           NOT NULL DEFAULT 0        COMMENT '支付状态',
    channel_extras      TEXT              DEFAULT NULL              COMMENT '渠道额外参数(JSON)',
    channel_notify_data TEXT              DEFAULT NULL              COMMENT '渠道回调数据',
    channel_error_code  VARCHAR(64)       DEFAULT NULL              COMMENT '渠道错误码',
    channel_error_msg   VARCHAR(255)      DEFAULT NULL              COMMENT '渠道错误信息',
    create_by_id        BIGINT(20)        DEFAULT 0                 COMMENT '创建者Id',
    create_by_name      VARCHAR(64)       DEFAULT ''                COMMENT '创建者名称',
    create_time         DATETIME          NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by_id        BIGINT(20)        DEFAULT 0                 COMMENT '更新者Id',
    update_by_name      VARCHAR(64)       DEFAULT ''                COMMENT '更新者名称',
    update_time         DATETIME          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark              VARCHAR(500)      DEFAULT NULL              COMMENT '备注',
    PRIMARY KEY (extension_id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_order_id (order_id)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='支付订单扩展表';

-- ----------------------------
-- 支付退款表
-- ----------------------------
DROP TABLE IF EXISTS pay_refund;
CREATE TABLE pay_refund
(
    refund_id           BIGINT(20)        NOT NULL AUTO_INCREMENT   COMMENT '退款编号',
    refund_no           VARCHAR(64)       NOT NULL                  COMMENT '退款单号',
    order_id            BIGINT(20)        NOT NULL                  COMMENT '订单编号',
    user_id             BIGINT(20)        NOT NULL                  COMMENT '用户编号',
    merchant_order_id   VARCHAR(64)       NOT NULL                  COMMENT '商户订单编号',
    merchant_refund_id  VARCHAR(64)       NOT NULL                  COMMENT '商户退款单号',
    channel_id          BIGINT(20)        NOT NULL                  COMMENT '渠道编号',
    channel_code        VARCHAR(32)       NOT NULL                  COMMENT '渠道编码',
    channel_order_no    VARCHAR(64)       DEFAULT NULL              COMMENT '渠道订单号',
    channel_refund_no   VARCHAR(64)       DEFAULT NULL              COMMENT '渠道退款单号',
    pay_price           INT               NOT NULL                  COMMENT '原支付金额(分)',
    refund_price        INT               NOT NULL                  COMMENT '退款金额(分)',
    refund_reason       VARCHAR(256)      DEFAULT NULL              COMMENT '退款原因',
    refund_status       TINYINT           NOT NULL DEFAULT 0        COMMENT '退款状态(0待退款 10成功 20失败)',
    success_time        DATETIME          DEFAULT NULL              COMMENT '退款成功时间',
    user_ip             VARCHAR(50)       DEFAULT NULL              COMMENT '用户IP',
    channel_error_code  VARCHAR(64)       DEFAULT NULL              COMMENT '渠道错误码',
    channel_error_msg   VARCHAR(255)      DEFAULT NULL              COMMENT '渠道错误信息',
    channel_notify_data TEXT              DEFAULT NULL              COMMENT '渠道回调数据',
    create_by_id        BIGINT(20)        DEFAULT 0                 COMMENT '创建者Id',
    create_by_name      VARCHAR(64)       DEFAULT ''                COMMENT '创建者名称',
    create_time         DATETIME          NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by_id        BIGINT(20)        DEFAULT 0                 COMMENT '更新者Id',
    update_by_name      VARCHAR(64)       DEFAULT ''                COMMENT '更新者名称',
    update_time         DATETIME          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark              VARCHAR(500)      DEFAULT NULL              COMMENT '备注',
    PRIMARY KEY (refund_id),
    UNIQUE KEY uk_refund_no (refund_no),
    KEY idx_order_id (order_id),
    KEY idx_user_id (user_id),
    KEY idx_refund_status (refund_status)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='支付退款表';
