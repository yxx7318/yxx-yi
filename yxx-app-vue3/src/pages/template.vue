<template>
  <view class="wrap">
    <view class="list-wrap">
      <u-cell-group title-bg-color="rgb(243, 244, 246)" :title="getGroupTitle(item)" v-for="(item, index) in list"
                    :key="index">
        <u-cell :titleStyle="{ fontWeight: 500 }" @click="openPage(item1.path)" :title="getFieldTitle(item1)"
                v-for="(item1, index1) in item.list" :key="index1">
          <template v-slot:icon>
            <image class="u-cell-icon" :src="getIcon(item1.icon)" mode="widthFix"></image>
          </template>
        </u-cell>
      </u-cell-group>
    </view>
    <u-gap height="70"></u-gap>
    <!-- <u-tabbar :list="vuex_tabbar" :mid-button="true"></u-tabbar> -->
  </view>
</template>

<script>
import list from "./template.config.js"

export default {
  data() {
    return {
      list: list,
      // desc: '收集众多的常用页面和布局，减少开发者的重复工作，让你专注逻辑，事半功倍'
    }
  },
  computed: {
    getIcon() {
      return path => {
        return '../static/uview/demo/' + path + '.png';
        return 'https://cdn.uviewui.com/uview/example/' + path + '.png';
      }
    },
  },
  methods: {
    openPage(path) {
      this.$u.route({
        url: path
      })
    },
    getGroupTitle(item) {
      return item.groupName
    },
    getFieldTitle(item) {
      return item.title
    }
  }
}
</script>

<style>
/* page {
        background-color: rgb(240, 242, 244);
    } */
</style>

<style lang="scss" scoped>
.u-cell-icon {
  width: 36rpx;
  height: 36rpx;
  margin-right: 8rpx;
}
</style>
