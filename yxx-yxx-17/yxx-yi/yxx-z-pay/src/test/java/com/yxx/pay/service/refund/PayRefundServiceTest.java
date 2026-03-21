package com.yxx.pay.service.refund;

import com.yxx.common.core.redis.RedisLockSimple;
import com.yxx.common.exception.ServiceException;
import com.yxx.pay.client.PayClient;
import com.yxx.pay.client.dto.refund.PayRefundRespDTO;
import com.yxx.pay.client.dto.refund.PayRefundUnifiedReqDTO;
import com.yxx.pay.core.domain.order.PayOrderDO;
import com.yxx.pay.core.domain.refund.PayRefundDO;
import com.yxx.pay.core.entity.dto.refund.PayRefundCreateReqDTO;
import com.yxx.pay.enums.order.PayOrderStatusEnum;
import com.yxx.pay.enums.refund.PayRefundStatusEnum;
import com.yxx.pay.mapper.PayRefundMapper;
import com.yxx.pay.service.channel.PayChannelService;
import com.yxx.pay.service.order.PayOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PayRefundServiceTest {

    @Mock
    private PayRefundMapper payRefundMapper;

    @Mock
    private PayOrderService payOrderService;

    @Mock
    private PayChannelService payChannelService;

    @Mock
    private RedisLockSimple redisLockSimple;

    @Mock
    private PayClient payClient;

    @InjectMocks
    private PayRefundServiceImpl payRefundService;

    private PayOrderDO testOrder;
    private PayRefundDO testRefund;

    @BeforeEach
    void setUp() {
        testOrder = PayOrderDO.builder()
                .orderId(1L)
                .orderNo("PAY123456789")
                .userId(100L)
                .merchantOrderId("MERCHANT_ORDER_001")
                .subject("测试商品")
                .payPrice(100)
                .orderStatus(PayOrderStatusEnum.SUCCESS.getStatus())
                .channelId(1L)
                .channelCode("wx_pub")
                .channelOrderNo("WX_ORDER_001")
                .refundPrice(0)
                .build();

        testRefund = PayRefundDO.builder()
                .refundId(1L)
                .refundNo("REF123456789")
                .orderId(1L)
                .userId(100L)
                .merchantOrderId("MERCHANT_ORDER_001")
                .merchantRefundId("MERCHANT_REFUND_001")
                .channelId(1L)
                .channelCode("wx_pub")
                .channelOrderNo("WX_ORDER_001")
                .payPrice(100)
                .refundPrice(50)
                .refundReason("用户申请退款")
                .refundStatus(PayRefundStatusEnum.WAITING.getStatus())
                .build();
    }

    @Test
    @DisplayName("创建退款 - 成功")
    void testCreateRefund_Success() {
        PayRefundCreateReqDTO reqDTO = new PayRefundCreateReqDTO();
        reqDTO.setUserId(100L);
        reqDTO.setMerchantOrderId("MERCHANT_ORDER_001");
        reqDTO.setMerchantRefundId("MERCHANT_REFUND_001");
        reqDTO.setPrice(50);
        reqDTO.setReason("用户申请退款");

        when(payOrderService.getOrderByMerchantOrderId("MERCHANT_ORDER_001")).thenReturn(testOrder);
        when(payRefundMapper.selectByMerchantRefundId("MERCHANT_REFUND_001")).thenReturn(null);
        when(payRefundMapper.selectListByOrderIdAndStatus(1L, PayRefundStatusEnum.WAITING.getStatus()))
                .thenReturn(Collections.emptyList());
        when(payRefundMapper.insert(any(PayRefundDO.class))).thenAnswer(invocation -> {
            PayRefundDO refund = invocation.getArgument(0);
            refund.setRefundId(1L);
            return 1;
        });
        when(payChannelService.getPayClient(1L)).thenReturn(payClient);

        PayRefundRespDTO respDTO = new PayRefundRespDTO();
        respDTO.setStatus(PayRefundStatusEnum.SUCCESS.getStatus());
        respDTO.setChannelRefundNo("WX_REFUND_001");
        when(payClient.unifiedRefund(any(PayRefundUnifiedReqDTO.class))).thenReturn(respDTO);
        doNothing().when(payOrderService).updateOrderRefundPrice(1L, 50);

        Long refundId = payRefundService.createRefund(reqDTO);

        assertNotNull(refundId);
        verify(payRefundMapper).insert(any(PayRefundDO.class));
        verify(payOrderService).updateOrderRefundPrice(1L, 50);
    }

    @Test
    @DisplayName("创建退款 - 订单不存在")
    void testCreateRefund_OrderNotFound() {
        PayRefundCreateReqDTO reqDTO = new PayRefundCreateReqDTO();
        reqDTO.setUserId(100L);
        reqDTO.setMerchantOrderId("NOT_EXIST_ORDER");
        reqDTO.setMerchantRefundId("MERCHANT_REFUND_001");
        reqDTO.setPrice(50);
        reqDTO.setReason("用户申请退款");

        when(payOrderService.getOrderByMerchantOrderId("NOT_EXIST_ORDER")).thenReturn(null);

        assertThrows(ServiceException.class, () -> payRefundService.createRefund(reqDTO));
    }

    @Test
    @DisplayName("创建退款 - 订单状态不允许退款")
    void testCreateRefund_OrderStatusNotAllowed() {
        testOrder.setOrderStatus(PayOrderStatusEnum.WAITING.getStatus());
        
        PayRefundCreateReqDTO reqDTO = new PayRefundCreateReqDTO();
        reqDTO.setUserId(100L);
        reqDTO.setMerchantOrderId("MERCHANT_ORDER_001");
        reqDTO.setMerchantRefundId("MERCHANT_REFUND_001");
        reqDTO.setPrice(50);
        reqDTO.setReason("用户申请退款");

        when(payOrderService.getOrderByMerchantOrderId("MERCHANT_ORDER_001")).thenReturn(testOrder);

        assertThrows(ServiceException.class, () -> payRefundService.createRefund(reqDTO));
    }

    @Test
    @DisplayName("创建退款 - 退款单已存在")
    void testCreateRefund_RefundExists() {
        PayRefundCreateReqDTO reqDTO = new PayRefundCreateReqDTO();
        reqDTO.setUserId(100L);
        reqDTO.setMerchantOrderId("MERCHANT_ORDER_001");
        reqDTO.setMerchantRefundId("MERCHANT_REFUND_001");
        reqDTO.setPrice(50);
        reqDTO.setReason("用户申请退款");

        when(payOrderService.getOrderByMerchantOrderId("MERCHANT_ORDER_001")).thenReturn(testOrder);
        when(payRefundMapper.selectByMerchantRefundId("MERCHANT_REFUND_001")).thenReturn(testRefund);

        assertThrows(ServiceException.class, () -> payRefundService.createRefund(reqDTO));
    }

    @Test
    @DisplayName("创建退款 - 有退款中的记录")
    void testCreateRefund_HasRefunding() {
        PayRefundCreateReqDTO reqDTO = new PayRefundCreateReqDTO();
        reqDTO.setUserId(100L);
        reqDTO.setMerchantOrderId("MERCHANT_ORDER_001");
        reqDTO.setMerchantRefundId("MERCHANT_REFUND_002");
        reqDTO.setPrice(50);
        reqDTO.setReason("用户申请退款");

        when(payOrderService.getOrderByMerchantOrderId("MERCHANT_ORDER_001")).thenReturn(testOrder);
        when(payRefundMapper.selectByMerchantRefundId("MERCHANT_REFUND_002")).thenReturn(null);
        when(payRefundMapper.selectListByOrderIdAndStatus(1L, PayRefundStatusEnum.WAITING.getStatus()))
                .thenReturn(Collections.singletonList(testRefund));

        assertThrows(ServiceException.class, () -> payRefundService.createRefund(reqDTO));
    }

    @Test
    @DisplayName("创建退款 - 退款金额超过可退金额")
    void testCreateRefund_RefundPriceExceed() {
        testOrder.setRefundPrice(80);

        PayRefundCreateReqDTO reqDTO = new PayRefundCreateReqDTO();
        reqDTO.setUserId(100L);
        reqDTO.setMerchantOrderId("MERCHANT_ORDER_001");
        reqDTO.setMerchantRefundId("MERCHANT_REFUND_002");
        reqDTO.setPrice(50);
        reqDTO.setReason("用户申请退款");

        when(payOrderService.getOrderByMerchantOrderId("MERCHANT_ORDER_001")).thenReturn(testOrder);
        when(payRefundMapper.selectByMerchantRefundId("MERCHANT_REFUND_002")).thenReturn(null);
        when(payRefundMapper.selectListByOrderIdAndStatus(1L, PayRefundStatusEnum.WAITING.getStatus()))
                .thenReturn(Collections.emptyList());

        assertThrows(ServiceException.class, () -> payRefundService.createRefund(reqDTO));
    }

    @Test
    @DisplayName("获取退款 - 根据ID")
    void testGetRefund() {
        when(payRefundMapper.selectByRefundId(1L)).thenReturn(testRefund);

        PayRefundDO result = payRefundService.getRefund(1L);

        assertNotNull(result);
        assertEquals("REF123456789", result.getRefundNo());
    }

    @Test
    @DisplayName("获取退款 - 根据商户退款单号")
    void testGetRefundByMerchantRefundId() {
        when(payRefundMapper.selectByMerchantRefundId("MERCHANT_REFUND_001")).thenReturn(testRefund);

        PayRefundDO result = payRefundService.getRefundByMerchantRefundId("MERCHANT_REFUND_001");

        assertNotNull(result);
        assertEquals("MERCHANT_REFUND_001", result.getMerchantRefundId());
    }

}
