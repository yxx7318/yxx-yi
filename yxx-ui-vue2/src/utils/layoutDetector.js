// 宽度阈值（另一种检测手机端可设为 768px）
let maxWidth = 992
// 高度阈值
let maxHeight = 660

/**
 * 直接判断是否是移动端（非响应式，单次判断）
 * @returns {boolean}
 */
export function mobileFlag() {
  return window.innerWidth < maxWidth
}

/**
 * 创建宽度媒体查询对象
 * @param {number} currentWidth 自定义宽度阈值
 * @returns {MediaQueryList|null}
 */
function getWidthMediaQuery(currentWidth = maxWidth) {
  return typeof window !== 'undefined'
    ? window.matchMedia(`(max-width: ${currentWidth}px)`)
    : null
}

/**
 * 创建高度媒体查询对象
 * @param {number} currentHeight 自定义高度阈值
 * @returns {MediaQueryList|null}
 */
function getHeightMediaQuery(currentHeight = maxHeight) {
  return typeof window !== 'undefined'
    ? window.matchMedia(`(max-height: ${currentHeight}px)`)
    : null
}

/**
 * 生成宽度媒体查询变化的处理函数
 * @param {Vue实例} vm Vue组件实例
 * @param {Function} widthCallback 宽度变化回调
 * @returns {Function}
 */
function getWidthHandleMediaChange(vm, widthCallback) {
  return (e) => {
    // 更新实例上的响应式状态
    vm.widthFlag = e.matches
    // 执行回调（绑定组件实例为 this）
    if (widthCallback && typeof widthCallback === 'function') {
      const innerHeight = window.innerHeight
      const innerWidth = window.innerWidth
      widthCallback.call(vm, vm.widthFlag, innerHeight, innerWidth)
    }
  }
}

/**
 * 生成高度媒体查询变化的处理函数
 * @param {Vue实例} vm Vue组件实例
 * @param {Function} heightCallback 高度变化回调
 * @returns {Function}
 */
function getHeightHandleMediaChange(vm, heightCallback) {
  return (e) => {
    // 更新实例上的响应式状态
    vm.heightFlag = e.matches
    // 执行回调（绑定组件实例为 this）
    if (heightCallback && typeof heightCallback === 'function') {
      const innerHeight = window.innerHeight
      const innerWidth = window.innerWidth
      heightCallback.call(vm, vm.heightFlag, innerHeight, innerWidth)
    }
  }
}

/**
 * Vue2 Mixin：监听宽度阈值变化（响应式）
 * @param {number} currentWidth 宽度阈值
 * @param {Function} widthCallback 宽度变化回调
 * @returns {Object} Vue2 Mixin 对象
 */
export function useWidthDetector(currentWidth = maxWidth, widthCallback) {
  return {
    data() {
      return {
        widthFlag: false
      }
    },
    mounted() {
      // 创建媒体查询对象并挂载到实例（方便销毁时移除监听）
      this._widthMediaQuery = getWidthMediaQuery(currentWidth)
      if (!this._widthMediaQuery) return

      // 生成处理函数并挂载到实例
      this._widthHandleMediaChange = getWidthHandleMediaChange(this, widthCallback)

      // 初始化执行一次（保证初始状态正确）
      this._widthHandleMediaChange(this._widthMediaQuery)

      // 监听媒体查询变化
      this._widthMediaQuery.addEventListener('change', this._widthHandleMediaChange)
    },
    beforeDestroy() {
      // 组件销毁时移除监听，防止内存泄漏
      if (this._widthMediaQuery && this._widthHandleMediaChange) {
        this._widthMediaQuery.removeEventListener('change', this._widthHandleMediaChange)
        // 清理引用
        this._widthMediaQuery = null
        this._widthHandleMediaChange = null
      }
    }
  }
}

/**
 * Vue2 Mixin：监听高度阈值变化（响应式）
 * @param {number} currentHeight 高度阈值
 * @param {Function} heightCallback 高度变化回调
 * @returns {Object} Vue2 Mixin 对象
 */
export function useHeightDetector(currentHeight = maxHeight, heightCallback) {
  return {
    data() {
      return {
        heightFlag: false
      }
    },
    mounted() {
      if (!window) return // 避免 SSR 环境执行

      // 创建媒体查询对象并挂载到实例
      this._heightMediaQuery = getHeightMediaQuery(currentHeight)
      if (!this._heightMediaQuery) return

      // 生成处理函数并挂载到实例
      this._heightHandleMediaChange = getHeightHandleMediaChange(this, heightCallback)

      // 初始化执行一次
      this._heightHandleMediaChange(this._heightMediaQuery)

      // 监听媒体查询变化
      this._heightMediaQuery.addEventListener('change', this._heightHandleMediaChange)
    },
    beforeDestroy() {
      // 组件销毁时移除监听
      if (this._heightMediaQuery && this._heightHandleMediaChange) {
        this._heightMediaQuery.removeEventListener('change', this._heightHandleMediaChange)
        // 清理引用
        this._heightMediaQuery = null
        this._heightHandleMediaChange = null
      }
    }
  }
}

/**
 * Vue2 Mixin：同时监听宽度 + 高度阈值变化（响应式）
 * @param {number} currentWidth 宽度阈值
 * @param {number} currentHeight 高度阈值
 * @param {Function} widthCallback 宽度变化回调
 * @param {Function} heightCallback 高度变化回调
 * @returns {Object} Vue2 Mixin 对象
 */
export function useLayoutDetector(
  currentWidth = maxWidth,
  currentHeight = maxHeight,
  widthCallback,
  heightCallback
) {
  // 合并宽度/高度检测的 mixin
  const widthMixin = useWidthDetector(currentWidth, widthCallback)
  const heightMixin = useHeightDetector(currentHeight, heightCallback)

  return {
    data() {
      return {
        ...widthMixin.data(), // 合并 widthFlag
        ...heightMixin.data() // 合并 heightFlag
      }
    },
    mounted() {
      // 执行宽度检测的 mounted 逻辑
      widthMixin.mounted.call(this)
      // 执行高度检测的 mounted 逻辑
      heightMixin.mounted.call(this)
    },
    beforeDestroy() {
      // 执行宽度检测的销毁逻辑
      widthMixin.beforeDestroy.call(this)
      // 执行高度检测的销毁逻辑
      heightMixin.beforeDestroy.call(this)
    }
  }
}
