//package com.yxx.pay.controller;
//
//import com.yxx.common.annotation.Anonymous;
//import com.yxx.pay.client.PayClient;
//import com.yxx.pay.client.dto.order.PayOrderRespDTO;
//import com.yxx.pay.client.dto.refund.PayRefundRespDTO;
//import com.yxx.pay.service.channel.PayChannelService;
//import com.yxx.pay.service.order.PayOrderService;
//import com.yxx.pay.service.refund.PayRefundService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//import static com.yxx.pay.enums.ErrorCodeConstants.CHANNEL_NOT_FOUND;
//
//@Tag(name = "管理后台 - 回调通知")
//@RestController
//@RequestMapping("/pay/notify")
//@Validated
//@Slf4j
//@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
//public class PayNotifyController {
//
//    private final PayOrderService orderService;
//
//    private final PayRefundService refundService;
//
//    private final PayTransferService payTransferService;
//
//    private final PayNotifyService notifyService;
//
//    private final PayAppService appService;
//
//    private final PayChannelService channelService;
//
//    @PostMapping(value = "/order/{channelId}")
//    @Operation(summary = "支付渠道的统一【支付】回调")
//    @Anonymous
//    public String notifyOrder(@PathVariable("channelId") Long channelId,
//                              @RequestParam(required = false) Map<String, String> params,
//                              @RequestBody(required = false) String body,
//                              @RequestHeader Map<String, String> headers) {
//        log.info("[notifyOrder][channelId({}) 回调数据({}/{})]", channelId, params, body);
//        // 1. 校验支付渠道是否存在
//        PayClient payClient = channelService.getPayClient(channelId);
//        if (payClient == null) {
//            log.error("[notifyOrder][渠道编号({}) 找不到对应的支付客户端]", channelId);
//            throw exception(CHANNEL_NOT_FOUND);
//        }
//
//        // 2. 解析通知数据
//        PayOrderRespDTO notify = payClient.parseOrderNotify(params, body, headers);
//        orderService.notifyOrder(channelId, notify);
//        return "success";
//    }
//
//    @PostMapping(value = "/refund/{channelId}")
//    @Operation(summary = "支付渠道的统一【退款】回调")
//    @Anonymous
//    public String notifyRefund(@PathVariable("channelId") Long channelId,
//                               @RequestParam(required = false) Map<String, String> params,
//                               @RequestBody(required = false) String body,
//                               @RequestHeader Map<String, String> headers) {
//        log.info("[notifyRefund][channelId({}) 回调数据({}/{})]", channelId, params, body);
//        // 1. 校验支付渠道是否存在
//        PayClient payClient = channelService.getPayClient(channelId);
//        if (payClient == null) {
//            log.error("[notifyRefund][渠道编号({}) 找不到对应的支付客户端]", channelId);
//            throw exception(CHANNEL_NOT_FOUND);
//        }
//
//        // 2. 解析通知数据
//        PayRefundRespDTO notify = payClient.parseRefundNotify(params, body, headers);
//        refundService.notifyRefund(channelId, notify);
//        return "success";
//    }
//
//    @PostMapping(value = "/transfer/{channelId}")
//    @Operation(summary = "支付渠道的统一【转账】回调")
//    @Anonymous
//    public String notifyTransfer(@PathVariable("channelId") Long channelId,
//                                 @RequestParam(required = false) Map<String, String> params,
//                                 @RequestBody(required = false) String body,
//                                 @RequestHeader Map<String, String> headers) {
//        log.info("[notifyTransfer][channelId({}) 回调数据({}/{})]", channelId, params, body);
//        // 1. 校验支付渠道是否存在
//        PayClient payClient = channelService.getPayClient(channelId);
//        if (payClient == null) {
//            log.error("[notifyTransfer][渠道编号({}) 找不到对应的支付客户端]", channelId);
//            throw exception(CHANNEL_NOT_FOUND);
//        }
//
//        // 2. 解析通知数据
//        PayTransferRespDTO notify = payClient.parseTransferNotify(params, body, headers);
//        payTransferService.notifyTransfer(channelId, notify);
//        return "success";
//    }
//}
