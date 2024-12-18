<!-- eslint-disable vue/html-closing-bracket-newline -->
<!-- eslint-disable vue/html-indent -->
<!-- eslint-disable vue/max-attributes-per-line -->
<template>
  <div class="register-container">
    <!-- 用户注册表单 -->
    <el-form ref="userForm" :model="userForm" :rules="rules" class="register-form">
      <div class="title-container">
        <h3 class="title">欢迎注册</h3>
      </div>
      <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
        <el-input v-model="userForm.username" autocomplete="off" />
      </el-form-item>
      <el-form-item label="密码" :label-width="formLabelWidth" prop="password">
        <el-input :key="passwordType" ref="password" v-model="userForm.password" :type="passwordType" name="password"
          auto-complete="new-password" />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>
      <el-form-item label="确认密码" :label-width="formLabelWidth" prop="checkPassword">
        <el-input :key="checkPasswordType" ref="password" v-model="userForm.checkPassword" :type="checkPasswordType"
          name="password" auto-complete="new-password" />
        <span class="show-pwd" @click="showCPwd">
          <svg-icon :icon-class="checkPasswordType === 'password' ? 'checkEye' : 'checkEye-open'" />
        </span>
      </el-form-item>
      <el-form-item label="电话" :label-width="formLabelWidth" prop="userPhone">
        <el-input v-model="userForm.userPhone" autocomplete="off" />
      </el-form-item>
      <el-form-item label="电子邮件" :label-width="formLabelWidth" prop="userEmail">
        <el-input v-model="userForm.userEmail" autocomplete="off" />
      </el-form-item>
      <el-form-item label="学院" :label-width="formLabelWidth" prop="userSchoolId">
        <el-select v-model="userForm.userSchoolId" placeholder="请选择" filterable style="width:370px">
          <el-option v-for="item in schoolList" :key="item.schoolId" :label="item.schoolDesc" :value="item.schoolId" />
        </el-select>
      </el-form-item>
      <div class="button">
        <el-button style="width:30%;" @click="clearForm">重置</el-button>
        <el-button style="width:30%;" type="primary" @click="saveUser">提交</el-button>
      </div>
      <div class="login">
        <el-link target="_blank" type="primary" @click="$router.push({ path: '/login' })">已有账号？返回登录！</el-link>
      </div>
    </el-form>
  </div>
</template>

<script>

import userApi from '@/api/userManage' // @代表src目录
import schoolApi from '@/api/schoolManage'

export default {
  name: 'Register',
  data() {
    // 检查密码
    var validatePass = (rule, value, callback) => {
      if (value.length < 6 || value.length > 20) {
        callback(new Error('长度在 6 到 20 个字符'))
      } else {
        this.$refs.userForm.validateField('checkPassword')
        callback()
      }
    }
    // 检查确认密码
    var validateCheckPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.userForm.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    // 检查email是否合规
    var checkEmail = (rule, value, callback) => {
      var reg = /^[a-zA-Z0-9]+([-_.][a-zA-Z0-9]+)*@[a-zA-Z0-9]+([-_.][a-zA-Z0-9]+)*\.[a-z]{2,}$/ // 检查Email合规的正则表达式
      if (!reg.test(value)) {
        return callback(new Error('邮箱格式错误'))
      }
      callback()
    }
    // 检查电话号码是否合规
    var checkPhone = (rule, value, callback) => {
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
      dialogFormVisible: 'true',
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur', validator: validatePass }
        ],
        checkPassword: [
          { validator: validateCheckPass, trigger: 'blur' }
        ],
        userPhone: [
          { required: true, message: '请输入电话', trigger: 'blur' },
          { validator: checkPhone, trigger: 'blur' }
        ],
        userEmail: [
          { required: true, message: '请输入电子邮件', trigger: 'blur' },
          { validator: checkEmail, trigger: 'blur' }
        ],
        userSchoolId: [
          { required: true, message: '请选择一个选项', trigger: 'blur' }
        ]
      },
      passwordType: 'password',
      checkPasswordType: 'password',
      formLabelWidth: '80px',
      userForm: {
        card: '',
        username: '',
        password: '',
        userPhone: '',
        userEmail: '',
        userSchoolId: '',
        checkPassword: ''
      },
      schoolList: []
    }
  },
  created() { // 构造函数
    this.getAllSchoolExceptCollege()
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    showCPwd() {
      if (this.checkPasswordType === 'password') {
        this.checkPasswordType = ''
      } else {
        this.checkPasswordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    clearForm() {
      this.userForm = {}
      this.$refs.userForm.clearValidate() // 清除表单验证信息
    },
    saveUser() {
      // 先触发表单验证
      this.$refs.userForm.validate((valid) => {
        if (valid) {
          // 提交给后台
          userApi.saveUser(this.userForm).then(response => {
            // 成功提示
            this.$message({
              message: '注册成功',
              type: 'success'
            })
            // 回到登录界面
            this.$router.push({ path: '/login' })
          })
        } else {
          console.log('提交信息有错误！')
          return false
        }
      })
    },
    getAllSchoolExceptCollege() {
      schoolApi.getAllSchoolExceptCollege().then(response => {
        this.schoolList = response.data
      })
    }
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg: #283443;
$light_gray: #fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" >
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

.register-container {
  .el-form-item__label {
    color: rgba(255, 255, 255, 0.8) !important;
  }

  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray !important;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }

  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  background-image: url('~@/assets/bg.jpg');
  background-size: 100%;

  display: flex;
  align-items: center;

  .register-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 35px 35px 10px;
    margin: 0 auto;
    overflow: hidden;
    background-color: #2d3e4b;
    border-radius: 8px; //边框圆角
    opacity: 0.9; //透明度
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}

.button {
  width: 100%;
  margin-bottom: 30px;
  text-align: center;
}

.login {
  text-align: right;
  height: 20px;
}
</style>
