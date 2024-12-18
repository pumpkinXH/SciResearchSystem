<!-- eslint-disable vue/html-closing-bracket-newline -->
<!-- eslint-disable vue/html-indent -->
<!-- eslint-disable vue/max-attributes-per-line -->
<template>
  <div>
    <el-card style="width:600px">
      <h3 class="title">修改用户名和密码</h3>
      <el-form ref="userForm" :model="userForm" :rules="rules">
        <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
          <el-input v-model="userForm.username" autocomplete="new-password" />
        </el-form-item>
        <el-form-item label="原密码" :label-width="formLabelWidth" prop="password">
          <el-input v-model="userForm.password" show-password auto-complete="off" />
        </el-form-item>
        <el-form-item label="新密码" :label-width="formLabelWidth" prop="newPassword">
          <el-input v-model="userForm.newPassword" show-password auto-complete="new-password" />
        </el-form-item>
        <el-form-item label="确认新密码" :label-width="formLabelWidth" prop="checkPassword">
          <el-input v-model="userForm.checkPassword" show-password auto-complete="new-password" />
        </el-form-item>
        <el-form-item class="button">
          <el-button style="width:30%;" @click="clearForm">重置</el-button>
          <el-button style="width:30%;" type="primary" @click="saveUser">提交</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import userApi from '@/api/userManage' // @代表src目录
import store from '../store'
import { getPersonInfo } from '@/api/user'
export default {
  data() {
    // 检查密码
    var validatePass = (rule, value, callback) => {
      if (this.userForm.checkPassword !== '') {
        this.$refs.userForm.validateField('checkPassword')
      }
      callback()
    }
    // 检查确认密码
    var validateCheckPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入新密码'))
      } else if (value !== this.userForm.newPassword) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      formLabelWidth: '130px',
      userForm: {},
      rules: {
        password: [
          { required: true, message: '请输入原密码', trigger: 'blur' },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur', validator: validatePass }
        ],
        checkPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { validator: validateCheckPass, trigger: 'blur' }
        ]
      }
    }
  },
  created() { // 构造函数
    this.getUserInfo()
  },
  methods: {
    getUserInfo() {
      const token = store.getters.token
      getPersonInfo(token).then(response => {
        this.userForm = response.data
        this.userForm.password = ''
      })
    },
    clearForm() {
      this.userForm = {}
      this.$refs.userForm.clearValidate() // 清除表单验证信息
    },
    // 回到登陆界面
    async logout() {
      await this.$store.dispatch('user/logout')
      await this.$store.dispatch('tagsView/delAllViews')
      sessionStorage.removeItem('tabViews')
      this.$router.push(`/login`)
    },
    saveUser() {
      // 先触发表单验证
      this.$refs.userForm.validate((valid) => {
        if (valid) {
          // 提交给后台
          userApi.updateKeyInfo(this.userForm).then(response => {
            // 成功提示
            this.$message({
              message: response.message,
              type: 'success'
            })
            // 回到登录界面
            this.logout()
          })
        } else {
          console.log('提交信息有错误！')
          return false
        }
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

.button {
  width: 100%;
  margin-bottom: 30px;
  text-align: center;
}

.title {
  font-size: 26px;
  margin: 0px auto 40px auto;
  text-align: center;
  font-weight: bold
}
</style>
