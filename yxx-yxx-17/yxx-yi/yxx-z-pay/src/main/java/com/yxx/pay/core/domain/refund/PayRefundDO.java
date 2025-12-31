package com.yxx.pay.core.domain.refund;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yxx.common.core.domain.BaseEntity;
import com.yxx.pay.client.dto.refund.PayRefundRespDTO;
import com.yxx.pay.core.domain.channel.PayChannelDO;
import com.yxx.pay.core.domain.order.PayOrderDO;
import com.yxx.pay.enums.PayChannelEnum;
import com.yxx.pay.enums.refund.PayRefundStatusEnum;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 支付退款单 DO
 * 一个支付订单，可以拥有多个支付退款单
 * <p>
 * 即 PayOrderDO : PayRefundDO = 1 : n
 *
 * @author yxx
 */
@TableName("pay_refund")
@KeySequence("pay_refund_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayRefundDO extends BaseEntity {

    /**
     * 退款单编号，数据库自增
     */
    @TableId
    private Long id;

    /**
     * 外部退款号，根据规则生成
     * <p>
     * 调用支付渠道时，使用该字段作为对接的退款号：
     * 1. 微信退款：对应 <a href="https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_4">申请退款</a> 的 out_refund_no 字段
     * 2. 支付宝退款：对应 <a href="https://opendocs.alipay.com/open/02e7go"统一收单交易退款接口></a> 的 out_request_no 字段
     */
    private String no;

    /**
     * 渠道编号
     * <p>
     * 关联 {@link PayChannelDO#getId()}
     */
    private Long channelId;

    /**
     * 渠道编码
     * <p>
     * 枚举 {@link PayChannelEnum}
     */
    private String channelCode;

    /**
     * 订单编号
     * <p>
     * 关联 {@link PayOrderDO#getId()}
     */
    private Long orderId;

    /**
     * 支付订单编号
     * <p>
     * 冗余 {@link PayOrderDO#getNo()}
     */
    private String orderNo;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 用户类型
     */
    private Integer userType;

    // ========== 商户相关字段 ==========
    /**
     * 商户订单编号
     * <p>
     * 例如说，内部系统 A 的订单号，需要保证每个 PayAppDO 唯一
     */
    private String merchantOrderId;

    /**
     * 商户退款订单号
     * <p>
     * 例如说，内部系统 A 的订单号，需要保证每个 PayAppDO 唯一
     */
    private String merchantRefundId;

    /**
     * 异步通知地址
     */
    private String notifyUrl;

    // ========== 退款相关字段 ==========
    /**
     * 退款状态
     * <p>
     * 枚举 {@link PayRefundStatusEnum}
     */
    private Integer status;

    /**
     * 支付金额，单位：分
     */
    private Integer payPrice;

    /**
     * 退款金额，单位：分
     */
    private Integer refundPrice;

    /**
     * 退款原因
     */
    private String reason;

    /**
     * 用户 IP
     */
    private String userIp;

    // ========== 渠道相关字段 ==========
    /**
     * 渠道订单号
     * <p>
     * 冗余 {@link PayOrderDO#getChannelOrderNo()}
     */
    private String channelOrderNo;

    /**
     * 渠道退款单号
     * <p>
     * 1. 微信退款：对应 <a href="https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_4">申请退款</a> 的 refund_id 字段
     * 2. 支付宝退款：没有字段
     */
    private String channelRefundNo;

    /**
     * 退款成功时间
     */
    private LocalDateTime successTime;

    /**
     * 调用渠道的错误码
     */
    private String channelErrorCode;

    /**
     * 调用渠道的错误提示
     */
    private String channelErrorMsg;

    /**
     * 支付渠道的同步/异步通知的内容
     * <p>
     * 对应 {@link PayRefundRespDTO#getRawData()}
     */
    private String channelNotifyData;

}
