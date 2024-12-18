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
          <el-select v-model="searchModel.proChargeId" placeholder="请选择负责人" filterable clearable>
            <el-option v-for="item in userList" :key="item.userId" :label="item.username" :value="item.userId" />
          </el-select>
          <el-input v-model="searchModel.proSubject" placeholder="项目学科" clearable />
          <el-select v-model="searchModel.resultsTypeId" placeholder="请选择成果类别" filterable clearable>
            <el-option v-for="item in resultsTypeList" :key="item.resultsTypeId" :label="item.resultsTypeDesc"
              :value="item.resultsTypeId" />
          </el-select>
          <el-button type="primary" icon="el-icon-search" round @click="getProAuditList">查询</el-button>
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
        <el-table-column prop="proChargeName" label="负责人" width="90" />
        <el-table-column prop="schoolDesc" label="项目所属" width="120" />
        <el-table-column prop="proSubject" label="项目学科" width="120" />
        <el-table-column prop="resultsTypeDesc" label="成果类别" width="115" />
        <el-table-column prop="statusDesc" label="项目状态" width="150">
            <template slot-scope="scope">
              <el-tag>{{ scope.row.statusDesc }}</el-tag>
            </template>
          </el-table-column>
        <el-table-column prop="budget" label="项目预算" width="110" />
        <el-table-column prop="submitTime" label="提交时间" width="100" />
        <el-table-column label="查看" width="80">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-search" circle @click="openEditUI(scope.row.proId)" />
          </template>
        </el-table-column>
        <el-table-column label="通过" width="80">
          <template slot-scope="scope">
            <el-button type="success" icon="el-icon-check" circle @click="pass(scope.row.proId)" />
          </template>
        </el-table-column>
        <el-table-column label="驳回" width="80">
          <template slot-scope="scope">
            <el-button type="danger" icon="el-icon-close" circle @click="reject(scope.row.proId)" />
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
      <el-form ref="proFormRef" :model="proForm">
        <el-row>
          <el-col :span="14">
            <el-form-item label="项目名" :label-width="formLabelWidth" prop="proName">
              <el-input v-model="proForm.proName" autocomplete="off" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="项目负责人" :label-width="formLabelWidth">
              <el-select v-model="proForm.proChargeId" placeholder="请选择" filterable clearable>
                <el-option v-for="item in userList" :key="item.userId" :label="item.username" :value="item.userId" />
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
            <el-form-item label="项目所属" :label-width="formLabelWidth">
              <el-select v-model="proForm.proSchoolId" placeholder="请选择" filterable clearable>
                <el-option v-for="item in schoolList" :key="item.schoolId" :label="item.schoolDesc"
                  :value="item.schoolId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="14">
            <el-form-item label="成果类别" :label-width="formLabelWidth">
              <el-select v-model="proForm.resultsTypeId" placeholder="请选择" filterable clearable>
                <el-option v-for="item in resultsTypeList" :key="item.resultsTypeId" :label="item.resultsTypeDesc"
                  :value="item.resultsTypeId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="项目状态" :label-width="formLabelWidth">
              <el-select v-model="proForm.proStatusId" placeholder="请选择" filterable clearable>
                <el-option v-for="item in statusList" :key="item.statusId" :label="item.statusDesc"
                  :value="item.statusId" />
              </el-select>
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
            <el-form-item label="项目预算" :label-width="formLabelWidth" prop="budget">
              <el-input v-model="proForm.budget" autocomplete="off" />
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
          <el-input v-model="proForm.proProposalUrl" style="width:440px" autocomplete="off" />
          <el-button :style="{ display: proForm.proProposalUrl == null ? 'none' : '' }" type="primary"
            style="margin-left: 10px;" @click="download(proForm.proProposalUrl)">下载<i
              class="el-icon-download" /></el-button>
        </el-form-item>
        <el-form-item label="中期检查报告" :label-width="formLabelWidth" prop="interInspectUrl">
          <el-input v-model="proForm.interInspectUrl" style="width:440px" autocomplete="off" />
          <el-button :style="{ display: proForm.interInspectUrl == null ? 'none' : '' }" type="primary"
            style="margin-left: 10px;" @click="download(proForm.interInspectUrl)">下载<i
              class="el-icon-download" /></el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
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
export default {
  data() {
    return {
      formLabelWidth: '100px',
      proForm: {},
      dialogFormVisible: false, // 控制对话框默认不可见，当点击新增按钮后改为可见
      title: '',
      total: 0,
      searchModel: {
        pageNo: 1,
        pageSize: 10
      },
      proList: [],
      schoolList: [],
      resultsTypeList: [],
      statusList: [],
      userList: []
    }
  },
  created() { // 构造函数
    this.searchModel.proStatusId = 5 // 中期检查申报中
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
        this.searchModel.proSchoolId = response.data.userSchoolId
        this.getProAuditList()
      })
    },
    handleSizeChange(pageSize) {
      this.searchModel.pageSize = pageSize
      this.getProAuditList()
    },
    handleCurrentChange(pageNo) {
      this.searchModel.pageNo = pageNo
      this.getProAuditList()
    },
    getProAuditList() {
      proApi.getProAuditList(this.searchModel).then(response => { // then是回调函数
        this.proList = response.data.rows
        this.total = response.data.total
      })
    },
    openEditUI(id) {
      this.title = '查看项目'
      // 根据ID查询项目
      proApi.getProById(id).then(response => {
        this.proForm = response.data
      })
      this.dialogFormVisible = true
    },
    clearForm() {
      this.proForm = {}
    },
    pass(id) {
      this.proForm.proId = id
      this.proForm.proStatusId = 6 // 中期检查通过
      this.proForm.submitTimeString = '' // 提交时间置空
      proApi.updatePro(this.proForm)
      // // 成功提示
      this.$message({
        message: '成功通过',
        type: 'success'
      })
      this.getProAuditList()
      this.getProAuditList()
    },
    reject(id) {
      this.proForm.proId = id
      this.proForm.proStatusId = 4 // 校级申报通过
      this.proForm.submitTimeString = '' // 提交时间置空
      proApi.updatePro(this.proForm)
      // 成功提示
      this.$message({
        message: '成功驳回',
        type: 'success'
      })
      this.getProAuditList()
      this.getProAuditList()
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
