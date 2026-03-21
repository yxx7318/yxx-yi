package com.yxx.pay.mapper;

import com.yxx.common.core.mapper.BaseMapperPlus;
import com.yxx.pay.core.domain.order.PayOrderExtensionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PayOrderExtensionMapper extends BaseMapperPlus<PayOrderExtensionDO> {

    PayOrderExtensionDO selectByExtensionId(Long extensionId);

    PayOrderExtensionDO selectByOrderNo(String orderNo);

    List<PayOrderExtensionDO> selectListByOrderId(@Param("orderId") Long orderId);

    List<PayOrderExtensionDO> selectListByOrderIdAndStatus(@Param("orderId") Long orderId, 
                                                           @Param("status") Integer status);

    List<PayOrderExtensionDO> selectListByStatusAndCreateTimeGe(@Param("status") Integer status, 
                                                                 @Param("createTime") LocalDateTime createTime);

    int updateByIdAndStatus(@Param("extensionId") Long extensionId, 
                            @Param("status") Integer status, 
                            @Param("updateObj") PayOrderExtensionDO updateObj);
}
