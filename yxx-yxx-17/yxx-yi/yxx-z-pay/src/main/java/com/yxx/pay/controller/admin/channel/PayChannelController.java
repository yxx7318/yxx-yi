package com.yxx.pay.controller.admin.channel;

import com.yxx.common.annotation.Log;
import com.yxx.common.core.controller.BaseControllerPlus;
import com.yxx.common.core.domain.PageResult;
import com.yxx.common.core.domain.R;
import com.yxx.common.enums.BusinessType;
import com.yxx.pay.core.domain.channel.PayChannelDO;
import com.yxx.pay.core.entity.vo.channel.PayChannelCreateReqVO;
import com.yxx.pay.core.entity.vo.channel.PayChannelRespVO;
import com.yxx.pay.core.entity.vo.channel.PayChannelUpdateReqVO;
import com.yxx.pay.service.channel.PayChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "支付渠道")
@RestController
@RequestMapping("/pay/channel")
@Validated
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class PayChannelController extends BaseControllerPlus {

    private final PayChannelService payChannelService;

    @PostMapping
    @Operation(summary = "创建支付渠道")
    @PreAuthorize("@ss.hasPermi('pay:channel:add')")
    @Log(title = "支付渠道", businessType = BusinessType.INSERT)
    public R<Long> createChannel(@RequestBody @Validated PayChannelCreateReqVO createReqVO) {
        return R.ok(payChannelService.createChannel(createReqVO));
    }

    @PutMapping
    @Operation(summary = "更新支付渠道")
    @PreAuthorize("@ss.hasPermi('pay:channel:edit')")
    @Log(title = "支付渠道", businessType = BusinessType.UPDATE)
    public R<String> updateChannel(@RequestBody @Validated PayChannelUpdateReqVO updateReqVO) {
        payChannelService.updateChannel(updateReqVO);
        return R.ok();
    }

    @DeleteMapping("/{channelId}")
    @Operation(summary = "删除支付渠道")
    @PreAuthorize("@ss.hasPermi('pay:channel:remove')")
    @Log(title = "支付渠道", businessType = BusinessType.DELETE)
    public R<String> deleteChannel(@PathVariable Long channelId) {
        payChannelService.deleteChannel(channelId);
        return R.ok();
    }

    @GetMapping("/{channelId}")
    @Operation(summary = "获得支付渠道")
    @PreAuthorize("@ss.hasPermi('pay:channel:query')")
    public R<PayChannelRespVO> getChannel(@PathVariable Long channelId) {
        PayChannelDO channel = payChannelService.getChannel(channelId);
        return R.ok(payChannelService.convertBean(channel, PayChannelRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得支付渠道列表")
    @PreAuthorize("@ss.hasPermi('pay:channel:query')")
    public R<List<PayChannelRespVO>> getChannelList() {
        List<PayChannelDO> list = payChannelService.getChannelList();
        return R.ok(payChannelService.convertList(list, PayChannelRespVO.class));
    }

    @GetMapping("/enable-list")
    @Operation(summary = "获得启用的支付渠道列表")
    public R<List<PayChannelRespVO>> getEnableChannelList() {
        List<PayChannelDO> list = payChannelService.getEnableChannelList();
        return R.ok(payChannelService.convertList(list, PayChannelRespVO.class));
    }

}
