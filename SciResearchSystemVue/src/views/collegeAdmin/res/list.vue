<!-- eslint-disable vue/html-closing-bracket-newline -->
<!-- eslint-disable vue/html-indent -->
<!-- eslint-disable vue/max-attributes-per-line -->
<template>
  <div>
    <!--搜索框-->
    <el-card id="search">
      <el-row>
        <el-col :span="22">
          <el-input v-model="searchModel.resultsName" placeholder="成果名" clearable />
          <el-select v-model="searchModel.resultsChargeId" placeholder="请选择负责人" filterable clearable>
            <el-option v-for="item in userList" :key="item.userId" :label="item.username" :value="item.userId" />
          </el-select>
          <el-select v-model="searchModel.resultsSchoolId" placeholder="请选择成果所属学院" filterable clearable>
            <el-option v-for="item in schoolList" :key="item.schoolId" :label="item.schoolDesc" :value="item.schoolId" />
          </el-select>
          <el-input v-model="searchModel.resultsSubject" placeholder="成果学科" clearable />
          <el-select v-model="searchModel.typeId" placeholder="请选择成果类别" filterable clearable>
            <el-option v-for="item in resultsTypeList" :key="item.resultsTypeId" :label="item.resultsTypeDesc"
              :value="item.resultsTypeId" />
          </el-select>
          <el-select v-model="searchModel.resultsStatusId" placeholder="请选择成果状态" filterable clearable>
            <el-option v-for="item in statusList" :key="item.statusId" :label="item.statusDesc" :value="item.statusId" />
          </el-select>
          <el-button type="primary" icon="el-icon-search" round @click="getResultsList">查询</el-button>
        </el-col>
        <el-col :span="2" align="right">
          <el-button type="primary" icon="el-icon-download" round @click="getExcel">导出</el-button></el-col>
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
        <el-table-column prop="resultsName" label="成果名" width="220" align="center" />
        <el-table-column prop="resultsChargeName" label="负责人" width="120" />
        <el-table-column prop="schoolDesc" label="成果所属" width="140" />
        <el-table-column prop="resultsSubject" label="成果学科" width="160" />
        <el-table-column prop="typeDesc" label="成果类别" width="130" />
        <el-table-column prop="statusDesc" label="成果状态" width="180">
            <template slot-scope="scope">
              <el-tag>{{ scope.row.statusDesc }}</el-tag>
            </template>
          </el-table-column>
        <el-table-column prop="updateTime" label="修改时间" width="150" />
        <el-table-column label="查看" width="90">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-search" circle @click="openEditUI(scope.row.resultsId)" />
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
      <el-form ref="resultsFormRef" :model="resultsForm">
        <el-row>
          <el-col :span="14">
            <el-form-item label="成果名" :label-width="formLabelWidth" prop="resultsName">
              <el-input v-model="resultsForm.resultsName" autocomplete="off" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="成果负责人" :label-width="formLabelWidth">
              <el-select v-model="resultsForm.resultsChargeId" placeholder="请选择" filterable>
                <el-option v-for="item in userList" :key="item.userId" :label="item.username" :value="item.userId" />
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
            <el-form-item label="成果所属" :label-width="formLabelWidth">
              <el-select v-model="resultsForm.resultsSchoolId" placeholder="请选择" filterable>
                <el-option v-for="item in schoolList" :key="item.schoolId" :label="item.schoolDesc"
                  :value="item.schoolId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="14">
            <el-form-item label="成果类别" :label-width="formLabelWidth">
              <el-select v-model="resultsForm.typeId" placeholder="请选择" filterable>
                <el-option v-for="item in resultsTypeList" :key="item.resultsTypeId" :label="item.resultsTypeDesc"
                  :value="item.resultsTypeId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="成果状态" :label-width="formLabelWidth">
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
          <el-button :style="{ display: resultsForm.resultsUrl == null ? 'none' : '' }" type="primary"
            style="margin-left: 10px;" @click="download(resultsForm.resultsUrl)">下载<i
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
import resultsApi from '@/api/resultsManage'
import resTypeApi from '@/api/resultsTypeManage'
import statusApi from '@/api/statusManage'
import userApi from '@/api/userManage'
export default {
  data() {
    return {
      formLabelWidth: '100px',
      resultsForm: {},
      dialogFormVisible: false, // 控制对话框默认不可见，当点击新增按钮后改为可见
      title: '',
      total: 0,
      searchModel: {
        pageNo: 1,
        pageSize: 10
      },
      resultsList: [],
      schoolList: [],
      resultsTypeList: [],
      statusList: [],
      userList: []
    }
  },
  created() { // 构造函数
    this.getResultsList()
    this.getAllSchoolExceptCollege()
    this.getAllResultsTypeList()
    this.getResStatusExceptDraft()
    this.getAllSciUserList()
  },
  methods: {
    handleSizeChange(pageSize) {
      this.searchModel.pageSize = pageSize
      this.getResultsList()
    },
    handleCurrentChange(pageNo) {
      this.searchModel.pageNo = pageNo
      this.getResultsList()
    },
    getResultsList() {
      resultsApi.getResultsList(this.searchModel).then(response => { // then是回调函数
        this.resultsList = response.data.rows
        this.total = response.data.total
      })
    },
    openEditUI(id) {
      this.title = '查看成果'
      // 根据ID查询成果
      resultsApi.getResultsById(id).then(response => {
        this.resultsForm = response.data
      })
      this.dialogFormVisible = true
    },
    clearForm() {
      this.resultsForm = {}
    },
    getAllSchoolExceptCollege() {
      schoolApi.getAllSchoolExceptCollege().then(response => {
        this.schoolList = response.data
      })
    },
    getAllResultsTypeList() {
      resTypeApi.getAllResultsTypeList().then(response => {
        this.resultsTypeList = response.data
      })
    },
    getResStatusExceptDraft() {
      statusApi.getResStatusExceptDraft().then(response => {
        this.statusList = response.data
      })
    },
    getAllSciUserList() {
      userApi.getAllSciUserList().then(response => {
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
    async getExcel() {
      try {
        const excelModel = {
          resultsChargeId: '',
          resultsSchoolId: '',
          resultsStatusId: '10'
        }
        const response = await resultsApi.getExcelFile(excelModel)
        const url = window.URL.createObjectURL(new Blob([response.data]))
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', '成果表.xlsx')
        document.body.appendChild(link)
        link.click()
        link.remove()
        this.$message({
          type: 'success',
          message: '导出成功'
        })
      } catch (error) {
        console.error('导出失败', error)
      }
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
