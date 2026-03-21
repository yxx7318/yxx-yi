package com.yxx.pay.service.channel;

import com.yxx.common.exception.ServiceException;
import com.yxx.pay.client.PayClient;
import com.yxx.pay.core.domain.channel.PayChannelDO;
import com.yxx.pay.core.entity.vo.channel.PayChannelCreateReqVO;
import com.yxx.pay.core.entity.vo.channel.PayChannelUpdateReqVO;
import jakarta.validation.Valid;

import java.util.List;

public interface PayChannelService {

    Long createChannel(@Valid PayChannelCreateReqVO createReqVO);

    void updateChannel(@Valid PayChannelUpdateReqVO updateReqVO);

    void deleteChannel(Long channelId);

    PayChannelDO getChannel(Long channelId);

    PayChannelDO getChannelByCode(String channelCode);

    List<PayChannelDO> getChannelList();

    List<PayChannelDO> getEnableChannelList();

    PayChannelDO validPayChannel(Long channelId);

    PayChannelDO validPayChannel(String channelCode);

    PayClient getPayClient(Long channelId);

    PayClient getPayClient(String channelCode);

}
