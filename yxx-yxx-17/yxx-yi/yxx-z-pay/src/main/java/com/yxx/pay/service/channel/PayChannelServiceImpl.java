package com.yxx.pay.service.channel;

import com.yxx.common.core.service.impl.ServiceImplPlus;
import com.yxx.common.exception.ServiceException;
import com.yxx.pay.client.PayClient;
import com.yxx.pay.client.PayClientFactory;
import com.yxx.pay.client.impl.alipay.AlipayPayClientConfig;
import com.yxx.pay.client.impl.weixin.WxPayClientConfig;
import com.yxx.pay.config.PayProperties;
import com.yxx.pay.core.domain.channel.PayChannelDO;
import com.yxx.pay.enums.PayChannelEnum;
import com.yxx.pay.mapper.PayChannelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.yxx.pay.enums.ErrorCodeConstants.*;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class PayChannelServiceImpl extends ServiceImplPlus<PayChannelMapper, PayChannelDO> implements PayChannelService {

    private final PayChannelMapper payChannelMapper;
    private final PayClientFactory payClientFactory;
    private final PayProperties payProperties;

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
        List<PayChannelDO> result = new ArrayList<>();
        if (Boolean.TRUE.equals(payProperties.getWechat().getEnabled())) {
            for (PayChannelEnum channelEnum : PayChannelEnum.getWechatChannels()) {
                PayChannelDO channel = buildWechatChannel(channelEnum);
                if (channel != null) {
                    result.add(channel);
                }
            }
        }
        if (Boolean.TRUE.equals(payProperties.getAlipay().getEnabled())) {
            for (PayChannelEnum channelEnum : PayChannelEnum.getAlipayChannels()) {
                PayChannelDO channel = buildAlipayChannel(channelEnum);
                if (channel != null) {
                    result.add(channel);
                }
            }
        }
        return result;
    }

    @Override
    public PayChannelDO validPayChannel(Long channelId) {
        PayChannelDO channel = payChannelMapper.selectByChannelId(channelId);
        if (channel == null) {
            throw new ServiceException(CHANNEL_NOT_FOUND);
        }
        if (channel.getStatus() != null && channel.getStatus() == 1) {
            throw new ServiceException(CHANNEL_IS_DISABLE);
        }
        return channel;
    }

    @Override
    public PayChannelDO validPayChannel(String channelCode) {
        PayChannelDO channel = buildChannelByCode(channelCode);
        if (channel == null) {
            throw new ServiceException(CHANNEL_NOT_FOUND);
        }
        return channel;
    }

    @Override
    public PayClient getPayClient(Long channelId) {
        PayChannelDO channel = validPayChannel(channelId);
        return payClientFactory.getPayClient(channelId);
    }

    @Override
    public PayClient getPayClient(String channelCode) {
        PayChannelDO channel = validPayChannel(channelCode);
        return createPayClient(channel);
    }

    private PayChannelDO buildChannelByCode(String channelCode) {
        PayChannelEnum channelEnum = PayChannelEnum.getByCode(channelCode);
        if (channelEnum == null) {
            return null;
        }
        if (PayChannelEnum.isWeixin(channelCode)) {
            return buildWechatChannel(channelEnum);
        } else if (PayChannelEnum.isAlipay(channelCode)) {
            return buildAlipayChannel(channelEnum);
        }
        return null;
    }

    private PayChannelDO buildWechatChannel(PayChannelEnum channelEnum) {
        PayProperties.Wechat wechat = payProperties.getWechat();
        if (!Boolean.TRUE.equals(wechat.getEnabled())) {
            return null;
        }
        WxPayClientConfig config = new WxPayClientConfig();
        config.setAppId(wechat.getAppId());
        config.setMchId(wechat.getMchId());
        config.setApiVersion(WxPayClientConfig.API_VERSION_V3);
        config.setApiV3Key(wechat.getApiV3Key());
        config.setPrivateKeyContent(wechat.getPrivateKeyContent());
        config.setCertSerialNo(wechat.getCertSerialNo());
        config.setPublicKeyContent(wechat.getPublicKeyContent());
        config.setPublicKeyId(wechat.getPublicKeyId());
        return PayChannelDO.builder()
                .channelId((long) channelEnum.ordinal() + 1)
                .channelCode(channelEnum.getCode())
                .channelName(channelEnum.getName())
                .channelConfig(config)
                .channelFeeRate(new BigDecimal("0.60"))
                .status(0)
                .build();
    }

    private PayChannelDO buildAlipayChannel(PayChannelEnum channelEnum) {
        PayProperties.Alipay alipay = payProperties.getAlipay();
        if (!Boolean.TRUE.equals(alipay.getEnabled())) {
            return null;
        }
        AlipayPayClientConfig config = new AlipayPayClientConfig();
        config.setServerUrl(alipay.getServerUrl());
        config.setAppId(alipay.getAppId());
        config.setSignType(alipay.getSignType());
        config.setMode(alipay.getMode());
        config.setPrivateKey(alipay.getPrivateKey());
        config.setAlipayPublicKey(alipay.getAlipayPublicKey());
        config.setAppCertContent(alipay.getAppCertContent());
        config.setAlipayPublicCertContent(alipay.getAlipayPublicCertContent());
        config.setRootCertContent(alipay.getRootCertContent());
        config.setEncryptType(alipay.getEncryptType());
        config.setEncryptKey(alipay.getEncryptKey());
        return PayChannelDO.builder()
                .channelId((long) channelEnum.ordinal() + 100)
                .channelCode(channelEnum.getCode())
                .channelName(channelEnum.getName())
                .channelConfig(config)
                .channelFeeRate(new BigDecimal("0.60"))
                .status(0)
                .build();
    }

    private PayClient createPayClient(PayChannelDO channel) {
        return payClientFactory.createOrUpdatePayClient(
                channel.getChannelId(), 
                channel.getChannelCode(), 
                channel.getChannelConfig());
    }

}
