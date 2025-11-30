import request from '@/utils/request'

// 查询发票信息列表
export function listInvoiceInfo(query) {
  return request({
    url: '/ai/invoiceInfo/list',
    method: 'get',
    params: query
  })
}

// 查询发票信息详细
export function getInvoiceInfo(invoiceInfoId) {
  return request({
    url: '/ai/invoiceInfo/' + invoiceInfoId,
    method: 'get'
  })
}

// 新增发票信息
export function addInvoiceInfo(data) {
  return request({
    url: '/ai/invoiceInfo',
    method: 'post',
    data: data
  })
}

// 修改发票信息
export function updateInvoiceInfo(invoiceInfoId, data) {
  return request({
    url: '/ai/invoiceInfo/' + invoiceInfoId,
    method: 'put',
    data: data
  })
}

// 删除发票信息
export function delInvoiceInfo(invoiceInfoId) {
  return request({
    url: '/ai/invoiceInfo/' + invoiceInfoId,
    method: 'delete'
  })
}
