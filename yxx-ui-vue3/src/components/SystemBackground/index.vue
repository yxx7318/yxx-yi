<template>
  <el-row style="height: 100vh; min-height: 500px; background-color: #353e54;" justify="center" align="middle">
    <logo />
    <el-row style="min-height: 350px; max-height: 350px">
      <el-col>
        <!-- 中心背景图 -->
        <div>
          <img src="@/assets/svg/login-box-bg.svg" alt style="width: 360px; height: 360px" />
        </div>
        <div style="display: flex; justify-content: center;">
          <span style="font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, Arial, sans-serif; color: white; font-size: 30px; margin-top: -26px">欢迎进入系统</span>
        </div>
      </el-col>
    </el-row>
  </el-row>
</template>

<script setup>
import Logo from '@/components/Logo'

</script>

<style>
</style>
