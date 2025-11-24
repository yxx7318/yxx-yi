import request from '@/utils/request'

// 查询测试单表生成列表
export function listTestUser(query) {
  return request({
    url: '/business/testUser/list',
    method: 'get',
    params: query
  })
}

// 查询测试单表生成详细
export function getTestUser(userId) {
  return request({
    url: '/business/testUser/' + userId,
    method: 'get'
  })
}

// 新增测试单表生成
export function addTestUser(data) {
  return request({
    url: '/business/testUser',
    method: 'post',
    data: data
  })
}

// 修改测试单表生成
export function updateTestUser(userId, data) {
  return request({
    url: '/business/testUser/' + userId,
    method: 'put',
    data: data
  })
}

// 删除测试单表生成
export function delTestUser(userId) {
  return request({
    url: '/business/testUser/' + userId,
    method: 'delete'
  })
}
