import request from '@/utils/request'
import { streamRequest } from '@/utils/fetch'
import { SSERequest } from '@/utils/sse'

// 查询会话列表
export function getSessionList(query) {
  return request({
    url: '/ai/history',
    method: 'get',
    params: query
  })
}

// 查询会话详细
export function getSession(conversationId) {
  return request({
    url: '/ai/history/detail/' + conversationId,
    method: 'get'
  })
}

// 新增会话历史
export function addSessionHistory(data) {
  return request({
    url: '/ai/history',
    method: 'post',
    data: data
  })
}

// 新增会话
export function addSession(data) {
  return request({
    url: '/ai/session',
    method: 'post',
    data: data
  })
}

// 修改会话
export function updateSession(userId, data) {
  return request({
    url: '/ai/session/' + userId,
    method: 'put',
    data: data
  })
}

// 删除会话
export function delSession(sessionId) {
  return request({
    url: '/ai/session/' + sessionId,
    method: 'delete'
  })
}

// 开始对话
export function startChat(data, onData, onError, onComplete) {
  return streamRequest({
    url: '/ai/session/chat',
    method: 'post',
    data: data,
  }, onData, onError, onComplete)
}

// 开始对话(SSE协议)
export function startChatSSE(data) {
  return SSERequest({
    url: '/ai/session/chat',
    method: 'post',
    data: data,
  })
}


