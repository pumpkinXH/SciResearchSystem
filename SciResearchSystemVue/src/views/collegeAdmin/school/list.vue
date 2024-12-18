<!-- eslint-disable vue/html-closing-bracket-newline -->
<!-- eslint-disable vue/html-indent -->
<!-- eslint-disable vue/max-attributes-per-line -->
<template>
  <div>
    <!--搜索框-->
    <el-card id="search">
      <el-row>
        <el-col :span="20">
          <el-input v-model="searchModel.schoolName" placeholder="学院英文名" clearable />
          <el-input v-model="searchModel.schoolDesc" placeholder="学院中文名" clearable />
          <el-button type="primary" icon="el-icon-search" round @click="getSchoolList">查询</el-button>
        </el-col>
        <el-col :span="4" align="right">
          <el-button type="primary" icon="el-icon-plus" circle @click="openEditUI(null)" />
        </el-col>
      </el-row>
    </el-card>
    <!--结果列表-->
    <el-card>
      <el-table :data="schoolList" stripe style="width: 100%">
        <el-table-column label="序号" width="200">
          <template slot-scope="scope">
            <!--(pageNo-1) * pageSize + index + 1-->
            {{
              (searchModel.pageNo - 1) * searchModel.pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>
        <el-table-column prop="schoolName" label="学院英文名" width="400" />
        <el-table-column prop="schoolDesc" label="学院中文名" width="430" />
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" circle @click="openEditUI(scope.row.schoolId)" />
            <el-button type="danger" icon="el-icon-delete" circle @click="deleteSchool(scope.row)" />
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 分页组件 -->
    <el-pagination :current-page="searchModel.pageNo" :page-sizes="[5, 10, 20, 50]" :page-size="searchModel.pageSize"
      layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
    <!-- 学院信息编辑对话框 -->
    <el-dialog v-drag :title="title" :visible.sync="dialogFormVisible" :close-on-click-modal="false" @close="clearForm">
      <el-form ref="schoolFormRef" :model="schoolForm" :rules="rules">
        <el-form-item label="学院英文名" :label-width="formLabelWidth" prop="schoolName">
          <el-input v-model="schoolForm.schoolName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="学院中文名" :label-width="formLabelWidth" prop="schoolDesc">
          <el-input v-model="schoolForm.schoolDesc" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveSchool">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import schoolApi from '@/api/schoolManage' // @代表src目录
export default {
  data() {
    return {
      formLabelWidth: '130px',
      schoolForm: {},
      dialogFormVisible: false, // 控制对话框默认不可见，当点击新增按钮后改为可见
      title: '',
      total: 0,
      searchModel: {
        pageNo: 1,
        pageSize: 10
      },
      rules: {
        schoolDesc: [
          { required: true, message: '请输入学院中文名', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ]
      },
      schoolList: []
    }
  },
  created() {
    // 构造函数
    this.getSchoolList()
  },
  methods: {
    handleSizeChange(pageSize) {
      this.searchModel.pageSize = pageSize
      this.getSchoolList()
    },
    handleCurrentChange(pageNo) {
      this.searchModel.pageNo = pageNo
      this.getSchoolList()
    },
    getSchoolList() {
      schoolApi.getSchoolList(this.searchModel).then((response) => {
        // then是回调函数
        this.schoolList = response.data.rows
        this.total = response.data.total
      })
    },
    openEditUI(id) {
      if (id == null) {
        this.title = '新增学院'
      } else {
        this.title = '修改学院'
        // 根据ID查询学院数据
        schoolApi.getSchoolById(id).then((response) => {
          this.schoolForm = response.data
        })
      }
      this.dialogFormVisible = true
    },
    clearForm() {
      this.schoolForm = {}
      this.$refs.schoolFormRef.clearValidate() // 清除表单验证信息
    },
    saveSchool() {
      // 先触发表单验证
      this.$refs.schoolFormRef.validate((valid) => {
        if (valid) {
          // 提交给后台
          schoolApi.saveSchool(this.schoolForm).then((response) => {
            // 成功提示
            this.$message({
              message: response.message,
              type: 'success'
            })
            // 关闭对话框
            this.dialogFormVisible = false
            // 刷新表格
            this.getSchoolList()
          })
        } else {
          console.log('提交信息有错误！')
          return false
        }
      })
    },
    deleteSchool(school) {
      this.$confirm(`您确认删除${school.schoolDesc}吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 确定
        schoolApi.deleteSchoolById(school.schoolId).then((response) => {
          this.$message({
            type: 'success',
            message: response.message
          })
          this.getSchoolList()
        })
      }).catch(() => {
        // 取消
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    }
  }
}
</script>

<style>
#search .el-input {
  width: 200px;
  margin-right: 10px;
}

.el-dialog .el-input {
  width: "%90";
}
</style>
