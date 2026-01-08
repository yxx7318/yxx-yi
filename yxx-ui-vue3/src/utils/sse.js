import { useXStream } from 'vue-element-plus-x'
import { getUrl, requestInterceptor } from '@/utils/fetch'

const { startStream, cancel, data, error, isLoading } = useXStream()

/**
 * 获取SSE响应内容
 * @type {ComputedRef<string>} SSE响应内容
 */
const content = computed(() => {
  if (!data.value.length) return ''
  let text = ''
  for (let index = 0; index < data.value.length; index++) {
    const chunk = data.value[index].data
    try {
      const parsedChunk = JSON.parse(chunk).content
      text += parsedChunk
    } catch (error) {
      // 这个 结束标识 是后端给的，所以这里这样判断
      // 实际项目中，以项目需要为准
      if (chunk === ' [DONE]') {
        // 处理数据结束的情况
        // console.log('数据接收完毕')
      } else {
        console.error('解析数据时出错:', error)
      }
    }
    // console.log('New chunk:', chunk)
  }
  console.log('Text:', text)
  return text
})

/**
 * SSE请求方法
 * @param {Object} config - 请求配置
 */
export const SSERequest = async (config) => {
  try {
    // 处理请求配置
    const processedConfig = requestInterceptor({
      ...config,
      url: getUrl(config)
    })
    console.log(processedConfig.headers, "processedConfig.headers")
    // 发送请求
    const response = await fetch(processedConfig.url, {
      method: processedConfig.method || 'GET',
      headers: {
        'Content-Type': 'text/event-stream',
        ...processedConfig.headers
      },
      body: processedConfig.data ? JSON.stringify(processedConfig.data) : null,
    })
    const readableStream = response.body
    await startStream({ readableStream })
  } catch (err) {
    console.error('Fetch error:', err)
  }
  // 返回响应内容
  return content
}

/**
 * 取消SSE请求
 * @type {() => void}
 */
export const SSECancel = cancel

/**
 * SSE请求错误内容
 * @type {globalThis.Ref<Error | null, Error | null>}
 */
export const SSEError = error

/**
 * SSE是否在请求中标记
 * @type {globalThis.Ref<boolean, boolean>}
 */
export const SSEisLoading = isLoading
