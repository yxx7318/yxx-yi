<script setup>
</script>
<template>
  <view class="u-wrap">
    <view class="meituan">
      <view class="content">
        <view class="left">
          <view class="sum">
            ￥
            <text class="num">8</text>
          </view>
          <view class="type">抵用券</view>
        </view>
        <view class="centre">
          <view class="title">【洗牙】8元无门槛红包</view>
          <view class="valid-date">今日到期</view>
        </view>
        <view class="right">
          <view size="mini" class="immediate-use" :round="true">立即使用</view>
        </view>
      </view>
      <view class="tips">
        <view class="circle-left"></view>
        <view class="circle-right"></view>
        <view class="explain u-line-1">满8.1元可用、限最新版本客户端使用</view>
        <view class="rule" @tap="xxx122">
          <text>使用规则</text>
          <u-icon name="arrow-right" color="" :size="20" @click=""></u-icon>
        </view>
      </view>
    </view>
    <view class="jingdong">
      <view class="left">
        <view class="sum">
          ￥
          <text class="num">100</text>
        </view>
        <view class="type">满149元可用</view>
      </view>
      <view class="right">
        <view class="top">
          <view class="title">
            <text class="tag">限品类东券</text>
            <text>仅可购买个人护理部分商品</text>
          </view>
          <view class="bottom">
            <view class="date u-line-1">2020.01.01-2020.01.31</view>
            <view class="immediate-use">立即使用</view>
          </view>
        </view>
        <view class="tips">
          <view class="explain">
            <u-icon name="zhuanfa" class="transpond" :size="24"></u-icon>
            <text>可赠送</text>
          </view>
        </view>
      </view>
    </view>
    <view class="taobao">
      <view class="title">
        <view class="left">
          <image class="buddha"
                 src="https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1975388697,1068670603&fm=26&gp=0.jpg"
                 mode="aspectFill"></image>
          <view class="store">袜子精保护协会</view>
        </view>
        <view class="entrance">进店</view>
      </view>
      <view class="ticket">
        <view class="left">
          <image class="picture"
                 src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1578059523488&di=5f592ac19c1b983005d3e85add469756&imgtype=0&src=http%3A%2F%2Fimg010.hc360.cn%2Fg7%2FM00%2F2D%2FB9%2FwKhQs1QfUo6EdeM-AAAAALwk1hM072.jpg"
                 mode="widthFix"></image>
          <view class="introduce">
            <view class="top">
              ￥
              <text class="big">3</text>
              满88减3
            </view>
            <view class="type">店铺优惠券</view>
            <view class="date u-line-1">2019.11.28-2020.1.24</view>
          </view>
        </view>
        <view class="right">
          <view class="use immediate-use" :round="true">去使用</view>
        </view>

      </view>
    </view>
  </view>
</template>

<style lang="scss" scoped>
page {
  height: 100%;
  background-color: rgb(244, 244, 244);
}

.u-wrap {
  padding: 24 rpx;
}

.meituan {
  margin: 30 rpx auto;
  background-color: #ffffff;
  width: 700 rpx;
  // border: 10rpx;
  color: $u-warning;
  font-size: 28 rpx;

  .content {
    display: flex;
    align-items: center;
    padding: 80 rpx 20 rpx;
    border: 10 rpx;
    background-color: #fff5f4;

    .left {
      .sum {
        font-size: 32 rpx;

        .num {
          font-size: 60 rpx;
          font-weight: bold;
        }
      }
    }

    .centre {
      margin-left: 40 rpx;

      .title {
        font-size: 32 rpx;
        font-weight: bold;
        color: $u-main-color;
        margin-bottom: 20 rpx;
      }
    }

    .right {
      margin-left: 30 rpx;

      .immediate-use {
        padding: 0 20 rpx;
        height: 50 rpx;
        border-radius: 25 rpx;
        line-height: 50 rpx;
        background-color: $u-warning !important;
        color: #ffffff !important;
        font-size: 24 rpx;
        border: none;
        word-break: keep-all;
      }
    }
  }

  .tips {
    padding: 0 20 rpx;
    border: 10 rpx;
    background-color: $u-info-light;
    position: relative;
    color: $u-tips-color;
    display: flex;
    justify-content: space-between;
    line-height: 60 rpx;
    font-size: 24 rpx;

    .circle-left,
    .circle-right {
      position: absolute;
      height: 36 rpx;
      width: 18 rpx;
      background-color: #f2f2f2;
    }

    .circle-right {
      border-radius: 40 rpx 0 0 40 rpx;
      right: 0;
      top: -18rpx;
    }

    .circle-left {
      border-radius: 0 40 rpx 40 rpx 0;
      left: 0;
      top: -18rpx;
    }

    .rule {
      font-size: 24 rpx;
      display: flex;
      align-items: center;

      text {
        margin-right: 10 rpx;
        flex: 1;
      }
    }
  }
}

