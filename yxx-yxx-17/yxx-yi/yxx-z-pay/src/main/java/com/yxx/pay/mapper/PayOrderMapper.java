package com.yxx.pay.mapper;

import com.yxx.common.core.mapper.BaseMapperPlus;
import com.yxx.pay.core.domain.order.PayOrderDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PayOrderMapper extends BaseMapperPlus<PayOrderDO> {

    PayOrderDO selectByOrderId(Long orderId);

    PayOrderDO selectByOrderNo(String orderNo);

    PayOrderDO selectByMerchantOrderId(@Param("merchantOrderId") String merchantOrderId);

    List<PayOrderDO> selectListByStatusAndExpireTimeLt(@Param("status") Integer status, 
                                                        @Param("expireTime") LocalDateTime expireTime);

    int updateByIdAndStatus(@Param("orderId") Long orderId, 
                            @Param("status") Integer status, 
                            @Param("updateObj") PayOrderDO updateObj);

    int updateRefundPrice(@Param("orderId") Long orderId, 
                          @Param("incrRefundPrice") Integer incrRefundPrice);
}
