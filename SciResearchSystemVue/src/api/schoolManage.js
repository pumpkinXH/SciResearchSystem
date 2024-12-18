import request from '@/utils/request'

export default {
  getAllSchoolList() {
    return request({
      url: '/school/all',
      method: 'get'
    })
  },
  getAllSchoolExceptCollege() {
    return request({
      url: '/school/allExCo',
      method: 'get'
    })
  },
  getCollege() {
    return request({
      url: '/school/college',
      method: 'get'
    })
  },
  getSchoolList(searchModel) {
    return request({
      url: '/school/list',
      method: 'get',
      params: {
        schoolName: searchModel.schoolName,
        schoolDesc: searchModel.schoolDesc,
        pageNo: searchModel.pageNo,
        pageSize: searchModel.pageSize
      }
    })
  },
  addSchool(school) {
    return request({
      url: '/school',
      method: 'post',
      data: school
    })
  },
  updateSchool(school) {
    return request({
      url: '/school',
      method: 'put',
      data: school
    })
  },
  saveSchool(school) {
    if (school.schoolId == null || school.schoolId === undefined) {
      return this.addSchool(school)
    }
    return this.updateSchool(school)
  },
  getSchoolById(id) {
    return request({
      url: `/school/${id}`,
      method: 'get'
    })
  },
  deleteSchoolById(id) {
    return request({
      url: `/school/${id}`,
      method: 'delete'
    })
  }
}
