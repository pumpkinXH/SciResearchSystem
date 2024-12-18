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
          <el-input v-model="searchModel.card" placeholder="卡号" clearable />
          <el-select v-model="searchModel.userRoleId" placeholder="请选择用户角色" filterable clearable>
            <el-option v-for="item in roleList" :key="item.roleId" :label="item.roleDesc" :value="item.roleId" />
          </el-select>
          <el-select v-model="searchModel.userSchoolId" placeholder="请选择用户所属部门" filterable clearable>
            <el-option v-for="item in schoolList" :key="item.schoolId" :label="item.schoolDesc" :value="item.schoolId" />
          </el-select>
          <el-button type="primary" icon="el-icon-search" round @click="getUserList">查询</el-button>
        </el-col>
        <el-col :span="4" align="right">
          <el-button type="primary" icon="el-icon-plus" circle @click="openEditUI()" />
        </el-col>
      </el-row>
    </el-card>
    <!--结果列表-->
    <el-card>
      <el-table :data="userList" stripe style="width: 100%">
        <el-table-column label="序号" width="50">
          <template slot-scope="scope">
            <!--(pageNo-1) * pageSize + index + 1-->
            {{ (searchModel.pageNo - 1) * searchModel.pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="card" label="卡号" width="130" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.card === null">未分配</el-tag>
            <span v-else>{{ scope.row.card }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="userPhone" label="电话" width="200" />
        <el-table-column prop="userEmail" label="电子邮件" width="200" />
        <!-- <el-table-column prop="userStatus" label="用户状态" width="130">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.userStatus === 1">正常</el-tag>
                    <el-tag v-else type="danger">禁用</el-tag>
                  </template>
                </el-table-column> -->
        <el-table-column prop="roleDesc" label="用户角色" width="180" />
        <el-table-column prop="schoolDesc" label="所属部门" width="150" />
        <el-table-column label="角色修改" width="100">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-thumb" circle @click="openRoleUI(scope.row.userId)" />
          </template>
        </el-table-column>
        <el-table-column label="删除" width="100">
          <template slot-scope="scope">
            <el-button type="danger" icon="el-icon-delete" circle @click="deleteUser(scope.row)" />
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 分页组件 -->
    <el-pagination :current-page="searchModel.pageNo" :page-sizes="[5, 10, 20, 50]" :page-size="searchModel.pageSize"
      layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
    <!-- 用户信息增加对话框 -->
    <el-dialog v-drag :title="title" :visible.sync="dialogFormVisible" :close-on-click-modal="false" @close="clearForm">
      <el-form ref="userFormRef" :model="userForm" :rules="rules">
        <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
          <el-input v-model="userForm.username" autocomplete="new-password" />
        </el-form-item>
        <el-form-item v-if="userForm.userId == null || userForm.userId == undefined" label="密码"
          :label-width="formLabelWidth" prop="password">
          <el-input v-model="userForm.password" autocomplete="new-password" type="password" />
        </el-form-item>
        <!-- <el-form-item label="用户状态" :label-width="formLabelWidth">
                  <el-switch v-model="userForm.userStatus" :active-value="1" :inactive-value="0" />
                </el-form-item> -->
        <el-form-item label="用户角色" :label-width="formLabelWidth" prop="userRoleId">
          <el-select v-model="userForm.userRoleId" placeholder="请选择角色" filterable @change="onchange(userForm.userRoleId)">
            <el-option v-for="item in roleList" :key="item.roleId" :label="item.roleDesc" :value="item.roleId" />
          </el-select>
        </el-form-item>
        <el-form-item label="电话" :label-width="formLabelWidth" prop="userPhone">
          <el-input v-model="userForm.userPhone" autocomplete="off" />
        </el-form-item>
        <el-form-item label="电子邮件" :label-width="formLabelWidth" prop="userEmail">
          <el-input v-model="userForm.userEmail" autocomplete="off" />
        </el-form-item>
        <el-form-item label="所属" :label-width="formLabelWidth" prop="userSchoolId">
          <el-select v-model="userForm.userSchoolId" placeholder="请选择学院" filterable>
            <el-option v-for="item in schoolList" :key="item.schoolId" :label="item.schoolDesc" :value="item.schoolId" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveUser">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 角色修改对话框 -->
    <el-dialog v-drag :title="title" :visible.sync="dialogRoleVisible" :close-on-click-modal="false"
      @close="clearRoleForm">
      <el-form ref="userRoleRef" :model="userForm">
        <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
          <el-input v-model="userForm.username" :disabled="true" autocomplete="new-password" />
        </el-form-item>
        <el-form-item label="用户角色" :label-width="formLabelWidth">
          <el-select v-model="userForm.userRoleId" placeholder="请选择角色" filterable>
            <el-option v-for="item in roleList" :key="item.roleId" :label="item.roleDesc" :value="item.roleId" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogRoleVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveRole">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 管理员角色修改对话框 -->
    <el-dialog v-drag :title="title" :visible.sync="dialogRoleVisible2" :close-on-click-modal="false"
      @close="clearRoleForm">
      <el-form ref="userRoleRef2" :model="userForm">
        <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
          <el-input v-model="userForm.username" :disabled="true" autocomplete="new-password" />
        </el-form-item>
        <el-form-item label="用户角色" :label-width="formLabelWidth">
          <el-select v-model="userForm.userRoleId" placeholder="请选择角色" filterable @change="onchange(userForm.userRoleId)">
            <el-option v-for="item in roleList" :key="item.roleId" :label="item.roleDesc" :value="item.roleId" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属" :label-width="formLabelWidth">
          <el-select v-model="userForm.userSchoolId" placeholder="请选择学院" filterable>
            <el-option v-for="item in schoolList" :key="item.schoolId" :label="item.schoolDesc" :value="item.schoolId" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogRoleVisible2 = false">取 消</el-button>
        <el-button type="primary" @click="saveRole">确 定</el-button>
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
    // 检查email是否合规
    var checkEmail = (rule, value, callback) => {
      var reg = /^[a-zA-Z0-9]+([-_.][a-zA-Z0-9]+)*@[a-zA-Z0-9]+([-_.][a-zA-Z0-9]+)*\.[a-z]{2,}$/ // 检查Email合规的正则表达式
      if (!reg.test(value)) {
        return callback(new Error('邮箱格式错误'))
      }
      callback()
    }
    var isPhone = (rule, value, callback) => { // 检查是否为电话号码
      var regNum = /^\+?[0-9][0-9]*$/ // 正整数（可以以0打头）
      if (!regNum.test(value)) {
        return callback(new Error('请输入正整数'))
      }
      if (value.length !== 11) {
        return callback(new Error('请输入11位正整数'))
      }
      callback()
    }
    return {
      formLabelWidth: '130px',
      userForm: {
      },
      dialogFormVisible: false, // 控制对话框默认不可见，当点击新增按钮后改为可见
      dialogRoleVisible: false,
      dialogRoleVisible2: false,
      title: '',
      total: 0,
      searchModel: {
        pageNo: 1,
        pageSize: 10
      },
      userList: [],
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入登陆密码', trigger: 'blur' },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        userPhone: [
          { required: true, message: '请输入电话', trigger: 'blur' },
          { validator: isPhone, trigger: 'blur' }
        ],
        userEmail: [
          { required: true, message: '请输入电子邮件', trigger: 'blur' },
          { validator: checkEmail, trigger: 'blur' }
        ],
        userRoleId: [
          { required: true, message: '请选择一个值', trigger: 'blur' }
        ],
        userSchoolId: [
          { required: true, message: '请选择一个值', trigger: 'blur' }
        ]
      },
      roleList: [],
      schoolList: []
    }
  },
  created() { // 构造函数
    this.getUserList()
    this.getAllRoleExceptAdmin()
    this.getAllSchoolList()
  },
  methods: {
    onchange(roleId) {
      if (roleId === 2) {
        this.getCollege()
        this.userForm.userSchoolId = 1
      } else {
        this.getAllSchoolExceptCollege()
        this.userForm.userSchoolId = 2
      }
    },
    handleSizeChange(pageSize) {
      this.searchModel.pageSize = pageSize
      this.getUserList()
    },
    handleCurrentChange(pageNo) {
      this.searchModel.pageNo = pageNo
      this.getUserList()
    },
    getUserList() {
      userApi.getUserList(this.searchModel).then(response => { // then是回调函数
        this.userList = response.data.rows
        this.total = response.data.total
      })
    },
    openEditUI() {
      this.title = '新增用户'
      this.dialogFormVisible = true
    },
    openRoleUI(id) {
      this.title = '角色修改'
      // 根据ID查询用户数据
      userApi.getUserById(id).then(response => {
        this.userForm = response.data
        if (this.userForm.userRoleId === 2) {
          this.getCollege()
          this.userForm.userSchoolId = 1
          this.dialogRoleVisible2 = true
        } else {
          this.dialogRoleVisible = true
        }
      })
    },
    clearForm() {
      this.userForm = {}
      this.$refs.userFormRef.clearValidate() // 清除表单验证信息
      this.getAllSchoolList()
    },
    clearRoleForm() {
      this.userForm = {}
      this.getAllSchoolList()
      this.getAllRoleExceptAdmin()
    },
    saveRole() {
      if (this.userForm.userRoleId === 2) {
        this.userForm.userSchoolId = 1
      }
      userApi.saveUser(this.userForm).then(response => {
        // 成功提示
        this.$message({
          message: response.message,
          type: 'success'
        })
        // 关闭对话框
        this.dialogRoleVisible = false
        this.dialogRoleVisible2 = false
        // 刷新表格
        this.getUserList()
      })
    },
    saveUser() {
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
            this.getUserList()
          })
        } else {
          console.log('提交信息有错误！')
          return false
        }
      })
    },
    deleteUser(user) {
      this.$confirm(`您确认删除${user.username}用户吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { // 确定
        userApi.deleteUserById(user.userId).then(response => {
          this.$message({
            type: 'success',
            message: response.message
          })
          this.getUserList()
        })
      }).catch(() => { // 取消
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    getAllRoleExceptAdmin() {
      roleApi.getAllRoleExceptAdmin().then(response => {
        this.roleList = response.data
      })
    },
    getAllSchoolList() {
      schoolApi.getAllSchoolList().then(response => {
        this.schoolList = response.data
      })
    },
    getAllSchoolExceptCollege() {
      schoolApi.getAllSchoolExceptCollege().then(response => {
        this.schoolList = response.data
      })
    },
    getCollege() {
      schoolApi.getCollege().then(response => {
        this.schoolList = response.data
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
