package com.yxx.pay.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@Data
@ConfigurationProperties(prefix = "yxx.pay")
public class PayProperties {

    private static final String ORDER_NO_PREFIX = "P";
    private static final String REFUND_NO_PREFIX = "R";

    @NotEmpty(message = "支付回调地址不能为空")
    @URL(message = "支付回调地址的格式必须是 URL")
    private String orderNotifyUrl;

    @NotEmpty(message = "退款回调地址不能为空")
    @URL(message = "退款回调地址的格式必须是 URL")
    private String refundNotifyUrl;

    @NotEmpty(message = "支付订单 no 的前缀不能为空")
    private String orderNoPrefix = ORDER_NO_PREFIX;

    @NotEmpty(message = "退款订单 no 的前缀不能为空")
    private String refundNoPrefix = REFUND_NO_PREFIX;

    @Valid
    private Wechat wechat = new Wechat();

    @Valid
    private Alipay alipay = new Alipay();

    @Data
    public static class Wechat {

        private Boolean enabled = false;

        private String appId;

        private String mchId;

        private String apiV3Key;

        private String privateKeyContent;

        private String certSerialNo;

        private String publicKeyContent;

        private String publicKeyId;

    }

    @Data
    public static class Alipay {

        private Boolean enabled = false;

        private String serverUrl = "https://openapi.alipay.com/gateway.do";

        private String appId;

        private String signType = "RSA2";

        private Integer mode = 1;

        private String privateKey;

        private String alipayPublicKey;

        private String appCertContent;

        private String alipayPublicCertContent;

        private String rootCertContent;

        private String encryptType;

        private String encryptKey;

    }

}
