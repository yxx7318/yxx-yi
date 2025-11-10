import { ElNotification, ElMessageBox, ElMessage, ElLoading } from 'element-plus'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { tansParams, blobValidate } from '@/utils/yxx.js'
import cache from '@/plugins/cache'
import { saveAs } from 'file-saver'
import useUserStore from '@/store/modules/user'

let downloadLoadingInstance
export let isRelogin = { show: false }

const BASE_URL = import.meta.env.VITE_APP_BASE_API
const TIMEOUT = import.meta.env.VITE_APP_ENV === 'development' ? -1 : 10000

// 获取请求地址
const getUrl = (config) => {
  let currentUrl = config.url || config
  return currentUrl.startsWith('http') ? currentUrl : BASE_URL + currentUrl
}

// 请求拦截器
const requestInterceptor = (config) => {
  // 是否需要设置 token
  const isToken = (config.headers || {}).isToken === false
  // 是否需要防止数据重复提交(默认开启，repeatSubmit值为false时关闭)
  const isRepeatSubmit = (config.headers || {}).repeatSubmit === false
  // 间隔时间(ms)，小于此时间视为重复提交
  const interval = (config.headers || {}).interval || 1000

  // 确保 headers 对象存在
  config.headers = config.headers || {}

  // 添加 token
  if (getToken() && !isToken) {
    config.headers['Authorization'] = 'Bearer ' + getToken()
  }

  // get请求映射params参数
  if (config.method === 'GET' || config.method === 'get' && config.params) {
    let url = config.url + '?' + tansParams(config.params)
    url = url.slice(0, -1)
    config.params = {}
    config.url = url
  }

  // 防重复提交
  if (!isRepeatSubmit && (config.method === 'POST' || config.method === 'PUT' || config.method === 'post' || config.method === 'put')) {
    const requestObj = {
      url: config.url,
      data: typeof config.data === 'object' ? JSON.stringify(config.data) : config.data,
      time: new Date().getTime()
    }

    const requestSize = Object.keys(JSON.stringify(requestObj)).length // 请求数据大小
    const limitSize = 5 * 1024 * 1024 // 限制存放数据5M

    if (requestSize >= limitSize) {
      console.warn(`[${config.url}]: ` + '请求数据大小超出允许的5M限制，无法进行防重复提交验证。')
      return config
    }

    const sessionObj = cache.session.getJSON('sessionObj')
    if (sessionObj === undefined || sessionObj === null || sessionObj === '') {
      cache.session.setJSON('sessionObj', requestObj)
    } else {
      const s_url = sessionObj.url                // 请求地址
      const s_data = sessionObj.data              // 请求数据
      const s_time = sessionObj.time              // 请求时间

      if (s_data === requestObj.data && requestObj.time - s_time < interval && s_url === requestObj.url) {
        const message = '数据正在处理，请勿重复提交'
        console.warn(`[${s_url}]: ` + message)
        throw new Error(message)
      } else {
        cache.session.setJSON('sessionObj', requestObj)
      }
    }
  }

  return config
}

// 响应拦截器
const responseInterceptor = async (response, config) => {
  // 二进制数据直接返回
  if (config.responseType === 'blob' || config.responseType === 'arraybuffer') {
    return response.blob ? await response.blob() : response
  }

  const data = await response.json()
  const code = data.code || 200
  const msg = errorCode[code] || data.msg || errorCode['default']

  if (code === 401) {
    if (!isRelogin.show) {
      isRelogin.show = true
      ElMessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', {
        confirmButtonText: '重新登录',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        isRelogin.show = false
        useUserStore().logOut().then(() => {
          location.href = (import.meta.env.VITE_APP_PUBLIC_PATH || '') + '/index'
        })
      }).catch(() => {
        isRelogin.show = false
      })
    }
    throw new Error('无效的会话，或者会话已过期，请重新登录。')
  } else if (code === 500) {
    ElMessage({ message: msg, type: 'error' })
    throw new Error(msg)
  } else if (code === 601) {
    ElMessage({ message: msg, type: 'warning' })
    throw new Error(msg)
  } else if (code !== 200) {
    ElNotification.error({ title: msg })
    throw new Error('error')
  }

  return data
}

// 错误处理
const errorHandler = (error) => {
  console.error('err' + error)
  let message = error.message || error
  message = "系统接口请求异常：" + message
  ElMessage({ message: message, type: 'error', duration: 5 * 1000 })
  throw error
}

