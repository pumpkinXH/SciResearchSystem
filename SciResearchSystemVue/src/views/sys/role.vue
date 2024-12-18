<!-- eslint-disable vue/html-closing-bracket-newline -->
<!-- eslint-disable vue/html-indent -->
<!-- eslint-disable vue/max-attributes-per-line -->
<template>
  <div>
    <!--搜索框-->
    <el-card id="search">
      <el-row>
        <el-col :span="20">
          <el-input v-model="searchModel.roleName" placeholder="角色名称" clearable />
          <el-input v-model="searchModel.roleDesc" placeholder="角色描述" clearable />
          <el-button type="primary" icon="el-icon-search" round @click="getRoleList">查询</el-button>
        </el-col>
        <el-col :span="4" align="right">
          <el-button type="primary" icon="el-icon-plus" circle @click="openEditUI(null)" />
        </el-col>
      </el-row>
    </el-card>
    <!--结果列表-->
    <el-card>
      <el-table :data="roleList" stripe style="width: 100%">
        <el-table-column label="序号" width="200">
          <template slot-scope="scope">
            <!--(pageNo-1) * pageSize + index + 1-->
            {{ (searchModel.pageNo - 1) * searchModel.pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="roleName" label="用户名称" width="300" />
        <el-table-column prop="roleDesc" label="角色描述" width="300" />
        <el-table-column label="菜单权限分配" width="300">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" @click="openEditMenuUI(scope.row.roleId)">分配</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" circle size="mini" @click="openEditUI(scope.row.roleId)" />
            <el-button type="danger" icon="el-icon-delete" circle size="mini" @click="deleteRole(scope.row)" />
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 分页组件 -->
    <el-pagination :current-page="searchModel.pageNo" :page-sizes="[5, 10, 20, 50]" :page-size="searchModel.pageSize"
      layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange"
      @current-change="handleCurrentChange" />
    <!-- 菜单权限分配对话框 -->
      <el-dialog v-drag :title="menuTitle" :visible.sync="dialogMenuFormVisible" :close-on-click-modal="false" @close="clearMenuForm">
        <el-form :model="roleForm">
          <h1 style="text-align: center;">{{ roleForm.roleDesc }}</h1>
          <el-form-item label="菜单权限分配" :label-width="formLabelWidth">
            <el-tree ref="menuRef" :data="menuList" :props="menuProps" show-checkbox style="width:%85" default-expand-all
              node-key="menuId" />
          </el-form-item>
        </el-form>
        <div style="text-align: right;">
          <el-button @click="dialogMenuFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveMenu">确 定</el-button>
        </div>
      </el-dialog>
    <!-- 角色信息编辑对话框 -->
    <el-dialog v-drag :title="title" :visible.sync="dialogFormVisible" :close-on-click-modal="false" @close="clearForm">
      <el-form ref="roleFormRef" :model="roleForm" :rules="rules">
        <el-form-item label="角色名称" :label-width="formLabelWidth" prop="roleName">
          <el-input v-model="roleForm.roleName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="角色描述" :label-width="formLabelWidth" prop="roleDesc">
          <el-input v-model="roleForm.roleDesc" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveRole">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import roleApi from '@/api/roleManage' // @代表src目录
import menuApi from '@/api/menuManage' // @代表src目录
import '@/views/directives'
export default {
  data() {
    return {
      formLabelWidth: '150px',
      roleForm: {},
      dialogMenuFormVisible: false, // 控制对话框默认不可见，当点击新增按钮后改为可见
      dialogFormVisible: false,
      title: '',
      menuTitle: '菜单权限分配',
      total: 0,
      searchModel: {
        pageNo: 1,
        pageSize: 10
      },
      roleList: [],
      rules: {
        roleName: [
          { required: true, message: '请输入角色名称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        roleDesc: [
          { required: true, message: '请输入角色描述', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ]
      },
      menuProps: {
        children: 'children',
        label: 'title'
      },
      menuList: []
    }
  },
  created() { // 构造函数
    this.getRoleList()
    this.getAllMenu()
  },
  methods: {
    handleSizeChange(pageSize) {
      this.searchModel.pageSize = pageSize
      this.getRoleList()
    },
    handleCurrentChange(pageNo) {
      this.searchModel.pageNo = pageNo
      this.getRoleList()
    },
    getRoleList() {
      roleApi.getRoleList(this.searchModel).then(response => { // then是回调函数
        this.roleList = response.data.rows
        this.total = response.data.total
      })
    },
    openEditMenuUI(id) {
      // 根据ID查询角色数据
      roleApi.getRoleById(id).then(response => {
        this.roleForm = response.data
        this.$refs.menuRef.setCheckedKeys(response.data.menuIdList) // 回显用户的权限菜单
      })
      this.dialogMenuFormVisible = true
    },
    openEditUI(id) {
      if (id == null) {
        this.title = '新增角色'
      } else {
        this.title = '修改角色'
        // 根据ID查询角色数据
        roleApi.getRoleById(id).then(response => {
          this.roleForm = response.data
        })
      }
      this.dialogFormVisible = true
    },
    clearForm() {
      this.roleForm = {}
      this.$refs.roleFormRef.clearValidate() // 清除表单验证信息
    },
    clearMenuForm() {
      this.$refs.menuRef.setCheckedKeys([]) // 菜单记录置空
    },
    saveMenu() {
      const checkedKeys = this.$refs.menuRef.getCheckedKeys() // 返回目前被选中的节点的 key 所组成的数组
      const halfCheckedKeys = this.$refs.menuRef.getHalfCheckedKeys() // 返回目前半选中的节点所组成的数组
      this.roleForm.menuIdList = checkedKeys.concat(halfCheckedKeys) // 拼接
      roleApi.saveRole(this.roleForm).then(response => {
        // 成功提示
        this.$message({
          message: response.message,
          type: 'success'
        })
        // 关闭对话框
        this.dialogMenuFormVisible = false
      })
    },
    saveRole() {
      // 先触发表单验证
      this.$refs.roleFormRef.validate((valid) => {
        if (valid) {
          // 提交给后台
          roleApi.saveRole(this.roleForm).then(response => {
            // 成功提示
            this.$message({
              message: response.message,
              type: 'success'
            })
            // 关闭对话框
            this.dialogFormVisible = false
            // 刷新表格
            this.getRoleList()
          })
        } else {
          console.log('提交信息有错误！')
          return false
        }
      })
    },
    deleteRole(role) {
      this.$confirm(`您确认删除${role.roleName}角色吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { // 确定
        roleApi.deleteRoleById(role.roleId).then(response => {
          this.$message({
            type: 'success',
            message: response.message
          })
          this.getRoleList()
        })
      }).catch(() => { // 取消
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    getAllMenu() {
      menuApi.getAllMenu().then(response => {
        this.menuList = response.data
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
  width: '%50'
}
</style>
