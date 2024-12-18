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
        <el-col :span="4" align="right">
          <el-button type="primary" icon="el-icon-plus" circle @click="openEditUI(null)" />
        </el-col>
      </el-row>
    </el-card>
    <!--结果列表-->
    <el-card>
      <el-table :data="proList" stripe style="width: 100%">
        <el-table-column label="序号" width="80">
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
        <el-table-column prop="statusDesc" label="项目状态" width="100">
            <template slot-scope="scope">
              <el-tag>{{ scope.row.statusDesc }}</el-tag>
            </template>
          </el-table-column>
        <el-table-column prop="members" label="项目成员" width="120" />
        <el-table-column prop="budget" label="项目预算" width="100" />
        <el-table-column prop="updateTime" label="修改时间" width="100" />
        <el-table-column label="申报" width="80">
          <template slot-scope="scope">
            <el-button type="success" icon="el-icon-check" circle size="mini" @click="pass(scope.row.proId)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" circle size="mini" @click="openEditUI(scope.row.proId)" />
            <el-button type="danger" icon="el-icon-delete" circle size="mini" @click="deletePro(scope.row)" />
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 分页组件 -->
    <el-pagination :current-page="searchModel.pageNo" :page-sizes="[5, 10, 20, 50]" :page-size="searchModel.pageSize"
      layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
    <!-- 项目信息编辑对话框 -->
    <el-dialog :title="title" :visible.sync="dialogFormVisible" :close-on-click-modal="false" @close="clearForm">
      <el-form ref="proFormRef" :model="proForm" :rules="rules">
        <el-row>
          <el-col :span="14">
            <el-form-item label="项目名" :label-width="formLabelWidth" prop="proName">
              <el-input v-model="proForm.proName" autocomplete="off" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="成果类别" :label-width="formLabelWidth" prop="resultsTypeId">
              <el-select v-model="proForm.resultsTypeId" placeholder="请选择" filterable>
                <el-option v-for="item in resultsTypeList" :key="item.resultsTypeId" :label="item.resultsTypeDesc"
                  :value="item.resultsTypeId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="14">
            <el-form-item label="项目学科" :label-width="formLabelWidth" prop="proSubject">
              <el-input v-model="proForm.proSubject" autocomplete="off" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="项目预算" :label-width="formLabelWidth" prop="budget">
              <el-input v-model="proForm.budget" autocomplete="off" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="14">
            <el-form-item label="项目成员" :label-width="formLabelWidth" prop="members">
              <el-input v-model="proForm.members" autocomplete="off" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item :style="{ display: proForm.proStatusId == null || proForm.proStatusId == 1 ? 'none' : '' }" label="项目状态"
              :label-width="formLabelWidth">
              <el-select v-model="proForm.proStatusId" placeholder="请选择" filterable clearable>
                <el-option v-for="item in statusList" :key="item.statusId" :label="item.statusDesc"
                  :value="item.statusId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="预期目标" :label-width="formLabelWidth" prop="purpose">
          <el-input v-model="proForm.purpose" type="textarea" :rows="2" autocomplete="off" />
        </el-form-item>
        <el-form-item label="预期结果" :label-width="formLabelWidth" prop="expectedResult">
          <el-input v-model="proForm.expectedResult" type="textarea" :rows="2" autocomplete="off" />
        </el-form-item>
        <el-form-item label="方案分析" :label-width="formLabelWidth" prop="schemeAnalysis">
          <el-input v-model="proForm.schemeAnalysis" type="textarea" :rows="3" autocomplete="off" />
        </el-form-item>
        <el-form-item label="项目申报书" :label-width="formLabelWidth" prop="proProposalUrl">
          <el-input v-model="proForm.proProposalUrl" style="width:430px" autocomplete="off" />
          <el-upload :action="'http://' + serverIp + ':9999/files/upload'" :show-file-list="false"
            :on-success="handleFileUploadSuccess" style="display:inline">
            <el-button :style="{ display: proForm.proStatusId > 1 ? 'none' : '' }" type="primary"
              style="margin-left: 10px;">上传<i class="el-icon-top" /></el-button>
          </el-upload>
          <el-button :style="{ display: proForm.proProposalUrl == null ? 'none' : '' }" type="primary"
            style="margin-left: 10px;" @click="download(proForm.proProposalUrl)">下载<i
              class="el-icon-download" /></el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary"
          :style="{ display: proForm.proStatusId == null || proForm.proStatusId == 1 ? '' : 'none' }"
          @click="savePro">确定</el-button>
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
    var isNumber = (rule, value, callback) => { // 检查是否为数字
      var regPos = /^\d+(\.\d+)?$/ // 非负浮点数
      var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/ // 负浮点数
      if (regPos.test(value) || regNeg.test(value)) {
        callback()
      } else {
        callback(new Error('请输入数字'))
      }
    }
    return {
      serverIp: serverIp,
      formLabelWidth: '100px',
      dialogFormVisible: false,
      title: '',
      total: 0,
      searchModel: {
        pageNo: 1,
        pageSize: 10
      },
      rules: {
        proName: [
          { required: true, message: '请输入项目名', trigger: 'blur' },
          { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
        ],
        proSubject: [
          { required: true, message: '请输入项目学科', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        budget: [
          { required: true, message: '请输入项目预算', trigger: 'blur' },
          { validator: isNumber, trigger: 'blur' }
        ],
        resultsTypeId: [
          { required: true, message: '请选择一个', trigger: 'blur' }
        ],
        proSchoolId: [
          { required: true, message: '请选择一个', trigger: 'blur' }
        ]
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
    this.searchModel.proStatusId = 1 // 草稿
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
    openEditUI(id) {
      if (id == null) {
        this.title = '新增项目'
      } else {
        this.title = '修改项目'
        // 根据ID查询项目
        proApi.getProById(id).then(response => {
          this.proForm = response.data
        })
      }
      this.dialogFormVisible = true
    },
    clearForm() {
      this.proForm = {}
      this.$refs.proFormRef.clearValidate() // 清除表单验证信息
    },
    savePro() {
      // 先触发表单验证
      this.$refs.proFormRef.validate((valid) => {
        if (valid) {
          // 提交给后台
          this.proForm.proChargeId = this.searchModel.proChargeId
          this.proForm.proSchoolId = this.searchModel.proSchoolId
          this.proForm.proStatusId = 1
          this.proForm.updateTimeString = this.getCurrentDateTime() // 修改时间
          proApi.savePro(this.proForm).then(response => {
            // 成功提示
            this.$message({
              message: response.message,
              type: 'success'
            })
            // 关闭对话框
            this.dialogFormVisible = false
            // 刷新表格
            this.getSciProList()
          })
        } else {
          console.log('提交信息有错误！')
          return false
        }
      })
    },
    deletePro(pro) {
      this.$confirm(`您确认删除${pro.proName}项目吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { // 确定
        proApi.deleteProById(pro.proId).then(response => {
          this.$message({
            type: 'success',
            message: response.message
          })
          this.getSciProList()
        })
      }).catch(() => { // 取消
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    pass(id) {
      this.proForm.proId = id
      this.proForm.proStatusId = 2 // 申报中
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
    handleFileUploadSuccess(res) {
      this.proForm.proProposalUrl = res
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
  width: 130px;
  margin-right: 10px;
}

.el-dialog .el-input {
  width: '%90'
}
</style>
