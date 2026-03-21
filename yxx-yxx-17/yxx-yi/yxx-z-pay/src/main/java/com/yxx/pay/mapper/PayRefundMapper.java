package com.yxx.pay.mapper;

import com.yxx.common.core.mapper.BaseMapperPlus;
import com.yxx.pay.core.domain.refund.PayRefundDO;
import com.yxx.pay.core.entity.vo.refund.PayRefundPageReqVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PayRefundMapper extends BaseMapperPlus<PayRefundDO> {

    PayRefundDO selectByRefundId(Long refundId);

    PayRefundDO selectByRefundNo(String refundNo);

    PayRefundDO selectByMerchantRefundId(String merchantRefundId);

    List<PayRefundDO> selectListByOrderId(@Param("orderId") Long orderId);

    List<PayRefundDO> selectListByOrderIdAndStatus(@Param("orderId") Long orderId, @Param("status") Integer status);

    List<PayRefundDO> selectRefundPageList(PayRefundPageReqVO pageReqVO);

    List<PayRefundDO> selectListByStatusAndCreateTimeGe(@Param("status") Integer status, 
                                                         @Param("createTime") LocalDateTime createTime);

    int updateByIdAndStatus(@Param("refundId") Long refundId, 
                            @Param("status") Integer status, 
                            @Param("updateObj") PayRefundDO updateObj);

}
