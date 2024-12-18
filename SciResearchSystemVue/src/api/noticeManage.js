import request from '@/utils/request'

export default {
  getNoticeList(searchModel) {
    return request({
      url: '/notice/list',
      method: 'get',
      params: {
        noticeTitle: searchModel.noticeTitle,
        schoolId: searchModel.schoolId,
        pageNo: searchModel.pageNo,
        pageSize: searchModel.pageSize
      }
    })
  },
  addNotice(notice) {
    return request({
      url: '/notice',
      method: 'post',
      data: notice
    })
  },
  updateNotice(notice) {
    return request({
      url: '/notice',
      method: 'put',
      data: notice
    })
  },
  saveNotice(notice) {
    if (notice.noticeId == null || notice.noticeId === undefined) {
      return this.addNotice(notice)
    }
    return this.updateNotice(notice)
  },
  getNoticeById(id) {
    return request({
      url: `/notice/${id}`,
      method: 'get'
    })
  },
  deleteNoticeById(id) {
    return request({
      url: `/notice/${id}`,
      method: 'delete'
    })
  }
}
