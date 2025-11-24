import request from '@/utils/request'

// 查询测试树表生成列表
export function listUserTree(query) {
  return request({
    url: '/business/userTree/list',
    method: 'get',
    params: query
  })
}

// 查询测试树表生成详细
export function getUserTree(userId) {
  return request({
    url: '/business/userTree/' + userId,
    method: 'get'
  })
}

// 新增测试树表生成
export function addUserTree(data) {
  return request({
    url: '/business/userTree',
    method: 'post',
    data: data
  })
}

// 修改测试树表生成
export function updateUserTree(userId, data) {
  return request({
    url: '/business/userTree/' + userId,
    method: 'put',
    data: data
  })
}

// 删除测试树表生成
export function delUserTree(userId) {
  return request({
    url: '/business/userTree/' + userId,
    method: 'delete'
  })
}
