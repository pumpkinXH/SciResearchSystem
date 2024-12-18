<!-- eslint-disable vue/html-closing-bracket-newline -->
<!-- eslint-disable vue/html-indent -->
<!-- eslint-disable vue/max-attributes-per-line -->
<template>
  <div>
    <!--搜索框-->
    <el-card id="search">
      <el-row>
        <el-col :span="20">
          <el-input v-model="searchModel.username" placeholder="用户名" clearable />
          <el-select v-model="searchModel.userRoleId" placeholder="请选择用户角色" filterable clearable>
            <el-option v-for="item in roleList" :key="item.roleId" :label="item.roleDesc" :value="item.roleId" />
          </el-select>
          <el-select v-model="searchModel.userSchoolId" placeholder="请选择用户所属部门" filterable clearable>
            <el-option v-for="item in schoolList" :key="item.schoolId" :label="item.schoolDesc" :value="item.schoolId" />
          </el-select>
          <el-button type="primary" icon="el-icon-search" round @click="getCardrList">查询</el-button>
        </el-col>
      </el-row>
    </el-card>
    <el-card>
      <div>当前最新已分配卡号：{{ newCard }}</div>
    </el-card>
    <!--结果列表-->
    <el-card>
      <el-table :data="userList" stripe style="width: 100%">
        <el-table-column label="序号" width="80">
          <template slot-scope="scope">
            <!--(pageNo-1) * pageSize + index + 1-->
            {{ (searchModel.pageNo - 1) * searchModel.pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <!-- <el-table-column prop="card" label="卡号" width="100" /> -->
        <el-table-column prop="username" label="用户名" width="140" />
        <el-table-column prop="userPhone" label="电话" width="220" />
        <el-table-column prop="userEmail" label="电子邮件" width="220" />
        <!-- <el-table-column prop="userStatus" label="用户状态" width="130">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.userStatus === 1">正常</el-tag>
                    <el-tag v-else type="danger">禁用</el-tag>
                  </template>
                </el-table-column> -->
        <el-table-column prop="roleDesc" label="用户角色" width="200" />
        <el-table-column prop="schoolDesc" label="所属部门" width="200" />
        <el-table-column label="卡号分配" width="180">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-thumb" circle @click="openEditUI(scope.row.userId)" />
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 分页组件 -->
    <el-pagination :current-page="searchModel.pageNo" :page-sizes="[5, 10, 20, 50]" :page-size="searchModel.pageSize"
      layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
    <!-- 用户信息编辑对话框 -->
    <el-dialog v-drag :title="title" :visible.sync="dialogFormVisible" :close-on-click-modal="false" @close="clearForm">
      <el-form ref="userFormRef" :model="userForm" :rules="rules">
        <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
          <el-input v-model="userForm.username" :disabled="true" autocomplete="new-password" />
        </el-form-item>
        <el-form-item label="卡号" :label-width="formLabelWidth" prop="card">
          <el-input v-model="userForm.card" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveUser">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import userApi from '@/api/userManage' // @代表src目录
import roleApi from '@/api/roleManage'
import schoolApi from '@/api/schoolManage'
export default {
  data() {
    // 检查卡号是否合规
    var checkCard = (rule, value, callback) => {
      var regNum = /^\+?[0-9][0-9]*$/ // 整数（可以以0打头）
      if (!regNum.test(value)) {
        return callback(new Error('请输入整数'))
      }
      if (value.length < 3 || value.length > 10) {
        return callback(new Error('请输入3~10位整数'))
      }
      callback()
    }
    return {
      formLabelWidth: '130px',
      newCard: '',
      userForm: {
      },
      dialogFormVisible: false, // 控制对话框默认不可见，当点击新增按钮后改为可见
      title: '',
      total: 0,
      searchModel: {
        pageNo: 1,
        pageSize: 10
      },
      userList: [],
      rules: {
        card: [
          { required: true, message: '请输入卡号', trigger: 'blur' },
          { validator: checkCard, trigger: 'blur' }
        ]
      },
      roleList: [],
      schoolList: []
    }
  },
  created() { // 构造函数
    this.getCardrList()
    this.getAllRoleList()
    this.getAllSchoolList()
  },
  methods: {
    handleSizeChange(pageSize) {
      this.searchModel.pageSize = pageSize
      this.getCardrList()
    },
    handleCurrentChange(pageNo) {
      this.searchModel.pageNo = pageNo
      this.getCardrList()
    },
    getCardrList() {
      userApi.getCardrList(this.searchModel).then(response => { // then是回调函数
        this.userList = response.data.rows
        this.total = response.data.total
        this.getNewCard()
      })
    },
    openEditUI(id) {
      this.title = '卡号分配'
      // 根据ID查询用户数据
      userApi.getUserById(id).then(response => {
        this.userForm = response.data
      })
      this.dialogFormVisible = true
    },
    clearForm() {
      this.userForm = {
        roleIdList: []
      }
      this.$refs.userFormRef.clearValidate() // 清除表单验证信息
    },
    saveUser() {
      this.$confirm(`您确认为该用户分配该卡号吗？该操作不可逆！`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { // 确定
        // 先触发表单验证
        this.$refs.userFormRef.validate((valid) => {
          if (valid) {
            // 提交给后台
            userApi.saveUser(this.userForm).then(response => {
              // 成功提示
              this.$message({
                message: response.message,
                type: 'success'
              })
              // 关闭对话框
              this.dialogFormVisible = false
              // 刷新表格
              this.getCardrList()
            })
          } else {
            console.log('提交信息有错误！')
            return false
          }
        })
      }).catch(() => { // 取消
        this.$message({
          type: 'info',
          message: '已取消分配'
        })
      })
    },
    getAllRoleList() {
      roleApi.getAllRoleList().then(response => {
        this.roleList = response.data
      })
    },
    getAllSchoolList() {
      schoolApi.getAllSchoolList().then(response => {
        this.schoolList = response.data
      })
    },
    getNewCard() {
      userApi.getNewCard().then(response => {
        this.newCard = response.data
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
  width: '%90'
}
</style>
