<!-- eslint-disable vue/html-closing-bracket-newline -->
<!-- eslint-disable vue/html-indent -->
<!-- eslint-disable vue/max-attributes-per-line -->
<template>
  <div>
    <!--搜索框-->
    <el-card id="search">
      <el-row>
        <el-col :span="20">
          <el-input v-model="searchModel.resultsName" placeholder="成果名" clearable />
          <el-input v-model="searchModel.resultsSubject" placeholder="成果学科" clearable />
          <el-select v-model="searchModel.typeId" placeholder="请选择成果类别" filterable clearable>
            <el-option v-for="item in resultsTypeList" :key="item.resultsTypeId" :label="item.resultsTypeDesc"
              :value="item.resultsTypeId" />
          </el-select>
          <el-button type="primary" icon="el-icon-search" round @click="getSciResultsList">查询</el-button>
        </el-col>
        <el-col :span="4" align="right">
          <el-button type="primary" icon="el-icon-plus" circle @click="openEditUI(null)" />
        </el-col>
      </el-row>
    </el-card>
    <!--结果列表-->
    <el-card>
      <el-table :data="resultsList" stripe style="width: 100%">
        <el-table-column label="序号" width="50">
          <template slot-scope="scope">
            <!--(pageNo-1) * pageSize + index + 1-->
            {{ (searchModel.pageNo - 1) * searchModel.pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="resultsName" label="成果名" width="250" align="center" />
        <el-table-column prop="resultsChargeName" label="负责人" width="120" />
        <el-table-column prop="schoolDesc" label="成果所属" width="150" />
        <el-table-column prop="resultsSubject" label="成果学科" width="180" />
        <el-table-column prop="typeDesc" label="成果类别" width="130" />
        <el-table-column prop="statusDesc" label="成果状态" width="180">
            <template slot-scope="scope">
              <el-tag>{{ scope.row.statusDesc }}</el-tag>
            </template>
          </el-table-column>
        <el-table-column label="提交" width="80">
          <template slot-scope="scope">
            <el-button type="success" icon="el-icon-check" circle size="mini" @click="pass(scope.row.resultsId)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" circle size="mini" @click="openEditUI(scope.row.resultsId)" />
            <el-button type="danger" icon="el-icon-delete" circle size="mini" @click="deleteResults(scope.row)" />
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 分页组件 -->
    <el-pagination :current-page="searchModel.pageNo" :page-sizes="[5, 10, 20, 50]" :page-size="searchModel.pageSize"
      layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
    <!-- 成果信息编辑对话框 -->
    <el-dialog v-drag :title="title" :visible.sync="dialogFormVisible" :close-on-click-modal="false" @close="clearForm">
      <el-form ref="resultsFormRef" :model="resultsForm" :rules="rules">
        <el-row>
          <el-col :span="14">
            <el-form-item label="成果名" :label-width="formLabelWidth" prop="resultsName">
              <el-input v-model="resultsForm.resultsName" autocomplete="off" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="成果类别" :label-width="formLabelWidth" prop="typeId">
              <el-select v-model="resultsForm.typeId" placeholder="请选择" filterable>
                <el-option v-for="item in resultsTypeList" :key="item.resultsTypeId" :label="item.resultsTypeDesc"
                  :value="item.resultsTypeId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="14">
            <el-form-item label="成果学科" :label-width="formLabelWidth" prop="resultsSubject">
              <el-input v-model="resultsForm.resultsSubject" autocomplete="off" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item :style="{ display: resultsForm.resultsStatusId == null || resultsForm.resultsStatusId == 10 ? 'none' : '' }" label="成果状态"
              :label-width="formLabelWidth">
              <el-select v-model="resultsForm.resultsStatusId" placeholder="请选择" filterable>
                <el-option v-for="item in statusList" :key="item.statusId" :label="item.statusDesc"
                  :value="item.statusId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="成果描述" :label-width="formLabelWidth" prop="resultsDesc">
          <el-input v-model="resultsForm.resultsDesc" type="textarea" :rows="3" autocomplete="off" />
        </el-form-item>
        <el-form-item label="成果证明文件" :label-width="formLabelWidth" prop="resultsUrl">
          <el-input v-model="resultsForm.resultsUrl" style="width:440px" placeholder="如有多个文件，请以压缩包的格式上传"
            autocomplete="off" />
              <el-upload :action="'http://' + serverIp + ':9999/files/upload'" :show-file-list="false"
            :on-success="handleFileUploadSuccess" style="display:inline">
            <el-button :style="{ display: resultsForm.resultsStatusId > 10 ? 'none' : '' }" type="primary"
              style="margin-left: 10px;">上传<i class="el-icon-top" /></el-button>
          </el-upload>
          <el-button :style="{ display: resultsForm.resultsUrl == null ? 'none' : '' }" type="primary"
            style="margin-left: 10px;" @click="download(resultsForm.resultsUrl)">下载<i
              class="el-icon-download" /></el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary"
          :style="{ display: resultsForm.resultsStatusId == 10 || resultsForm.resultsStatusId == null ? '' : 'none' }"
          @click="saveResults">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import schoolApi from '@/api/schoolManage'
import resultsApi from '@/api/resultsManage'
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
      formLabelWidth: '100px',
      resultsForm: {},
      dialogFormVisible: false, // 控制对话框默认不可见，当点击新增按钮后改为可见
      title: '',
      total: 0,
      searchModel: {
        pageNo: 1,
        pageSize: 10
      },
      rules: {
        resultsName: [
          { required: true, message: '请输入成果名', trigger: 'blur' },
          { min: 3, max: 30, message: '长度在 3 到 30 个字符', trigger: 'blur' }
        ],
        resultsSubject: [
          { required: true, message: '请输入成果学科', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        typeId: [
          { required: true, message: '请选择一个', trigger: 'blur' }
        ]
      },
      resultsList: [],
      schoolList: [],
      resultsTypeList: [],
      statusList: [],
      userList: []
    }
  },
  created() { // 构造函数
    this.searchModel.resultsStatusId = 10 // 成果草稿
    this.getUserInfo()
    this.getAllSchoolList()
    this.getAllResultsTypeList()
    this.getAllResStatusList()
    this.getAllUserList()
  },
  methods: {
    getUserInfo() {
      const token = store.getters.token
      getPersonInfo(token).then(response => {
        this.searchModel.resultsChargeId = response.data.userId
        this.getSciResultsList()
      })
    },
    handleSizeChange(pageSize) {
      this.searchModel.pageSize = pageSize
      this.getSciResultsList()
    },
    handleCurrentChange(pageNo) {
      this.searchModel.pageNo = pageNo
      this.getSciResultsList()
    },
    getSciResultsList() {
      resultsApi.getSciResultsList(this.searchModel).then(response => { // then是回调函数
        this.resultsList = response.data.rows
        this.total = response.data.total
      })
    },
    openEditUI(id) {
      if (id == null) {
        this.title = '新增成果'
      } else {
        this.title = '修改成果'
        // 根据ID查询成果
        resultsApi.getResultsById(id).then(response => {
          this.resultsForm = response.data
        })
      }
      this.dialogFormVisible = true
    },
    clearForm() {
      this.resultsForm = {}
      this.$refs.resultsFormRef.clearValidate() // 清除表单验证信息
    },
    saveResults() {
      // 先触发表单验证
      this.$refs.resultsFormRef.validate((valid) => {
        if (valid) {
          // 提交给后台
          this.resultsForm.resultsChargeId = this.searchModel.resultsChargeId
          this.resultsForm.resultsSchoolId = this.searchModel.resultsSchoolId
          this.resultsForm.resultsStatusId = 10 // 成果草稿
          this.resultsForm.updateTimeString = this.getCurrentDateTime() // 修改时间
          resultsApi.saveResults(this.resultsForm).then(response => {
            // 成功提示
            this.$message({
              message: response.message,
              type: 'success'
            })
            // 关闭对话框
            this.dialogFormVisible = false
            // 刷新表格
            this.getSciResultsList()
          })
        } else {
          console.log('提交信息有错误！')
          return false
        }
      })
    },
    deleteResults(results) {
      this.$confirm(`您确认删除${results.resultsName}成果吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { // 确定
        resultsApi.deleteResultsById(results.resultsId).then(response => {
          this.$message({
            type: 'success',
            message: response.message
          })
          this.getSciResultsList()
        })
      }).catch(() => { // 取消
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    pass(id) {
      this.resultsForm.resultsId = id
      this.resultsForm.resultsStatusId = 11 // 成果鉴定中
      this.resultsForm.submitTimeString = this.getCurrentDateTime() // 提交时间
      resultsApi.updateResults(this.resultsForm)
      // // 成功提示
      this.$message({
        message: '提交成功',
        type: 'success'
      })
      this.getSciResultsList()
      this.getSciResultsList()
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
    getAllResStatusList() {
      statusApi.getAllResStatusList().then(response => {
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
    handleFileUploadSuccess(res) {
      this.resultsForm.resultsUrl = res
      this.$message({
        type: 'success',
        message: '上传成功'
      })
      this.dialogFormVisible = false
      this.dialogFormVisible = true
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
  width: 145px;
  margin-right: 10px;
}

.el-dialog .el-input {
  width: '%90'
}
</style>
