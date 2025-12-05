// 另一种检测是 768px
let maxWidth = 992

// 保存的回调
let mobileCallback

// 目标函数
let targetCheckScreenSize

// 判断是否是移动端
export function mobileFlag() {
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

    // 支持传入回调函数
    checkScreenSize(callback) {
      // 如果传入了回调函数，保存
      if (callback && typeof callback === 'function') {
        mobileCallback = callback
      }
      // 包裹返回一个新的函数
      return () => {
        this.isMobile = this.mobileFlag()
        // 如果保存了函数，执行
        if (mobileCallback && typeof mobileCallback === 'function') {
          mobileCallback(innerHeight, innerWidth)
        }
      }
    }
  },

  mounted() {
    targetCheckScreenSize = this.checkScreenSize(mobileCallback)
    // 在组件挂载后，立即执行一次检查
    targetCheckScreenSize()
    // 并添加 resize 事件监听，注意这里要使用箭头函数或bind，以确保能传递回调
    window.addEventListener('resize', targetCheckScreenSize)
  },

  beforeDestroy() {
    // 在组件销毁前，移除 resize 事件监听，防止内存泄漏
    window.removeEventListener('resize', targetCheckScreenSize)
  }
}
