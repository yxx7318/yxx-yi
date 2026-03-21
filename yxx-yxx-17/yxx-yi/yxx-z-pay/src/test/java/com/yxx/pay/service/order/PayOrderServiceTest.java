package com.yxx.pay.service.order;

import com.yxx.common.core.domain.PageResult;
import com.yxx.common.core.redis.RedisLockSimple;
import com.yxx.common.exception.ServiceException;
import com.yxx.pay.client.PayClient;
import com.yxx.pay.client.dto.order.PayOrderRespDTO;
import com.yxx.pay.client.dto.order.PayOrderUnifiedReqDTO;
import com.yxx.pay.core.domain.channel.PayChannelDO;
import com.yxx.pay.core.domain.order.PayOrderDO;
import com.yxx.pay.core.domain.order.PayOrderExtensionDO;
import com.yxx.pay.core.entity.dto.order.PayOrderCreateReqDTO;
import com.yxx.pay.core.entity.vo.order.PayOrderSubmitReqVO;
import com.yxx.pay.core.entity.vo.order.PayOrderSubmitRespVO;
import com.yxx.pay.enums.order.PayOrderStatusEnum;
import com.yxx.pay.mapper.PayOrderExtensionMapper;
import com.yxx.pay.mapper.PayOrderMapper;
import com.yxx.pay.service.channel.PayChannelService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PayOrderServiceTest {

    @Mock
    private PayOrderMapper payOrderMapper;

    @Mock
    private PayOrderExtensionMapper payOrderExtensionMapper;

    @Mock
    private PayChannelService payChannelService;

    @Mock
    private RedisLockSimple redisLockSimple;

    @Mock
    private PayClient payClient;

    @InjectMocks
    private PayOrderServiceImpl payOrderService;

    private PayOrderDO testOrder;
    private PayChannelDO testChannel;

    @BeforeEach
    void setUp() {
        testOrder = PayOrderDO.builder()
                .orderId(1L)
                .orderNo("PAY123456789")
                .userId(100L)
                .merchantOrderId("MERCHANT_ORDER_001")
                .subject("测试商品")
                .body("测试商品描述")
                .payPrice(100)
                .orderStatus(PayOrderStatusEnum.WAITING.getStatus())
                .expireTime(LocalDateTime.now().plusMinutes(30))
                .refundPrice(0)
                .build();

        testChannel = PayChannelDO.builder()
                .channelId(1L)
                .channelCode("wx_pub")
                .channelName("微信JSAPI支付")
                .channelFeeRate(new BigDecimal("0.60"))
                .status(0)
                .build();
    }

    @Test
    @DisplayName("创建订单 - 成功")
    void testCreateOrder_Success() {
        PayOrderCreateReqDTO reqDTO = new PayOrderCreateReqDTO();
        reqDTO.setUserId(100L);
        reqDTO.setMerchantOrderId("MERCHANT_ORDER_001");
        reqDTO.setSubject("测试商品");
        reqDTO.setBody("测试商品描述");
        reqDTO.setPayPrice(100);

        when(payOrderMapper.selectByMerchantOrderId(anyString())).thenReturn(null);
        when(payOrderMapper.insert(any(PayOrderDO.class))).thenAnswer(invocation -> {
            PayOrderDO order = invocation.getArgument(0);
            order.setOrderId(1L);
            return 1;
        });

        Long orderId = payOrderService.createOrder(reqDTO);

        assertNotNull(orderId);
        verify(payOrderMapper).selectByMerchantOrderId("MERCHANT_ORDER_001");
        verify(payOrderMapper).insert(any(PayOrderDO.class));
    }

    @Test
    @DisplayName("创建订单 - 订单已存在且未过期")
    void testCreateOrder_OrderExistsNotExpired() {
        PayOrderCreateReqDTO reqDTO = new PayOrderCreateReqDTO();
        reqDTO.setUserId(100L);
        reqDTO.setMerchantOrderId("MERCHANT_ORDER_001");
        reqDTO.setSubject("测试商品");
        reqDTO.setPayPrice(100);

        testOrder.setExpireTime(LocalDateTime.now().plusMinutes(10));
        when(payOrderMapper.selectByMerchantOrderId(anyString())).thenReturn(testOrder);

        Long orderId = payOrderService.createOrder(reqDTO);

        assertEquals(1L, orderId);
        verify(payOrderMapper, never()).insert(any(PayOrderDO.class));
    }

    @Test
    @DisplayName("创建订单 - 订单已存在且已支付")
    void testCreateOrder_OrderAlreadyPaid() {
        PayOrderCreateReqDTO reqDTO = new PayOrderCreateReqDTO();
        reqDTO.setUserId(100L);
        reqDTO.setMerchantOrderId("MERCHANT_ORDER_001");
        reqDTO.setSubject("测试商品");
        reqDTO.setPayPrice(100);

        testOrder.setOrderStatus(PayOrderStatusEnum.SUCCESS.getStatus());
        when(payOrderMapper.selectByMerchantOrderId(anyString())).thenReturn(testOrder);

        assertThrows(ServiceException.class, () -> payOrderService.createOrder(reqDTO));
    }

    @Test
    @DisplayName("提交支付 - 成功")
    void testSubmitOrder_Success() {
        PayOrderSubmitReqVO reqVO = new PayOrderSubmitReqVO();
        reqVO.setOrderId(1L);
        reqVO.setChannelCode("wx_pub");
        reqVO.setUserIp("127.0.0.1");

        Map<String, String> channelExtras = new HashMap<>();
        channelExtras.put("openid", "oUpF8uMuAJ");
        reqVO.setChannelExtras(channelExtras);

        when(payOrderMapper.selectByOrderId(1L)).thenReturn(testOrder);
        when(payChannelService.validPayChannel("wx_pub")).thenReturn(testChannel);
        when(payChannelService.getPayClient(1L)).thenReturn(payClient);
        when(payOrderExtensionMapper.insert(any(PayOrderExtensionDO.class))).thenReturn(1);
        when(payOrderExtensionMapper.updateById(any(PayOrderExtensionDO.class))).thenReturn(1);

        PayOrderRespDTO respDTO = new PayOrderRespDTO();
        respDTO.setStatus(PayOrderStatusEnum.WAITING.getStatus());
        respDTO.setDisplayMode("qr_code");
        respDTO.setDisplayContent("weixin://wxpay/bizpayurl?pr=xxx");
        when(payClient.unifiedOrder(any(PayOrderUnifiedReqDTO.class))).thenReturn(respDTO);

        PayOrderSubmitRespVO result = payOrderService.submitOrder(reqVO, "127.0.0.1");

        assertNotNull(result);
        assertEquals("qr_code", result.getDisplayMode());
        assertNotNull(result.getDisplayContent());
    }

    @Test
    @DisplayName("提交支付 - 订单不存在")
    void testSubmitOrder_OrderNotFound() {
        PayOrderSubmitReqVO reqVO = new PayOrderSubmitReqVO();
        reqVO.setOrderId(999L);
        reqVO.setChannelCode("wx_pub");

        when(payOrderMapper.selectByOrderId(999L)).thenReturn(null);

        assertThrows(ServiceException.class, () -> payOrderService.submitOrder(reqVO, "127.0.0.1"));
    }

    @Test
    @DisplayName("提交支付 - 订单已过期")
    void testSubmitOrder_OrderExpired() {
        PayOrderSubmitReqVO reqVO = new PayOrderSubmitReqVO();
        reqVO.setOrderId(1L);
        reqVO.setChannelCode("wx_pub");

        testOrder.setExpireTime(LocalDateTime.now().minusMinutes(1));
        when(payOrderMapper.selectByOrderId(1L)).thenReturn(testOrder);

        assertThrows(ServiceException.class, () -> payOrderService.submitOrder(reqVO, "127.0.0.1"));
    }

    @Test
    @DisplayName("获取订单 - 根据ID")
    void testGetOrder() {
        when(payOrderMapper.selectByOrderId(1L)).thenReturn(testOrder);

        PayOrderDO result = payOrderService.getOrder(1L);

        assertNotNull(result);
        assertEquals("PAY123456789", result.getOrderNo());
    }

    @Test
    @DisplayName("获取订单 - 根据商户订单号")
    void testGetOrderByMerchantOrderId() {
        when(payOrderMapper.selectByMerchantOrderId("MERCHANT_ORDER_001")).thenReturn(testOrder);

        PayOrderDO result = payOrderService.getOrderByMerchantOrderId("MERCHANT_ORDER_001");

        assertNotNull(result);
        assertEquals("MERCHANT_ORDER_001", result.getMerchantOrderId());
    }

    @Test
    @DisplayName("过期订单处理")
    void testExpireOrder() {
        testOrder.setExpireTime(LocalDateTime.now().minusMinutes(1));
        when(payOrderMapper.selectListByStatusAndExpireTimeLt(eq(PayOrderStatusEnum.WAITING.getStatus()), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(testOrder));
        when(payOrderMapper.updateByIdAndStatus(eq(1L), eq(PayOrderStatusEnum.WAITING.getStatus()), any(PayOrderDO.class)))
                .thenReturn(1);

        int count = payOrderService.expireOrder();

        assertEquals(1, count);
    }

}
