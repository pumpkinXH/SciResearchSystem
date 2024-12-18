<!-- eslint-disable vue/html-closing-bracket-newline -->
<!-- eslint-disable vue/html-indent -->
<!-- eslint-disable vue/max-attributes-per-line -->
<template>
  <div>
    <!--搜索框-->
    <el-card id="search">
      <el-row>
        <el-col :span="20">
          <el-input v-model="searchModel.proName" placeholder="项目名" clearable />
          <el-input v-model="searchModel.proSubject" placeholder="项目学科" clearable />
          <el-select v-model="searchModel.resultsTypeId" placeholder="请选择成果类别" filterable clearable>
            <el-option v-for="item in resultsTypeList" :key="item.resultsTypeId" :label="item.resultsTypeDesc"
              :value="item.resultsTypeId" />
          </el-select>
          <el-button type="primary" icon="el-icon-search" round @click="getSciProList">查询</el-button>
        </el-col>
      </el-row>
    </el-card>
    <!--结果列表-->
    <el-card>
      <el-table :data="proList" stripe style="width: 100%">
        <el-table-column label="序号" width="70">
          <template slot-scope="scope">
            <!--(pageNo-1) * pageSize + index + 1-->
            {{ (searchModel.pageNo - 1) * searchModel.pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="proName" label="项目名" width="120" />
        <el-table-column prop="proChargeName" label="负责人" width="100" />
        <el-table-column prop="schoolDesc" label="项目所属" width="120" />
        <el-table-column prop="proSubject" label="项目学科" width="120" />
        <el-table-column prop="resultsTypeDesc" label="成果类别" width="100" />
        <el-table-column prop="statusDesc" label="项目状态" width="120">
            <template slot-scope="scope">
              <el-tag>{{ scope.row.statusDesc }}</el-tag>
            </template>
          </el-table-column>
        <el-table-column prop="members" label="项目成员" width="110" />
        <el-table-column prop="budget" label="项目预算" width="100" />
        <el-table-column prop="updateTime" label="修改时间" width="100" />
        <el-table-column label="提交" width="80">
          <template slot-scope="scope">
            <el-button type="success" icon="el-icon-check" circle @click="pass(scope.row.proId)" />
          </template>
        </el-table-column>
        <el-table-column label="项目结项书" width="100">
          <template slot-scope="scope">
            <el-button icon="el-icon-top" circle @click="openFileUI(scope.row.proId)" />
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 分页组件 -->
    <el-pagination :current-page="searchModel.pageNo" :page-sizes="[5, 10, 20, 50]" :page-size="searchModel.pageSize"
      layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
    <!-- 文件信息对话框 -->
    <el-dialog v-drag :title="fileTitle" :visible.sync="dialogFileFormVisible" :close-on-click-modal="false"
      @close="clearFileForm">
      <el-form ref="proFileFormRef" :model="proForm">
        <el-form-item label="项目名" :label-width="formLabelWidth" prop="proName">
          <el-input v-model="proForm.proName" :disabled="true" autocomplete="off" />
        </el-form-item>
        <el-form-item label="项目结项书" :label-width="formLabelWidth" prop="proEndfileUrl">
          <el-input v-model="proForm.proEndfileUrl" style="width:440px" autocomplete="off" />
            <el-upload :action="'http://' + serverIp + ':9999/files/upload'" :show-file-list="false"
            :on-success="handleFileUploadProEndfileSuccess" style="display:inline">
            <el-button type="primary" style="margin-left: 10px;">上传<i class="el-icon-top" /></el-button>
          </el-upload>
          <el-button :style="{ display: proForm.proEndfileUrl == null ? 'none' : '' }" type="primary"
            style="margin-left: 10px;" @click="download(proForm.proEndfileUrl)">下载<i
              class="el-icon-download" /></el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFileFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveProFile">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import schoolApi from '@/api/schoolManage'
import proApi from '@/api/proManage'
import resTypeApi from '@/api/resultsTypeManage'
import statusApi from '@/api/statusManage'
import userApi from '@/api/userManage'
import { getPersonInfo } from '@/api/user'
import store from '@/store'
import { serverIp } from '@/serverConfig'
export default {
  data() {
    return {
      serverIp: serverIp,
      fileTitle: '项目结项书',
      dialogFileFormVisible: false,
      formLabelWidth: '100px',
      total: 0,
      searchModel: {
        pageNo: 1,
        pageSize: 10
      },
      proList: [],
      schoolList: [],
      resultsTypeList: [],
      statusList: [],
      userList: [],
      proForm: {}
    }
  },
  created() { // 构造函数
    this.searchModel.proStatusId = 6 // 中期检查通过
    this.getUserInfo()
    this.getAllSchoolList()
    this.getAllResultsTypeList()
    this.getAllProStatusList()
    this.getAllUserList()
  },
  methods: {
    getUserInfo() {
      const token = store.getters.token
      getPersonInfo(token).then(response => {
        this.searchModel.proChargeId = response.data.userId
        this.getSciProList()
      })
    },
    handleSizeChange(pageSize) {
      this.searchModel.pageSize = pageSize
      this.getSciProList()
    },
    handleCurrentChange(pageNo) {
      this.searchModel.pageNo = pageNo
      this.getSciProList()
    },
    getSciProList() {
      proApi.getSciProList(this.searchModel).then(response => { // then是回调函数
        this.proList = response.data.rows
        this.total = response.data.total
      })
    },
    openFileUI(id) {
      // 根据ID查询项目
      proApi.getProById(id).then(response => {
        this.proForm = response.data
      })
      this.dialogFileFormVisible = true
    },
    clearFileForm() {
      this.proForm = {}
    },
    saveProFile() {
      // 提交给后台
      this.proForm.updateTimeString = this.getCurrentDateTime() // 修改时间
      proApi.savePro(this.proForm).then(response => {
        // 成功提示
        this.$message({
          message: response.message,
          type: 'success'
        })
        // 关闭对话框
        this.dialogFileFormVisible = false
        // 刷新表格
        this.getSciProList()
      })
    },
    pass(id) {
      this.proForm.proId = id
      this.proForm.proStatusId = 7 // 结题申报中
      this.proForm.submitTimeString = this.getCurrentDateTime() // 提交时间
      proApi.updatePro(this.proForm)
      // // 成功提示
      this.$message({
        message: '成功申报',
        type: 'success'
      })
      this.getSciProList()
      this.getSciProList()
    },
    getAllSchoolList() {
      schoolApi.getAllSchoolList().then(response => {
        this.schoolList = response.data
      })
    },
    getAllResultsTypeList() {
      resTypeApi.getAllResultsTypeList().then(response => {
        this.resultsTypeList = response.data
      })
    },
    getAllProStatusList() {
      statusApi.getAllProStatusList().then(response => {
        this.statusList = response.data
      })
    },
    getAllUserList() {
      userApi.getAllUserList().then(response => {
        this.userList = response.data
      })
    },
    download(url) {
      window.open(url)
      this.$message({
        type: 'success',
        message: '下载成功'
      })
    },
    handleFileUploadProEndfileSuccess(res) {
      this.proForm.proEndfileUrl = res
      this.$message({
        type: 'success',
        message: '上传成功'
      })
      this.dialogFileFormVisible = false
      this.dialogFileFormVisible = true
    },
    getCurrentDateTime() { // 获取当前时间
      const today = new Date()
      const year = today.getFullYear()
      const month = ('0' + (today.getMonth() + 1)).slice(-2)
      const day = ('0' + today.getDate()).slice(-2)
      const hours = ('0' + today.getHours()).slice(-2)
      const minutes = ('0' + today.getMinutes()).slice(-2)
      const seconds = ('0' + today.getSeconds()).slice(-2)
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    }
  }
}
</script>

<style>
#search .el-input {
  width: 130px;
  margin-right: 10px;
}

.el-dialog .el-input {
  width: '%90'
}
</style>
