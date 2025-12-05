import { ref, onMounted, onBeforeUnmount } from 'vue'

// 另一种检测是 768px
let maxWidth = 992

// 判断是否是移动端
export function mobileFlag() {
  return window.innerWidth < maxWidth
}

/**
 * Vue 3 Composable，用于检测设备是否为移动端，并在窗口大小改变时自动更新状态
 * @returns {Ref<boolean>} 一个响应式的 ref 对象，其 .value 为 true 时表示当前是移动端
 */
export function useMobileDetector(callback) {
  // 定义响应式状态，用于存储当前是否为移动端
  const isMobile = ref(false)

  // 定义一个函数，用于更新 isMobile 的值
  const checkScreenSize = () => {
    isMobile.value = mobileFlag()
    if (callback && typeof callback === 'function') {
      callback(innerHeight, innerWidth)
    }
  }

  // 在组件挂载后，立即执行一次检查，并添加 resize 事件监听
  onMounted(() => {
    checkScreenSize()
    window.addEventListener('resize', checkScreenSize);
  })

  // 在组件卸载前，移除 resize 事件监听，以防止内存泄漏
  onBeforeUnmount(() => {
    window.removeEventListener('resize', checkScreenSize)
  })

  // 将响应式状态暴露给组件使用
  return isMobile
}
