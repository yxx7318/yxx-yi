import request from '@/utils/request'

// 查询数据源管理列表
export function listResources(query) {
  return request({
    url: '/system/resources/list',
    method: 'get',
    params: query
  })
}

// 查询数据源管理详细
export function getResources(resourceId) {
  return request({
    url: '/system/resources/' + resourceId,
    method: 'get'
  })
}

// 新增数据源管理
export function addResources(data) {
  return request({
    url: '/system/resources',
    method: 'post',
    data: data
  })
}

// 修改数据源管理
export function updateResources(data) {
  return request({
    url: '/system/resources',
    method: 'put',
    data: data
  })
}

// 删除数据源管理
export function delResources(resourceId) {
  return request({
    url: '/system/resources/' + resourceId,
    method: 'delete'
  })
}
