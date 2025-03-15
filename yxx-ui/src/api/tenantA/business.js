import request from '@/utils/request'

// 查询xxx业务列表
export function listBusiness(query) {
  return request({
    url: '/tenantA/business/list',
    method: 'get',
    params: query
  })
}

// 查询xxx业务详细
export function getBusiness(businessId) {
  return request({
    url: '/tenantA/business/' + businessId,
    method: 'get'
  })
}

// 新增xxx业务
export function addBusiness(data) {
  return request({
    url: '/tenantA/business',
    method: 'post',
    data: data
  })
}

// 修改xxx业务
export function updateBusiness(data) {
  return request({
    url: '/tenantA/business',
    method: 'put',
    data: data
  })
}

// 删除xxx业务
export function delBusiness(businessId) {
  return request({
    url: '/tenantA/business/' + businessId,
    method: 'delete'
  })
}
