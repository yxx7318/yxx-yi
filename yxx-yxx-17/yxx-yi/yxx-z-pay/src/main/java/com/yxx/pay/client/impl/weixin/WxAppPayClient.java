package com.yxx.pay.client.impl.weixin;

import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.yxx.pay.client.dto.order.PayOrderRespDTO;
import com.yxx.pay.client.dto.order.PayOrderUnifiedReqDTO;
import com.yxx.pay.enums.PayChannelEnum;
import com.yxx.pay.enums.PayOrderDisplayModeEnum;
import lombok.extern.slf4j.Slf4j;

import static com.yxx.common.core.utils.JacksonUtils.toJsonString;

/**
 * 微信支付【App 支付】的 PayClient 实现类
 * <p>
 * 文档：<a href="https://pay.weixin.qq.com/doc/v3/merchant/4012062524">App 支付</a>
 *
 * @author yxx
 */
@Slf4j
public class WxAppPayClient extends AbstractWxPayClient {

    public WxAppPayClient(Long channelId, WxPayClientConfig config) {
        super(channelId, PayChannelEnum.WX_APP.getCode(), config);
    }

    @Override
    protected void doInit() {
        super.doInit(WxPayConstants.TradeType.APP);
    }

    @Override
    protected PayOrderRespDTO doUnifiedOrderV2(PayOrderUnifiedReqDTO reqDTO) throws WxPayException {
        // 构建 WxPayUnifiedOrderRequest 对象
        WxPayUnifiedOrderRequest request = buildPayUnifiedOrderRequestV2(reqDTO);
        // 执行请求
        WxPayAppOrderResult response = client.createOrder(request);

        // 转换结果
        return PayOrderRespDTO.waitingOf(PayOrderDisplayModeEnum.APP.getMode(), toJsonString(response),
                reqDTO.getOutTradeNo(), response);
    }

    @Override
    protected PayOrderRespDTO doUnifiedOrderV3(PayOrderUnifiedReqDTO reqDTO) throws WxPayException {
        // 构建 WxPayUnifiedOrderV3Request 对象
        WxPayUnifiedOrderV3Request request = buildPayUnifiedOrderRequestV3(reqDTO);
        // 执行请求
        WxPayUnifiedOrderV3Result.AppResult response = client.createOrderV3(TradeTypeEnum.APP, request);

        // 转换结果
        return PayOrderRespDTO.waitingOf(PayOrderDisplayModeEnum.APP.getMode(), toJsonString(response),
                reqDTO.getOutTradeNo(), response);
    }

}
