import request from '@/utils/request'
import axios from 'axios'
import { serverIp } from '@/serverConfig'

export default {
  getResultsList(searchModel) {
    return request({
      url: '/res/list',
      method: 'get',
      params: {
        resultsName: searchModel.resultsName,
        resultsChargeId: searchModel.resultsChargeId,
        resultsSchoolId: searchModel.resultsSchoolId,
        resultsSubject: searchModel.resultsSubject,
        typeId: searchModel.typeId,
        resultsStatusId: searchModel.resultsStatusId,
        pageNo: searchModel.pageNo,
        pageSize: searchModel.pageSize
      }
    })
  },
  getResultsAuditList(searchModel) {
    return request({
      url: '/res/auditList',
      method: 'get',
      params: {
        resultsName: searchModel.resultsName,
        resultsChargeId: searchModel.resultsChargeId,
        resultsSchoolId: searchModel.resultsSchoolId,
        resultsSubject: searchModel.resultsSubject,
        typeId: searchModel.typeId,
        resultsStatusId: searchModel.resultsStatusId,
        pageNo: searchModel.pageNo,
        pageSize: searchModel.pageSize
      }
    })
  },
  getSciResultsList(searchModel) {
    return request({
      url: '/res/sciList',
      method: 'get',
      params: {
        resultsName: searchModel.resultsName,
        resultsChargeId: searchModel.resultsChargeId,
        resultsSchoolId: searchModel.resultsSchoolId,
        resultsSubject: searchModel.resultsSubject,
        typeId: searchModel.typeId,
        resultsStatusId: searchModel.resultsStatusId,
        pageNo: searchModel.pageNo,
        pageSize: searchModel.pageSize
      }
    })
  },
  addResults(results) {
    return request({
      url: '/res',
      method: 'post',
      data: results
    })
  },
  updateResults(results) {
    return request({
      url: '/res',
      method: 'put',
      data: results
    })
  },
  saveResults(results) {
    if (results.resultsId == null || results.resultsId === undefined) {
      return this.addResults(results)
    }
    return this.updateResults(results)
  },
  getResultsById(id) {
    return request({
      url: `/res/${id}`,
      method: 'get'
    })
  },
  deleteResultsById(id) {
    return request({
      url: `/res/${id}`,
      method: 'delete'
    })
  },
  getExcelFile(excelModel) {
    const url = `http://${serverIp}:9999/res/exportExcel?resultsChargeId=${encodeURIComponent(excelModel.resultsChargeId)}&resultsSchoolId=${encodeURIComponent(excelModel.resultsSchoolId)}&resultsStatusId=${encodeURIComponent(excelModel.resultsStatusId)}`
    return axios.get(url, { responseType: 'blob' })
  }
}
