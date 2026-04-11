import request from '@/utils/request'

// 查询操作日志列表
export function list(query) {
  return request({
    url: '/monitor/operLog/list',
    method: 'get',
    params: query
  })
}

// 删除操作日志
export function delOperLog(operId) {
  return request({
    url: '/monitor/operLog/' + operId,
    method: 'delete'
  })
}

// 清空操作日志
export function cleanOperLog() {
  return request({
    url: '/monitor/operLog/clean',
    method: 'delete'
  })
}
