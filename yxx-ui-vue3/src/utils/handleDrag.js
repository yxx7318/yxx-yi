/**
 * 处理 dragleave 事件，确保只在鼠标真正离开元素（而非进入子元素）时触发回调
 * @param {DragEvent} e - dragleave 事件对象
 * @param {Function} onLeave - 当判断为“真正离开”时执行的回调函数
 * @example @dragleave="handleDragLeave($event, () => isFilesUpload = false)"
 */
export function handleDragLeave(e, onLeave) {
  // 阻止默认行为，某些浏览器可能会有默认的拖拽反馈
  e.preventDefault()

  const currentTarget = e.currentTarget
  const relatedTarget = e.relatedTarget

  // 核心判断逻辑
  if (!relatedTarget || !currentTarget.contains(relatedTarget)) {
    // 如果回调函数存在且是一个函数，则执行它
    if (typeof onLeave === 'function') {
      onLeave()
    }
  }
}
