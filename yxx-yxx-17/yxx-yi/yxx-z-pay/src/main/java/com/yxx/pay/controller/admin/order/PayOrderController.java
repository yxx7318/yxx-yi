package com.yxx.pay.controller.admin.order;

import com.yxx.common.annotation.Log;
import com.yxx.common.annotation.RepeatSubmit;
import com.yxx.common.core.controller.BaseControllerPlus;
import com.yxx.common.core.domain.PageResult;
import com.yxx.common.core.domain.R;
import com.yxx.common.enums.BusinessType;
import com.yxx.common.utils.ip.IpUtils;
import com.yxx.pay.core.domain.order.PayOrderDO;
import com.yxx.pay.core.entity.vo.order.PayOrderPageReqVO;
import com.yxx.pay.core.entity.vo.order.PayOrderRespVO;
import com.yxx.pay.core.entity.vo.order.PayOrderSubmitReqVO;
import com.yxx.pay.core.entity.vo.order.PayOrderSubmitRespVO;
import com.yxx.pay.service.order.PayOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "支付订单")
@RestController
@RequestMapping("/pay/order")
@Validated
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class PayOrderController extends BaseControllerPlus {

    private final PayOrderService payOrderService;

    @GetMapping("/get")
    @Operation(summary = "获得支付订单")
    @Parameter(name = "id", description = "订单编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermi('pay:order:query')")
    public R<PayOrderRespVO> getOrder(@RequestParam("id") Long id) {
        PayOrderDO order = payOrderService.getOrder(id);
        return R.ok(payOrderService.convertBean(order, PayOrderRespVO.class));
    }

    @GetMapping("/get-by-no")
    @Operation(summary = "根据订单号获得支付订单")
    @Parameter(name = "orderNo", description = "支付订单号", required = true, example = "PAY123456789")
    @PreAuthorize("@ss.hasPermi('pay:order:query')")
    public R<PayOrderRespVO> getOrderByNo(@RequestParam("orderNo") String orderNo) {
        PayOrderDO order = payOrderService.getOrderByNo(orderNo);
        return R.ok(payOrderService.convertBean(order, PayOrderRespVO.class));
    }

    @GetMapping("/get-by-merchant")
    @Operation(summary = "根据商户订单号获得支付订单")
    @Parameter(name = "merchantOrderId", description = "商户订单号", required = true, example = "ORDER_001")
    public R<PayOrderRespVO> getOrderByMerchantOrderId(@RequestParam("merchantOrderId") String merchantOrderId) {
        PayOrderDO order = payOrderService.getOrderByMerchantOrderId(merchantOrderId);
        return R.ok(payOrderService.convertBean(order, PayOrderRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得支付订单分页")
    @PreAuthorize("@ss.hasPermi('pay:order:query')")
    public PageResult<PayOrderRespVO> getOrderPage(@ParameterObject PayOrderPageReqVO pageReqVO) {
        PageResult<PayOrderDO> pageResult = payOrderService.getOrderPage(pageReqVO);
        return payOrderService.getMyBatisPageResult(pageResult.getRows(), PayOrderRespVO.class);
    }

    @PostMapping("/submit")
    @Operation(summary = "提交支付订单")
    @Log(title = "支付订单", businessType = BusinessType.UPDATE)
    @RepeatSubmit(interval = 3000, message = "请勿重复提交支付订单")
    public R<PayOrderSubmitRespVO> submitOrder(@RequestBody @Validated PayOrderSubmitReqVO reqVO) {
        String userIp = IpUtils.getIpAddr();
        PayOrderSubmitRespVO respVO = payOrderService.submitOrder(reqVO, userIp);
        return R.ok(respVO);
    }

    @PostMapping("/sync")
    @Operation(summary = "同步支付订单状态")
    @Parameter(name = "id", description = "订单编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermi('pay:order:sync')")
    @Log(title = "支付订单", businessType = BusinessType.UPDATE)
    public R<Void> syncOrder(@RequestParam("id") Long id) {
        payOrderService.syncOrderQuietly(id);
        return R.ok();
    }

}
