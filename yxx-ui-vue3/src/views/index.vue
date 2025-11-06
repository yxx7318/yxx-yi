<template>
  <div class="welcome home">
    <img alt="welcome" src="@/assets/images/welcome.png"/>
  </div>
</template>

<script setup name="Index">
const version = ref('3.8.9')

function goTarget(url) {
  window.open(url, '__blank')
}
</script>

<style scoped lang="scss">
.welcome {
  background-color: white;
  display: flex; /* 启用Flex布局 */
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  height: 100vh; /* 设置容器高度为视口高度 */
}

.welcome img {
  max-width: 100%; /* 图片最大宽度不超过容器 */
  max-height: 80vh; /* 图片最大高度不超过视口80% */
}
</style>
