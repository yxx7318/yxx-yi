<template>
  <footer v-if="visible" :class="['copyright', { 'dark-mode': isDark }]">
    <span>{{ content }}</span>
  </footer>
</template>

<script setup>
import { useDark } from '@vueuse/core'
import useSettingsStore from '@/store/modules/settings'

const settingsStore = useSettingsStore()
const isDark = useDark()  // 获取全局暗黑模式状态

const visible = computed(() => settingsStore.footerVisible)
const content = computed(() => settingsStore.footerContent)
</script>

<style scoped>
.copyright {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 36px;
  padding: 10px 20px;
  text-align: right;
  font-size: 14px;
  border-top: 1px solid #e7e7e7;
  z-index: 999;
  /* 浅色模式样式 */
  background-color: #f8f8f8;
  color: #606266;
  border-top: 1px solid #e7e7e7;
}

/* 暗黑模式样式 */
.copyright.dark-mode {
  --el-bg-color: #1d1e1f;      /* Element Plus 暗黑背景色 */
  --el-text-color-secondary: #a3a6ad; /* Element Plus 次文本色 */
  --el-border-color: #424242;  /* Element Plus 边框色 */

  background-color: var(--el-bg-color);
  color: var(--el-text-color-secondary);
  border-top: 1px solid var(--el-border-color);
}
</style>
