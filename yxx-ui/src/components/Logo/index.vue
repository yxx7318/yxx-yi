<template>
  <div style="position: fixed; top: 40px; left: 40px; z-index: 9999">
    <!-- logo行 -->
    <el-row type="flex" style="height: 80px">
      <el-col style="display: flex; align-items: center; width: 60px; margin-right: 5px">
        <img src="@/assets/logo/logo.png" alt style="width: 60px; height: 60px; border-radius: 10px" />
      </el-col>
      <el-col style="display: flex; align-items: center; width: 360px">
        <span style="font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, Arial, sans-serif; color: white; font-size: 24px;">{{ title }}</span>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'Logo',
  data() {
    return {
      title: process.env.VUE_APP_TITLE
    }
  },
}
</script>

<style>
</style>
