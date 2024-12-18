import request from '@/utils/request'

export default {
  deProVisBySchool() {
    return request({
      url: '/echarts/deProVisBySchool',
      method: 'get'
    })
  },
  proVisByResType() {
    return request({
      url: '/echarts/proVisByResType',
      method: 'get'
    })
  },
  proVisByStatus() {
    return request({
      url: '/echarts/proVisByStatus',
      method: 'get'
    })
  },
  conProVisBySchool() {
    return request({
      url: '/echarts/conProVisBySchool',
      method: 'get'
    })
  },
  resVisByType() {
    return request({
      url: '/echarts/resVisByType',
      method: 'get'
    })
  },
  resVisByStatus() {
    return request({
      url: '/echarts/resVisByStatus',
      method: 'get'
    })
  },
  deResVisBySchool() {
    return request({
      url: '/echarts/deResVisBySchool',
      method: 'get'
    })
  },
  passedResVisBySchool() {
    return request({
      url: '/echarts/passedResVisBySchool',
      method: 'get'
    })
  },
  userVisBySchool() {
    return request({
      url: '/echarts/userVisBySchool',
      method: 'get'
    })
  },
  userVisByRole() {
    return request({
      url: '/echarts/userVisByRole',
      method: 'get'
    })
  }
}