// 核心请求方法
const request = async (config) => {
  try {
    // 应用请求拦截器
    const processedConfig = requestInterceptor({
      ...config,
      url: getUrl(config)
    })

    // 处理请求数据
    let body = null
    if (processedConfig.data) {
      if (processedConfig.headers['Content-Type'] === 'application/x-www-form-urlencoded') {
        body = tansParams(processedConfig.data)
      } else {
        body = JSON.stringify(processedConfig.data)
      }
    }

    // 创建超时控制器
    const controller = new AbortController()
    const timeoutId = TIMEOUT > 0 ? setTimeout(() => controller.abort(), TIMEOUT) : null

    // 发送请求
    const response = await fetch(processedConfig.url, {
      method: processedConfig.method || 'GET',
      headers: {
        'Content-Type': 'application/json;charset=utf-8',
        ...processedConfig.headers
      },
      body: body,
      signal: controller.signal
    })

    // 清除超时定时器
    if (timeoutId) clearTimeout(timeoutId)

    // 应用响应拦截器
    return await responseInterceptor(response, processedConfig)
  } catch (error) {
    return errorHandler(error)
  }
}

/**
 * 流式请求方法
 * @param {Object} config - 请求配置
 * @param {Function} onData - 数据接收回调函数
 * @param {Function} onError - 错误处理回调函数
 * @param {Function} onComplete - 完成回调函数
 */
export const streamRequest = async (config, onData, onError, onComplete) => {
  try {
    // 处理请求配置
    const processedConfig = requestInterceptor({
      ...config,
      url: getUrl(config)
    })

    // 创建中止控制器，支持自定义超时时间
    const controller = new AbortController()
    const timeout = config.timeout || TIMEOUT

    // 只有设置了超时时间才启用超时控制
    let timeoutId = null
    if (timeout > 0) {
      timeoutId = setTimeout(() => controller.abort(), timeout)
    }

    // 发送请求
    const response = await fetch(processedConfig.url, {
      method: processedConfig.method || 'GET',
      headers: {
        'Content-Type': 'application/json;charset=utf-8',
        ...processedConfig.headers
      },
      body: processedConfig.data ? JSON.stringify(processedConfig.data) : null,
      signal: controller.signal
    })

    // 清除超时定时器
    if (timeoutId) clearTimeout(timeoutId)

    // 检查响应状态
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    // 获取流读取器
    const reader = response.body.getReader()
    const decoder = new TextDecoder()

    try {
      let resultChunk = ""
      // 持续读取流数据
      while (true) {
        const { done, value } = await reader.read()

        // 流读取完成
        if (done) {
          if (onComplete) onComplete(resultChunk)
          break
        }

        // 将二进制数据解码为文本并传递给回调
        const chunk = decoder.decode(value, { stream: true })
        resultChunk = resultChunk + chunk
        if (onData) onData(chunk)
      }
    } finally {
      // 释放读取器锁
      reader.releaseLock()
    }
  } catch (error) {
    // 错误处理
    if (onError) onError(error)
    errorHandler(error)
  }
}

// 通用下载方法
export const download = (url, params, filename, config) => {
  console.log("download")
  downloadLoadingInstance = ElLoading.service({ text: "正在下载数据，请稍候", background: "rgba(0, 0, 0, 0.7)", })

  return request({
    url,
    method: 'POST',
    data: params,
    headers: { 'Content-Type': 'application/x-www-form-urlencoded', ...config?.headers },
    responseType: 'blob',
    ...config
  }).then(async (data) => {
    const isBlob = blobValidate(data)
    if (isBlob) {
      const blob = new Blob([data])
      saveAs(blob, filename)
    } else {
      const resText = await data.text()
      const rspObj = JSON.parse(resText)
      const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default']
      ElMessage.error(errMsg)
    }
    downloadLoadingInstance.close()
  }).catch((r) => {
    console.error(r)
    ElMessage.error('下载文件出现错误，请联系管理员！')
    downloadLoadingInstance.close()
  })
}

// 便捷方法
export const get = (url, params, config) => {
  return request({
    url,
    method: 'GET',
    params,
    ...config
  })
}

export const post = (url, data, config) => {
  return request({
    url,
    method: 'POST',
    data,
    ...config
  })
}

export const put = (url, data, config) => {
  return request({
    url,
    method: 'PUT',
    data,
    ...config
  })
}

export const del = (url, params, config) => {
  return request({
    url,
    method: 'DELETE',
    params,
    ...config
  })
}

export default request

