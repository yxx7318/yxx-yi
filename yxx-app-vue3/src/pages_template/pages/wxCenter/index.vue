<script setup>
import tab from '@/plugins/tab'
import {ref} from 'vue'

const pic = ref('https://uviewui.com/common/logo.png')
const show = ref(true)

const navigateBackHandler = () => {
  tab.navigateBack();
}
</script>

<template>
  <view>
    <u-navbar :fixed="false" title="　" @leftClick="navigateBackHandler">
      <view class="u-flex u-row-right" style="width: 100%;">
        <view class="camera u-flex u-row-center">
          <u-icon name="camera-fill" color="#000000" size="48"></u-icon>
        </view>
      </view>
    </u-navbar>
    <view class="u-flex u-flex-y-center u-flex-around user-box u-p-l-30 u-p-r-20 u-p-b-30">
      <view class="u-m-r-10">
        <u-avatar :src="pic" size="140"></u-avatar>
      </view>
      <view class="u-flex-1">
        <view class="u-font-18 u-p-b-20">uview plus</view>
        <view class="u-font-14 u-tips-color">微信号:test</view>
      </view>
      <view class="u-m-l-10 u-p-10">
        <u-icon name="scan" color="#969799" size="28"></u-icon>
      </view>
      <view class="u-m-l-10 u-p-10">
        <u-icon name="arrow-right" color="#969799" size="28"></u-icon>
      </view>
    </view>

    <view class="u-m-t-20">
      <u-cell-group>
        <u-cell icon="rmb-circle" title="支付" isLink></u-cell>
      </u-cell-group>
    </view>

    <view class="u-m-t-20">
      <u-cell-group>
        <u-cell icon="star" title="收藏" isLink></u-cell>
        <u-cell icon="photo" title="相册" isLink></u-cell>
        <u-cell icon="coupon" title="卡券" isLink></u-cell>
        <u-cell icon="heart" title="关注" isLink></u-cell>
      </u-cell-group>
    </view>

    <view class="u-m-t-20">
      <u-cell-group>
        <u-cell icon="setting" title="设置" isLink></u-cell>
      </u-cell-group>
    </view>
  </view>
</template>


<style lang="scss" scoped>
page {
  background-color: #ededed;
}

.camera {
  width: 54px;
  height: 44px;

  &:active {
    background-color: #ededed;
  }
}

.user-box {
  background-color: #fff;
}

.u-cell-group {
  background-color: #fff;
}
</style>
