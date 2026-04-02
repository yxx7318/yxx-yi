import { ref, onMounted, onBeforeUnmount } from 'vue'

// 宽度阈值：默认 992px（另一种常见移动端检测阈值为 768px）
let maxWidth = 992
// 高度阈值：默认 660px
let maxHeight = 660

/**
 * 直接判断当前是否为移动端（单次判断，非响应式）
 * @returns {boolean} 视口宽度小于阈值返回 true，否则返回 false
 */
export function mobileFlag() {
  return window.innerWidth < maxWidth
}

/**
 * 创建宽度媒体查询对象
 * @param {number} currentWidth 自定义宽度阈值，默认使用全局 maxWidth
 * @returns {MediaQueryList} 媒体查询对象
 */
function getWidthMediaQuery(currentWidth = maxWidth) {
  return window.matchMedia(`(max-width: ${currentWidth}px)`)
}

/**
 * 创建高度媒体查询对象
 * @param {number} currentHeight 自定义高度阈值，默认使用全局 maxHeight
 * @returns {MediaQueryList} 媒体查询对象
 */
function getHeightMediaQuery(currentHeight = maxHeight) {
  return window.matchMedia(`(max-height: ${currentHeight}px)`)
}

/**
 * 生成宽度媒体查询变化的处理函数
 * @param {Ref<boolean>} widthFlag 响应式状态，用于存储宽度是否满足阈值
 * @param {Function} widthCallback 宽度变化时的回调函数（可选）
 * @returns {Function} 媒体查询变化的处理函数
 */
function getWidthHandleMediaChange(widthFlag, widthCallback) {
  return (e) => {
    // 直接通过 e.matches 获取媒体查询结果（是否满足 max-width 条件）
    widthFlag.value = e.matches
    // 如果传入了回调函数，则执行回调，并传递当前状态和视口尺寸
    if (widthCallback && typeof widthCallback === 'function') {
      const innerHeight = window.innerHeight
      const innerWidth = window.innerWidth
      widthCallback(widthFlag.value, innerHeight, innerWidth)
    }
  }
}

/**
 * 生成高度媒体查询变化的处理函数
 * @param {Ref<boolean>} heightFlag 响应式状态，用于存储高度是否满足阈值
 * @param {Function} heightCallback 高度变化时的回调函数（可选）
 * @returns {Function} 媒体查询变化的处理函数
 */
function getHeightHandleMediaChange(heightFlag, heightCallback) {
  return (e) => {
    // 直接通过 e.matches 获取媒体查询结果（是否满足 max-height 条件）
    heightFlag.value = e.matches
    // 如果传入了回调函数，则执行回调，并传递当前状态和视口尺寸
    if (heightCallback && typeof heightCallback === 'function') {
      const innerHeight = window.innerHeight
      const innerWidth = window.innerWidth
      heightCallback(heightFlag.value, innerHeight, innerWidth)
    }
  }
}

/**
 * Vue3 Composable：监听宽度阈值变化（响应式）
 * @param {number} currentWidth 自定义宽度阈值，默认使用全局 maxWidth
 * @param {Function} widthCallback 宽度变化时的回调函数（可选）
 * @returns {Ref<boolean>} 响应式状态，代表当前宽度是否小于阈值
 */
export function useWidthDetector(currentWidth = maxWidth, widthCallback) {
  // 定义响应式状态，初始值为 false
  const widthFlag = ref(false)
  // 创建宽度媒体查询对象
  const widthMediaQuery = getWidthMediaQuery(currentWidth)
  // 生成宽度媒体查询变化的处理函数
  const widthHandleMediaChange = getWidthHandleMediaChange(widthFlag, widthCallback)

  // 组件挂载时：初始化状态 + 监听媒体查询变化
  onMounted(() => {
    // 初始化时手动执行一次，确保初始状态正确（传入媒体查询对象作为参数）
    widthHandleMediaChange(widthMediaQuery)
    // 监听媒体查询的“change”事件（仅当视口跨越阈值时触发）
    widthMediaQuery.addEventListener('change', widthHandleMediaChange)
  })

  // 组件卸载前：移除监听，防止内存泄漏
  onBeforeUnmount(() => {
    widthMediaQuery.removeEventListener('change', widthHandleMediaChange)
  })

  // 将响应式状态暴露给组件使用
  return widthFlag
}

/**
 * Vue3 Composable：监听高度阈值变化（响应式）
 * @param {number} currentHeight 自定义高度阈值，默认使用全局 maxHeight
 * @param {Function} heightCallback 高度变化时的回调函数（可选）
 * @returns {Ref<boolean>} 响应式状态，代表当前高度是否小于阈值
 */
export function useHeightDetector(currentHeight = maxHeight, heightCallback) {
  // 定义响应式状态，初始值为 false
  const heightFlag = ref(false)
  // 创建高度媒体查询对象
  const heightMediaQuery = getHeightMediaQuery(currentHeight)
  // 生成高度媒体查询变化的处理函数
  const heightHandleMediaChange = getHeightHandleMediaChange(heightFlag, heightCallback)

  // 组件挂载时：初始化状态 + 监听媒体查询变化
  onMounted(() => {
    // 初始化时手动执行一次，确保初始状态正确
    heightHandleMediaChange(heightMediaQuery)
    // 监听媒体查询的“change”事件
    heightMediaQuery.addEventListener('change', heightHandleMediaChange)
  })

  // 组件卸载前：移除监听，防止内存泄漏
  onBeforeUnmount(() => {
    heightMediaQuery.removeEventListener('change', heightHandleMediaChange)
  })

  // 将响应式状态暴露给组件使用
  return heightFlag
}

/**
 * Vue3 Composable：同时监听宽度和高度阈值变化（响应式）
 * @param {number} currentWidth 自定义宽度阈值，默认使用全局 maxWidth
 * @param {number} currentHeight 自定义高度阈值，默认使用全局 maxHeight
 * @param {Function} widthCallback 宽度变化时的回调函数（可选）
 * @param {Function} heightCallback 高度变化时的回调函数（可选）
 * @returns {Object} 包含 widthFlag 和 heightFlag 两个响应式状态的对象
 */
export function useLayoutDetector(currentWidth = maxWidth, currentHeight = maxHeight,
                                  widthCallback, heightCallback) {
  // 复用宽度检测逻辑
  let widthFlag = useWidthDetector(currentWidth, widthCallback)
  // 复用高度检测逻辑
  let heightFlag = useHeightDetector(currentHeight, heightCallback)

  // 将两个响应式状态暴露给组件使用
  return {
    widthFlag, heightFlag
  }
}