.jingdong {
  margin-top: 40 rpx;
  width: 700 rpx;
  height: auto;
  background-color: #ffffff;
  display: flex;

  .left {
    padding: 0 30 rpx;
    background-color: rgb(95, 148, 224); //rgb(94, 152, 225);
    text-align: center;
    font-size: 28 rpx;
    color: #ffffff;

    .sum {
      margin-top: 50 rpx;
      font-weight: bold;
      font-size: 32 rpx;

      .num {
        font-size: 80 rpx;
      }
    }

    .type {
      margin-bottom: 50 rpx;
      font-size: 24 rpx;
    }
  }

  .right {
    padding: 20 rpx 20 rpx 0;
    font-size: 28 rpx;

    .top {
      border-bottom: 2 rpx dashed $u-border-color;

      .title {
        margin-right: 60 rpx;
        line-height: 40 rpx;

        .tag {
          padding: 4 rpx 20 rpx;
          background-color: rgb(73, 154, 201);
          border-radius: 20 rpx;
          color: #ffffff;
          font-weight: bold;
          font-size: 24 rpx;
          margin-right: 10 rpx;
        }
      }

      .bottom {
        display: flex;
        margin-top: 20 rpx;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 10 rpx;

        .date {
          font-size: 20 rpx;
          flex: 1;
        }

        .immediate-use {
          height: auto;
          padding: 0 20 rpx;
          font-size: 24 rpx;
          border-radius: 40 rpx;
          line-height: 40 rpx;
          color: rgb(117, 142, 165);
          border: 2 rpx solid rgb(117, 142, 165);
        }
      }
    }

    .tips {
      width: 100%;
      line-height: 50 rpx;
      display: flex;
      align-items: center;
      justify-content: space-between;
      font-size: 24 rpx;

      .transpond {
        margin-right: 10 rpx;
      }

      .explain {
        display: flex;
        align-items: center;
      }

      .particulars {
        width: 30 rpx;
        height: 30 rpx;
        box-sizing: border-box;
        padding-top: 8 rpx;
        border-radius: 50%;
        background-color: $u-info-disabled;
        text-align: center;
      }
    }
  }
}

.taobao {
  margin-top: 40 rpx;
  width: 700 rpx;
  background-color: white;
  padding: 30 rpx 20 rpx 20 rpx;
  border-radius: 20 rpx;

  .title {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 20 rpx;
    font-size: 30 rpx;

    .left {
      display: flex;
      align-items: center;
    }

    .store {
      font-weight: 500;
    }

    .buddha {
      width: 70 rpx;
      height: 70 rpx;
      border-radius: 10 rpx;
      margin-right: 10 rpx;
    }

    .entrance {
      color: $u-info;
      border: solid 2 rpx $u-info;
      line-height: 48 rpx;
      padding: 0 30 rpx;
      background: none;
      border-radius: 15px;
    }
  }

  .ticket {
    display: flex;

    .left {
      width: 70%;
      padding: 30 rpx 20 rpx;
      background-color: rgb(255, 245, 244);
      border-radius: 20 rpx;
      border-right: dashed 2 rpx rgb(224, 215, 211);
      display: flex;

      .picture {
        width: 172 rpx;
        height: 172 rpx;
        border-radius: 20 rpx;
      }

      .introduce {
        margin-left: 10 rpx;

        .top {
          color: $u-warning;
          font-size: 28 rpx;

          .big {
            font-size: 60 rpx;
            font-weight: bold;
            margin-right: 10 rpx;
          }
        }

        .type {
          font-size: 28 rpx;
          color: $u-info-dark;
        }

        .date {
          margin-top: 10 rpx;
          font-size: 20 rpx;
          color: $u-info-dark;
        }
      }
    }

    .right {
      width: 30%;
      padding: 40 rpx 20 rpx;
      background-color: rgb(255, 245, 244);
      border-radius: 20 rpx;
      display: flex;
      align-items: center;

      .use {
        height: auto;
        padding: 0 20 rpx;
        font-size: 24 rpx;
        border-radius: 40 rpx;
        color: #ffffff !important;
        background-color: $u-warning !important;
        line-height: 40 rpx;
        color: rgb(117, 142, 165);
        margin-left: 20 rpx;
      }
    }
  }
}
</style>
