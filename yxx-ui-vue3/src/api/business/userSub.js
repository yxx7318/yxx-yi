import request from '@/utils/request'

// 查询测试主表生成列表
export function listUserSub(query) {
  return request({
    url: '/business/userSub/list',
    method: 'get',
    params: query
  })
}

// 查询测试主表生成详细
export function getUserSub(subId) {
  return request({
    url: '/business/userSub/' + subId,
    method: 'get'
  })
}

// 新增测试主表生成
export function addUserSub(data) {
  return request({
    url: '/business/userSub',
    method: 'post',
    data: data
  })
}

// 修改测试主表生成
export function updateUserSub(subId, data) {
  return request({
    url: '/business/userSub/' + subId,
    method: 'put',
    data: data
  })
}

// 删除测试主表生成
export function delUserSub(subId) {
  return request({
    url: '/business/userSub/' + subId,
    method: 'delete'
  })
}
