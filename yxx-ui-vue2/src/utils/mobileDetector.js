// 判断是否是移动端
export function mobileFlag() {
  // 另一种检测是 768px
  const maxWidth = 992
  return window.innerWidth < maxWidth
}

export default {
  data() {
    return {
      // 响应式数据，用于存储当前是否为移动端
      isMobile: false
    }
  },

  methods: {
    // 固定的判断逻辑
    mobileFlag() {
      return mobileFlag()
    },

    // 用于更新 isMobile 值的方法
    checkScreenSize() {
      this.isMobile = this.mobileFlag()
    }
  },

  mounted() {
    // 在组件挂载后，立即执行一次检查
    this.checkScreenSize()
    // 并添加 resize 事件监听
    window.addEventListener('resize', this.checkScreenSize)
  },

  beforeDestroy() {
    // 在组件销毁前，移除 resize 事件监听，防止内存泄漏
    window.removeEventListener('resize', this.checkScreenSize)
  }
}
