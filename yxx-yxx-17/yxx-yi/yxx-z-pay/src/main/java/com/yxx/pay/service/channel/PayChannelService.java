package com.yxx.pay.service.channel;

import com.yxx.common.core.service.IServicePlus;
import com.yxx.pay.client.PayClient;
import com.yxx.pay.core.domain.channel.PayChannelDO;

import java.util.List;

public interface PayChannelService extends IServicePlus<PayChannelDO> {

    PayChannelDO getChannel(Long channelId);

    PayChannelDO getChannelByCode(String channelCode);

    List<PayChannelDO> getChannelList();

    List<PayChannelDO> getEnableChannelList();

    PayChannelDO validPayChannel(Long channelId);

    PayChannelDO validPayChannel(String channelCode);

    PayClient getPayClient(Long channelId);

    PayClient getPayClient(String channelCode);

}
