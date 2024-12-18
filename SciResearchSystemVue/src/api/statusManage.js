import request from '@/utils/request'

export default {
  getAllProStatusList() {
    return request({
      url: '/status/allPro',
      method: 'get'
    })
  },
  getAllResStatusList() {
    return request({
      url: '/status/allRes',
      method: 'get'
    })
  },
  getProStatusExceptDraft() {
    return request({
      url: '/status/proExceptDraft',
      method: 'get'
    })
  },
  getResStatusExceptDraft() {
    return request({
      url: '/status/resExceptDraft',
      method: 'get'
    })
  }
}
