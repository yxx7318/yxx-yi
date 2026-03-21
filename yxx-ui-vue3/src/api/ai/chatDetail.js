import request from '@/utils/request'

// 查询会话详细列表
export function listChatDetail(query) {
  return request({
    url: '/ai/chatDetail/list',
    method: 'get',
    params: query
  })
}

// 查询会话详细详细
export function getChatDetail(chatDetailId) {
  return request({
    url: '/ai/chatDetail/' + chatDetailId,
    method: 'get'
  })
}

// 新增会话详细
export function addChatDetail(data) {
  return request({
    url: '/ai/chatDetail',
    method: 'post',
    data: data
  })
}

// 修改会话详细
export function updateChatDetail(chatDetailId, data) {
  return request({
    url: '/ai/chatDetail/' + chatDetailId,
    method: 'put',
    data: data
  })
}

// 删除会话详细
export function delChatDetail(chatDetailId) {
  return request({
    url: '/ai/chatDetail/' + chatDetailId,
    method: 'delete'
  })
}
