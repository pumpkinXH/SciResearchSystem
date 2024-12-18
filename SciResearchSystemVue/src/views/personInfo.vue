<!-- eslint-disable vue/html-closing-bracket-newline -->
<!-- eslint-disable vue/html-indent -->
<!-- eslint-disable vue/max-attributes-per-line -->
<template>
  <div>
    <el-card style="width:600px">
      <!-- 头像 -->
      <el-upload class="avatar-uploader" :action="'http://' + serverIp + ':9999/files/upload'" :show-file-list="false"
        :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
        <img v-if="userForm.userAvatar" :src="userForm.userAvatar" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon" />
      </el-upload>
      <el-form ref="userFormRef" :model="userForm" :rules="rules">
        <el-form-item label="卡号" :label-width="formLabelWidth" prop="card">
          <el-input v-model="userForm.card" :disabled="true" autocomplete="off" />
        </el-form-item>
        <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
          <el-input v-model="userForm.username" :disabled="true" autocomplete="new-password" />
        </el-form-item>
        <el-form-item label="电话" :label-width="formLabelWidth" prop="userPhone">
          <el-input v-model="userForm.userPhone" autocomplete="off" />
        </el-form-item>
        <el-form-item label="电子邮件" :label-width="formLabelWidth" prop="userEmail">
          <el-input v-model="userForm.userEmail" autocomplete="off" />
        </el-form-item>
        <el-form-item label="所属" :label-width="formLabelWidth" prop="schoolDesc">
          <el-input v-model="userForm.schoolDesc" :disabled="true" autocomplete="off" />
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
import schoolApi from '@/api/schoolManage'
import store from '@/store'
import { getPersonInfo } from '@/api/user'
import { mapMutations } from 'vuex'
import { serverIp } from '@/serverConfig'
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
      serverIp: serverIp,
      formLabelWidth: '130px',
      userForm: {
      },
      rules: {
        card: [
          { required: true, message: '请输入卡号', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
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
        ]
      },
      schoolList: []
    }
  },
  created() { // 构造函数
    this.getUserInfo()
    this.getAllSchoolList()
  },
  methods: {
    ...mapMutations(['SET_AVATAR']),
    getUserInfo() {
      const token = store.getters.token
      getPersonInfo(token).then(response => {
        this.userForm = response.data
        store.dispatch('user/getUserInfo', this.userForm)
      })
    },
    clearForm() {
      this.userForm = {}
      this.$refs.userFormRef.clearValidate() // 清除表单验证信息
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
            // 刷新信息
            this.getUserInfo()
          })
        } else {
          console.log('提交信息有错误！')
          return false
        }
      })
    },
    getAllSchoolList() {
      schoolApi.getAllSchoolList().then(response => {
        this.schoolList = response.data
      })
    },
    handleAvatarSuccess(res) {
      this.userForm.userAvatar = res
      this.$message({
        type: 'success',
        message: '上传成功'
      })
    },
    beforeAvatarUpload(file) {
      const isJPG =
        file.type === 'image/jpeg' ||
        file.type === 'image/png' ||
        file.type === 'image/jpg' ||
        file.type === 'image/gif'
      if (!isJPG) {
        this.$message.error('上传图片只能是jpg、gif、png、jpeg格式！')
        return false
      }
      return isJPG
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
</style>

<style>
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 138px;
  height: 138px;
  line-height: 138px;
  text-align: center;
}

.avatar {
  width: 138px;
  height: 138px;
  display: block;
}
</style>
