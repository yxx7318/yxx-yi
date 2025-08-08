import request from '@/utils/request'

// 查询测试树表生成列表
export function listTree(query) {
  return request({
    url: '/business/tree/list',
    method: 'get',
    params: query
  })
}

// 查询测试树表生成详细
export function getTree(userId) {
  return request({
    url: '/business/tree/' + userId,
    method: 'get'
  })
}

// 新增测试树表生成
export function addTree(data) {
  return request({
    url: '/business/tree',
    method: 'post',
    data: data
  })
}

// 修改测试树表生成
export function updateTree(userId, data) {
  return request({
    url: '/business/tree/' + userId,
    method: 'put',
    data: data
  })
}

// 删除测试树表生成
export function delTree(userId) {
  return request({
    url: '/business/tree/' + userId,
    method: 'delete'
  })
}
