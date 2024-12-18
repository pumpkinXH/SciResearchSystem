import request from '@/utils/request'

export default {
  getAllMenu() {
    return request({
      url: 'menu',
      method: 'get'
    })
  }
}
