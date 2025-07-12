<template>
  <view class="column">
    <view v-for="(item,index) in progressList" :key="index"
          :class="['row','font-small','progress',padMiddle?'paddingMiddle':'']">
      <text class="title">{{ item.name }}</text>
      <view class="body">
        <view class="number">{{ item.now ? item.now + "/" : "" }}{{ item.expect }} [{{ item.value }}%]</view>
        <progress :percent="item.value" backgroundColor="#C9C9C9"
                  :border-radius="borderRadius?borderRadius+'rpx':'0px'"
                  :color="time"
                  stroke-width="16"/>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'data-progress',
  props: {
    progressList: {
      type: Array,
      default: () => {
        return []
      }
    },
    borderRadius: {
      type: Number,
      default: 0
    },
    padMiddle: {
      type: String,
      default: "false"
    }
  },
  data() {
    return {
      time: 0
    }
  },
  watch: {
    "progressList": {
      deep: true,
      handler: function (newVal, oldVal) {
        this.time = newVal.filter(x => x.name == "时间进度")[0].value;
      }
    }
  },
  created() {
    this.time = this.progressList.filter(x => x.name == "时间进度")[0].value;
  }
}
</script>

<style lang="scss">
.paddingMiddle {
  padding: 18 rpx 10 rpx;
}

.progress {

  .title {
    font-size: 28 rpx;
    width: 170 rpx;
    display: flex;
    align-items: center;

  }

  .body {
    position: relative;
    flex: 1;

    .number {
      color: #fff;
      position: absolute;
      z-index: 2;
      left: 26 rpx;
      height: 100%;
      display: flex;
      align-items: center;
      text-overflow: ellipsis;
      white-space: nowrap;
      overflow: hidden;
      height: 44 rpx;
    }

    progress {
      padding: 6 rpx 0;
    }

  }
}


</style>
