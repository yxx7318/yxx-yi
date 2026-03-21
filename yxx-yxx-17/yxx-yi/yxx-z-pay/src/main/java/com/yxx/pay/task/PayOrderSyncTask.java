package com.yxx.pay.task;

import com.yxx.pay.service.order.PayOrderService;
import com.yxx.pay.service.refund.PayRefundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class PayOrderSyncTask {

    private final PayOrderService payOrderService;
    private final PayRefundService payRefundService;

    @Scheduled(fixedRate = 60000)
    public void syncOrder() {
        LocalDateTime minCreateTime = LocalDateTime.now().minusMinutes(5);
        int count = payOrderService.syncOrder(minCreateTime);
        if (count > 0) {
            log.info("[syncOrder][同步支付订单 {} 个]", count);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void expireOrder() {
        int count = payOrderService.expireOrder();
        if (count > 0) {
            log.info("[expireOrder][关闭过期订单 {} 个]", count);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void syncRefund() {
        LocalDateTime minCreateTime = LocalDateTime.now().minusMinutes(5);
        int count = payRefundService.syncRefund(minCreateTime);
        if (count > 0) {
            log.info("[syncRefund][同步退款订单 {} 个]", count);
        }
    }

}
