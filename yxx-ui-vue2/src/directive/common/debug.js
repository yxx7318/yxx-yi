/**
 * v-debug 调试指令
 * 使用示例：
 *   <div v-debug>默认边框</div>
 *   <div v-debug:green>绿色边框</div>
 *   <div v-debug="'blue'">蓝色边框</div>
 *   <div v-debug.gird.label.size>组合功能</div>
 */
export default {
  beforeMount: applyDebug,
  updated: applyDebug,
  beforeUnmount: cleanDebug
}

/**
 * 应用调试样式
 */
function applyDebug(el, binding) {
  if (import.meta.env.VITE_APP_ENV === 'production') return

  cleanDebug(el) // 清理之前的调试元素

  const { value, arg, modifiers } = binding
  const config = parseConfig(value, arg, modifiers)

  applyBaseStyle(el, config)
  handleModifiers(el, modifiers, config)
}

/**
 * 解析配置参数
 */
function parseConfig(value, arg, modifiers) {
  const isObject = typeof value === 'object'
  const isString = typeof value === 'string'

  return {
    color: arg || (isString ? value : 'red'),
    width: (isObject && value.width) || '2px',
    style: (isObject && value.style) || 'solid',
    background: (isObject && value.background) || null,
    label: (isObject && value.label) || null,
    zIndex: 9999,
    ...(isObject ? value : {})
  }
}

/**
 * 应用基础边框样式
 */
function applyBaseStyle(el, config) {
  el.style.border = `${config.width} ${config.style} ${config.color}`
  if (config.background) el.style.background = config.background
  el.style.zIndex = config.zIndex
}

/**
 * 处理修饰符功能
 */
function handleModifiers(el, modifiers, config) {
  // 网格背景
  if (modifiers.grid) {
    el.style.backgroundImage = `linear-gradient(rgba(0,0,0,0.1) 1px, transparent 1px),
                               linear-gradient(90deg, rgba(0,0,0,0.1) 1px, transparent 1px)`
    el.style.backgroundSize = '20px 20px'
  }

  // 调试标签
  if (modifiers.label || config.label) {
    createDebugElement(el, config, 'label')
  }

  // 尺寸显示
  if (modifiers.size) {
    createDebugElement(el, config, 'size')
  }
}

/**
 * 创建调试元素（标签/尺寸显示）
 */
function createDebugElement(el, config, type) {
  const debugEl = document.createElement('div')
  debugEl.className = `v-debug-${type}`

  const baseStyle = {
    position: 'absolute',
    background: config.color,
    color: 'white',
    fontSize: '10px',
    padding: '2px 6px',
    fontFamily: 'monospace',
    fontWeight: 'bold',
    zIndex: config.zIndex + 1
  }

  if (type === 'label') {
    debugEl.textContent = config.label || getElementLabel(el)
    Object.assign(debugEl.style, baseStyle, {
      top: '0', left: '0', borderRadius: '0 0 3px 0'
    })
  } else if (type === 'size') {
    const updateSize = () => {
      const { width, height } = el.getBoundingClientRect()
      debugEl.textContent = `${Math.round(width)}×${Math.round(height)}`
    }

    Object.assign(debugEl.style, baseStyle, {
      bottom: '-20px', right: '0', borderRadius: '2px'
    })

    // 监听尺寸变化
    const observer = new ResizeObserver(updateSize)
    observer.observe(el)
    el._debugResizeObserver = observer
    updateSize()
  }

  el.appendChild(debugEl)
}

/**
 * 生成元素标签文本
 */
function getElementLabel(el) {
  const classNames = el.className ? `.${el.className.split(' ').join('.')}` : ''
  return `${el.tagName.toLowerCase()}${classNames}`
}

/**
 * 清理调试元素和样式
 */
function cleanDebug(el) {
  // 清理调试元素
  el.querySelectorAll('.v-debug-label, .v-debug-size').forEach(node => node.remove())

  // 清理观察器
  if (el._debugResizeObserver) {
    el._debugResizeObserver.disconnect()
    delete el._debugResizeObserver
  }

  // 重置样式
  el.style.border = el.style.backgroundImage = el.style.zIndex = ''
}
