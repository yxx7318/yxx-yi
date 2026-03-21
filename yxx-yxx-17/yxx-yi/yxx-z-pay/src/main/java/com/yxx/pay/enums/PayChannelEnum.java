package com.yxx.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum PayChannelEnum {

    WX_PUB("wx_pub", "微信 JSAPI 支付"), // 公众号网页
    WX_LITE("wx_lite", "微信小程序支付"),
    WX_APP("wx_app", "微信 App 支付"),
    WX_NATIVE("wx_native", "微信 Native 支付"),
    WX_WAP("wx_wap", "微信 Wap 网站支付"), // H5 网页
    WX_BAR("wx_bar", "微信付款码支付"),

    ALIPAY_PC("alipay_pc", "支付宝 PC 网站支付"),
    ALIPAY_WAP("alipay_wap", "支付宝 Wap 网站支付"),
    ALIPAY_APP("alipay_app", "支付宝App 支付"),
    ALIPAY_QR("alipay_qr", "支付宝扫码支付"),
    ALIPAY_BAR("alipay_bar", "支付宝条码支付"),

    MOCK("mock", "模拟支付");

    public static final Map<String, PayChannelEnum> ENUM_MAP = Arrays.stream(PayChannelEnum.values())
            .collect(Collectors.toMap(PayChannelEnum::getCode, entry -> entry));

    public static final List<PayChannelEnum> WECHAT_CHANNELS = Arrays.asList(
            WX_PUB, WX_LITE, WX_APP, WX_NATIVE, WX_WAP, WX_BAR
    );

    public static final List<PayChannelEnum> ALIPAY_CHANNELS = Arrays.asList(
            ALIPAY_PC, ALIPAY_WAP, ALIPAY_APP, ALIPAY_QR, ALIPAY_BAR
    );

    private final String code;
    private final String name;

    public static PayChannelEnum getByCode(String code) {
        return ENUM_MAP.get(code);
    }

    public static boolean isAlipay(String channelCode) {
        return StringUtils.startsWith(channelCode, "alipay_");
    }

    public static boolean isWeixin(String channelCode) {
        return StringUtils.startsWith(channelCode, "wx_");
    }

    public static List<PayChannelEnum> getWechatChannels() {
        return WECHAT_CHANNELS;
    }

    public static List<PayChannelEnum> getAlipayChannels() {
        return ALIPAY_CHANNELS;
    }

}
