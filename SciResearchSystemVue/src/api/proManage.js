import request from '@/utils/request'
import axios from 'axios'
import { serverIp } from '@/serverConfig'

export default {
  getProList(searchModel) {
    return request({
      url: '/pro/list',
      method: 'get',
      params: {
        proName: searchModel.proName,
        proChargeId: searchModel.proChargeId,
        proSchoolId: searchModel.proSchoolId,
        proSubject: searchModel.proSubject,
        resultsTypeId: searchModel.resultsTypeId,
        proStatusId: searchModel.proStatusId,
        pageNo: searchModel.pageNo,
        pageSize: searchModel.pageSize
      }
    })
  },
  getProAuditList(searchModel) {
    return request({
      url: '/pro/auditList',
      method: 'get',
      params: {
        proName: searchModel.proName,
        proChargeId: searchModel.proChargeId,
        proSchoolId: searchModel.proSchoolId,
        proSubject: searchModel.proSubject,
        resultsTypeId: searchModel.resultsTypeId,
        proStatusId: searchModel.proStatusId,
        pageNo: searchModel.pageNo,
        pageSize: searchModel.pageSize
      }
    })
  },
  getSciProList(searchModel) {
    return request({
      url: '/pro/sciList',
      method: 'get',
      params: {
        proName: searchModel.proName,
        proChargeId: searchModel.proChargeId,
        proSchoolId: searchModel.proSchoolId,
        proSubject: searchModel.proSubject,
        resultsTypeId: searchModel.resultsTypeId,
        proStatusId: searchModel.proStatusId,
        pageNo: searchModel.pageNo,
        pageSize: searchModel.pageSize
      }
    })
  },
  addPro(pro) {
    return request({
      url: '/pro',
      method: 'post',
      data: pro
    })
  },
  updatePro(pro) {
    return request({
      url: '/pro',
      method: 'put',
      data: pro
    })
  },
  savePro(pro) {
    if (pro.proId == null || pro.proId === undefined) {
      return this.addPro(pro)
    }
    return this.updatePro(pro)
  },
  getProById(id) {
    return request({
      url: `/pro/${id}`,
      method: 'get'
    })
  },
  deleteProById(id) {
    return request({
      url: `/pro/${id}`,
      method: 'delete'
    })
  },
  getExcelFile(excelModel) {
    const url = `http://${serverIp}:9999/pro/exportExcel?proChargeId=${encodeURIComponent(excelModel.proChargeId)}&proSchoolId=${encodeURIComponent(excelModel.proSchoolId)}&proStatusId=${encodeURIComponent(excelModel.proStatusId)}`
    return axios.get(url, { responseType: 'blob' })
  }
}
