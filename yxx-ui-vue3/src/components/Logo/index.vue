<template>
  <div style="position: fixed; top: 40px; left: 40px; z-index: 9999">
    <!-- logo行 -->
    <el-row type="flex" style="height: 80px">
      <el-col style="display: flex; align-items: center; width: 60px; margin-right: 5px">
        <img src="@/assets/logo/logo.png" alt style="width: 60px; height: 60px; border-radius: 10px" />
        <span style="font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, Arial, sans-serif; color: white; font-size: 24px; margin-left: 10px">{{ title }}</span>
      </el-col>
      <el-col style="display: flex; align-items: center; width: 360px">
<!--        <span style="font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, Arial, sans-serif; color: white; font-size: 24px;">{{ title }}</span>-->
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'Logo',
  setup() {
    const title = import.meta.env.VITE_APP_TITLE || process.env.VUE_APP_TITLE;

    return {
      title
    };
  },
});
</script>

<style>
</style>
