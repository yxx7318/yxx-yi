import request from '@/utils/request'

// 查询测试用户列表
export function listUser(query) {
  return request({
    url: '/business/user/list',
    method: 'get',
    params: query
  })
}

// 查询测试用户详细
export function getUser(userId) {
  return request({
    url: '/business/user/' + userId,
    method: 'get'
  })
}

// 新增测试用户
export function addUser(data) {
  return request({
    url: '/business/user',
    method: 'post',
    data: data
  })
}

// 修改测试用户
export function updateUser(userId, data) {
  return request({
    url: '/business/user/' + userId,
    method: 'put',
    data: data
  })
}

// 删除测试用户
export function delUser(userId) {
  return request({
    url: '/business/user/' + userId,
    method: 'delete'
  })
}
