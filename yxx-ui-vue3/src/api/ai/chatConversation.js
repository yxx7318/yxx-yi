import request from '@/utils/request'

// 查询会话列表
export function listChatConversation(query) {
  return request({
    url: '/ai/chatConversation/list',
    method: 'get',
    params: query
  })
}

// 查询会话详细
export function getChatConversation(chatConversationId) {
  return request({
    url: '/ai/chatConversation/' + chatConversationId,
    method: 'get'
  })
}

// 新增会话
export function addChatConversation(data) {
  return request({
    url: '/ai/chatConversation',
    method: 'post',
    data: data
  })
}

// 修改会话
export function updateChatConversation(chatConversationId, data) {
  return request({
    url: '/ai/chatConversation/' + chatConversationId,
    method: 'put',
    data: data
  })
}

// 删除会话
export function delChatConversation(chatConversationId) {
  return request({
    url: '/ai/chatConversation/' + chatConversationId,
    method: 'delete'
  })
}
