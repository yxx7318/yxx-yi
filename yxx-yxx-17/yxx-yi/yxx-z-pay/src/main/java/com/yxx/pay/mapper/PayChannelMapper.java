package com.yxx.pay.mapper;

import com.yxx.common.core.mapper.BaseMapperPlus;
import com.yxx.pay.core.domain.channel.PayChannelDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PayChannelMapper extends BaseMapperPlus<PayChannelDO> {

    PayChannelDO selectByChannelId(Long channelId);

    PayChannelDO selectByChannelCode(String channelCode);

    List<PayChannelDO> selectListByStatus(@Param("status") Integer status);

    int updateStatus(@Param("channelId") Long channelId, @Param("status") Integer status);
}
