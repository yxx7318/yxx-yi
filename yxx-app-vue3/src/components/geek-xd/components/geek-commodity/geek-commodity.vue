<script setup>
const props = defineProps({
  img: {
    type: String,
    default: ''
  },
  title: {
    type: String,
    default: ''
  },
  subTitle: {
    type: String,
    default: ''
  },
  price: {
    type: Number,
    default: 0
  },
  type: {
    type: String,
    default: 'line' // line, rect
  }
})
</script>
<template>
  <view class="card" :class="type" @click="$emit('click')">
    <image class="img" :src="img"/>
    <view class="content">
      <view class="title">{{ title }}</view>
      <view class="subTitle">{{ subTitle }}</view>
      <view class="price">￥{{ price }}</view>
    </view>
  </view>
</template>
<style lang="scss" scoped>
.card {
  padding: 0;
  border-radius: 10px;
  background-color: #ffffff;
  width: 700 rpx;
  padding: 20 rpx;
  margin: 10 rpx;
  position: relative;

  .img {
    height: 200 rpx;
    width: 200 rpx;
  }
}

.line {
  display: flex;

  .content {
    height: 200 rpx;
    padding-left: 20 rpx;

    .title {
      width: 400 rpx;
      font-size: 35 rpx;
    }

    .subTitle {
      width: 400 rpx;
      height: 90 rpx;
      margin-top: 10 rpx;
      font-size: 20 rpx;
      color: rgb(87, 87, 87);
      line-height: 30 rpx;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 3;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .price {
      font-size: 40 rpx;
      color: red;
      width: 400 rpx;
    }
  }
}

.rect {
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  width: 350 rpx;
  padding: 0;
  margin: 10 rpx;
  display: inline-block;

  .img {
    border-radius: 10px 10px 0 0;
    height: 350 rpx;
    width: 350 rpx;
  }

  .content {
    padding: 0 20 rpx;
    margin: 0;
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;

    .title {
      width: 330 rpx;
      font-size: 25 rpx;
    }

    .subTitle {
      width: 330 rpx;
      font-size: 20 rpx;
      color: rgb(87, 87, 87);
    }

    .price {
      font-size: 30 rpx;
      color: red;
      width: 100%;
    }
  }
}
</style>
