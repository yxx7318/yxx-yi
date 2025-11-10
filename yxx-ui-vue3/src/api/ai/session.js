import request from '@/utils/request'
import { streamRequest } from '@/utils/fetch'

// 查询会话列表
export function getSessionList(query) {
  return request({
    url: '/ai/history/chat',
    method: 'get',
    params: query
  })
}

// 查询会话详细
export function getSession(chatId) {
  return request({
    url: '/ai/history/chat/' + chatId,
    method: 'get'
  })
}

// 新增会话
export function addSession(data) {
  return request({
    url: '/ai/session',
    method: 'get',
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


