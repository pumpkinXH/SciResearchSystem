import request from '@/utils/request'

export default {
  getAllResultsTypeList() {
    return request({
      url: '/resType/all',
      method: 'get'
    })
  }
}
