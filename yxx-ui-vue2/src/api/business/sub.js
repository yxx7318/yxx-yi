import request from '@/utils/request'

// 查询测试主表生成列表
export function listSub(query) {
  return request({
    url: '/business/sub/list',
    method: 'get',
    params: query
  })
}

// 查询测试主表生成详细
export function getSub(subId) {
  return request({
    url: '/business/sub/' + subId,
    method: 'get'
  })
}

// 新增测试主表生成
export function addSub(data) {
  return request({
    url: '/business/sub',
    method: 'post',
    data: data
  })
}

// 修改测试主表生成
export function updateSub(subId, data) {
  return request({
    url: '/business/sub/' + subId,
    method: 'put',
    data: data
  })
}

// 删除测试主表生成
export function delSub(subId) {
  return request({
    url: '/business/sub/' + subId,
    method: 'delete'
  })
}
