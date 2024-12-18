import request from '@/utils/request'

export default {
  getUserList(searchModel) {
    return request({
      url: '/user/list',
      method: 'get',
      params: {
        username: searchModel.username,
        card: searchModel.card,
        userSchoolId: searchModel.userSchoolId,
        userRoleId: searchModel.userRoleId,
        pageNo: searchModel.pageNo,
        pageSize: searchModel.pageSize
      }
    })
  },
  getCardrList(searchModel) {
    return request({
      url: '/user/cardList',
      method: 'get',
      params: {
        username: searchModel.username,
        card: searchModel.card,
        userSchoolId: searchModel.userSchoolId,
        userRoleId: searchModel.userRoleId,
        pageNo: searchModel.pageNo,
        pageSize: searchModel.pageSize
      }
    })
  },
  addUser(user) {
    return request({
      url: '/user',
      method: 'post',
      data: user
    })
  },
  updateUser(user) {
    return request({
      url: '/user',
      method: 'put',
      data: user
    })
  },
  saveUser(user) {
    if (user.userId == null || user.userId === undefined) {
      return this.addUser(user)
    }
    return this.updateUser(user)
  },
  getUserById(id) {
    return request({
      // url: '/user/' + id,
      url: `/user/${id}`,
      method: 'get'
    })
  },
  deleteUserById(id) {
    return request({
      url: `/user/${id}`,
      method: 'delete'
    })
  },
  getAllSciUserList() {
    return request({
      url: '/user/allSci',
      method: 'get'
    })
  },
  getAllUserList() {
    return request({
      url: '/user/all',
      method: 'get'
    })
  },
  updateKeyInfo(user) {
    return request({
      url: '/user/keyInfoChange',
      method: 'put',
      data: user
    })
  },
  getNewCard() {
    return request({
      url: '/user/newCard',
      method: 'get'
    })
  }
}
