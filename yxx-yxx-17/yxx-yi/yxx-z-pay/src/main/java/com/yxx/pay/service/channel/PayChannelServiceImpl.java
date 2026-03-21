package com.yxx.pay.service.channel;

import com.alibaba.fastjson2.JSON;
import com.yxx.common.core.service.impl.ServiceImplPlus;
import com.yxx.common.exception.ServiceException;
import com.yxx.pay.client.PayClient;
import com.yxx.pay.client.PayClientConfig;
import com.yxx.pay.client.PayClientFactory;
import com.yxx.pay.client.impl.NonePayClientConfig;
import com.yxx.pay.client.impl.alipay.AlipayPayClientConfig;
import com.yxx.pay.client.impl.weixin.WxPayClientConfig;
import com.yxx.pay.core.domain.channel.PayChannelDO;
import com.yxx.pay.core.entity.vo.channel.PayChannelCreateReqVO;
import com.yxx.pay.core.entity.vo.channel.PayChannelUpdateReqVO;
import com.yxx.pay.enums.PayChannelEnum;
import com.yxx.pay.mapper.PayChannelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Validator;
import java.util.List;

import static com.yxx.pay.enums.ErrorCodeConstants.*;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class PayChannelServiceImpl extends ServiceImplPlus<PayChannelMapper, PayChannelDO> implements PayChannelService {

    private final PayChannelMapper payChannelMapper;
    private final PayClientFactory payClientFactory;
    private final Validator validator;
    private final PayChannelService self;

    @Override
    public Long createChannel(PayChannelCreateReqVO createReqVO) {
        PayChannelDO existChannel = payChannelMapper.selectByChannelCode(createReqVO.getChannelCode());
        if (existChannel != null) {
            throw new ServiceException(CHANNEL_EXIST_SAME_CHANNEL_ERROR);
        }
        PayClientConfig config = parseAndValidateConfig(createReqVO.getChannelCode(), createReqVO.getChannelConfig());
        PayChannelDO channel = PayChannelDO.builder()
                .channelCode(createReqVO.getChannelCode())
                .channelName(createReqVO.getChannelName())
                .channelConfig(config)
                .channelFeeRate(createReqVO.getChannelFeeRate())
                .status(createReqVO.getStatus())
                .build();
        channel.setRemark(createReqVO.getRemark());
        channel.fieldFillInsert();
        payChannelMapper.insert(channel);
        return channel.getChannelId();
    }

    @Override
    public void updateChannel(PayChannelUpdateReqVO updateReqVO) {
        PayChannelDO existChannel = validateChannelExists(updateReqVO.getChannelId());
        PayClientConfig config = parseAndValidateConfig(existChannel.getChannelCode(), updateReqVO.getChannelConfig());
        PayChannelDO channel = PayChannelDO.builder()
                .channelId(updateReqVO.getChannelId())
                .channelCode(updateReqVO.getChannelCode())
                .channelName(updateReqVO.getChannelName())
                .channelConfig(config)
                .channelFeeRate(updateReqVO.getChannelFeeRate())
                .status(updateReqVO.getStatus())
                .build();
        channel.setRemark(updateReqVO.getRemark());
        channel.fieldFillUpdate();
        payChannelMapper.updateById(channel);
    }

    @Override
    public void deleteChannel(Long channelId) {
        validateChannelExists(channelId);
        payChannelMapper.deleteById(channelId);
    }

    @Override
    public PayChannelDO getChannel(Long channelId) {
        return payChannelMapper.selectByChannelId(channelId);
    }

    @Override
    public PayChannelDO getChannelByCode(String channelCode) {
        return payChannelMapper.selectByChannelCode(channelCode);
    }

    @Override
    public List<PayChannelDO> getChannelList() {
        return payChannelMapper.selectList();
    }

    @Override
    public List<PayChannelDO> getEnableChannelList() {
        return payChannelMapper.selectListByStatus(0);
    }

    @Override
    public PayChannelDO validPayChannel(Long channelId) {
        PayChannelDO channel = payChannelMapper.selectByChannelId(channelId);
        validateChannelStatus(channel);
        return channel;
    }

    @Override
    public PayChannelDO validPayChannel(String channelCode) {
        PayChannelDO channel = payChannelMapper.selectByChannelCode(channelCode);
        validateChannelStatus(channel);
        return channel;
    }

    @Override
    public PayClient getPayClient(Long channelId) {
        PayChannelDO channel = validPayChannel(channelId);
        return payClientFactory.createOrUpdatePayClient(channelId, channel.getChannelCode(), channel.getChannelConfig());
    }

    @Override
    public PayClient getPayClient(String channelCode) {
        PayChannelDO channel = validPayChannel(channelCode);
        return payClientFactory.createOrUpdatePayClient(channel.getChannelId(), channel.getChannelCode(), channel.getChannelConfig());
    }

    private PayChannelDO validateChannelExists(Long channelId) {
        PayChannelDO channel = payChannelMapper.selectByChannelId(channelId);
        if (channel == null) {
            throw new ServiceException(CHANNEL_NOT_FOUND);
        }
        return channel;
    }

    private void validateChannelStatus(PayChannelDO channel) {
        if (channel == null) {
            throw new ServiceException(CHANNEL_NOT_FOUND);
        }
        if (channel.getStatus() != null && channel.getStatus() == 1) {
            throw new ServiceException(CHANNEL_IS_DISABLE);
        }
    }

    private PayClientConfig parseAndValidateConfig(String channelCode, String configStr) {
        Class<? extends PayClientConfig> configClass = getConfigClass(channelCode);
        PayClientConfig config = JSON.parseObject(configStr, configClass);
        if (config == null) {
            throw new ServiceException(CHANNEL_NOT_FOUND);
        }
        config.validate(validator);
        return config;
    }

    private Class<? extends PayClientConfig> getConfigClass(String channelCode) {
        if (PayChannelEnum.isWeixin(channelCode)) {
            return WxPayClientConfig.class;
        } else if (PayChannelEnum.isAlipay(channelCode)) {
            return AlipayPayClientConfig.class;
        }
        return NonePayClientConfig.class;
    }

}
