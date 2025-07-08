<template>
  <div style="position: fixed; top: 40px; height: 80px; left: 40px; z-index: 9999">
    <!-- logo行 -->
    <div style="display: flex; align-items: center; width: 360px; margin-right: 8px">
      <img src="@/assets/logo/logo.png" alt style="width: 60px; height: 60px; border-radius: 10px" />
      <span style="font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, Arial, sans-serif; color: white; font-size: 24px; margin-left: 5px">{{ title }}</span>
    </div>
  </div>
</template>

<script setup>
const title = import.meta.env.VITE_APP_TITLE

</script>

<style>
</style>
