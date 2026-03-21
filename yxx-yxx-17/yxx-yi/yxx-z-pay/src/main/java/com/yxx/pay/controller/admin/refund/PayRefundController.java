package com.yxx.pay.controller.admin.refund;

import com.yxx.common.annotation.Log;
import com.yxx.common.core.controller.BaseControllerPlus;
import com.yxx.common.core.domain.PageResult;
import com.yxx.common.core.domain.R;
import com.yxx.common.enums.BusinessType;
import com.yxx.pay.core.domain.refund.PayRefundDO;
import com.yxx.pay.core.entity.vo.refund.PayRefundPageReqVO;
import com.yxx.pay.core.entity.vo.refund.PayRefundRespVO;
import com.yxx.pay.service.refund.PayRefundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "管理后台 - 退款订单")
@RestController
@RequestMapping("/pay/refund")
@Validated
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class PayRefundController extends BaseControllerPlus {

    private final PayRefundService payRefundService;

    @GetMapping("/get")
    @Operation(summary = "获得退款订单")
    @Parameter(name = "id", description = "退款编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermi('pay:refund:query')")
    public R<PayRefundRespVO> getRefund(@RequestParam("id") Long id) {
        PayRefundDO refund = payRefundService.getRefund(id);
        return R.ok(payRefundService.convertBean(refund, PayRefundRespVO.class));
    }

    @GetMapping("/get-by-no")
    @Operation(summary = "根据退款单号获得退款订单")
    @Parameter(name = "refundNo", description = "退款单号", required = true, example = "REF123456789")
    @PreAuthorize("@ss.hasPermi('pay:refund:query')")
    public R<PayRefundRespVO> getRefundByNo(@RequestParam("refundNo") String refundNo) {
        PayRefundDO refund = payRefundService.getRefundByNo(refundNo);
        return R.ok(payRefundService.convertBean(refund, PayRefundRespVO.class));
    }

    @GetMapping("/get-by-merchant-refund-id")
    @Operation(summary = "根据商户退款单号获得退款订单")
    @Parameter(name = "merchantRefundId", description = "商户退款单号", required = true, example = "MERCHANT_REFUND_001")
    @PreAuthorize("@ss.hasPermi('pay:refund:query')")
    public R<PayRefundRespVO> getRefundByMerchantRefundId(@RequestParam("merchantRefundId") String merchantRefundId) {
        PayRefundDO refund = payRefundService.getRefundByMerchantRefundId(merchantRefundId);
        return R.ok(payRefundService.convertBean(refund, PayRefundRespVO.class));
    }

    @GetMapping("/list-by-order")
    @Operation(summary = "获得订单的退款列表")
    @Parameter(name = "orderId", description = "订单编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermi('pay:refund:query')")
    public R<java.util.List<PayRefundRespVO>> getRefundListByOrderId(@RequestParam("orderId") Long orderId) {
        java.util.List<PayRefundDO> list = payRefundService.getRefundListByOrderId(orderId);
        return R.ok(payRefundService.convertList(list, PayRefundRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得退款订单分页")
    @PreAuthorize("@ss.hasPermi('pay:refund:query')")
    public PageResult<PayRefundRespVO> getRefundPage(@ParameterObject PayRefundPageReqVO pageReqVO) {
        PageResult<PayRefundDO> pageResult = payRefundService.getRefundPage(pageReqVO);
        return payRefundService.getMyBatisPageResult(pageResult.getRows(), PayRefundRespVO.class);
    }

    @PostMapping("/sync")
    @Operation(summary = "同步退款状态")
    @Parameter(name = "id", description = "退款编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermi('pay:refund:sync')")
    public R<Void> syncRefund(@RequestParam("id") Long id) {
        payRefundService.syncRefundQuietly(id);
        return R.ok();
    }

}
