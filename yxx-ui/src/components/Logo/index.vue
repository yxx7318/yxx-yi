<template>
  <div>
    <!-- logo行 -->
    <el-row type="flex" style="height: 28vh; min-height: 100px; ">
      <el-col :span="2" style="height: 7vh; min-width: 70px; margin-top: 30px; margin-left: 40px;">
        <img src="../../assets/logo/logo.png" alt style="width: 60px; height: 60px;border-radius: 10px" />
      </el-col>
      <el-col style="height: 6vh; min-width: 70px; min-height: 54px; margin-top: 30px; display: flex; align-items: center;">
        <span style="font-family: Helvetica Neue; color: white; font-size: 24px;">{{ title }}</span>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'loginLogo',
  data() {
    return {
      title: process.env.VUE_APP_TITLE
    }
  },
}
</script>

<style>
</style>
