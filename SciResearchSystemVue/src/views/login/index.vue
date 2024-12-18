<!-- eslint-disable vue/html-closing-bracket-newline -->
<!-- eslint-disable vue/html-indent -->
<!-- eslint-disable vue/max-attributes-per-line -->
<template>
  <div class="login-container">
    <el-form ref="loginForm" class="login-form" :model="loginForm" :rules="loginRules" auto-complete="on"
      label-position="left">

      <div class="title-container">
        <h3 class="title">欢迎使用高校科研项目与成果管理系统</h3>
      </div>

      <p class="login_title">
        <span :class="isActiveIndex === 0 ? 'isActiveTitle' : ''" @click="usernameLogin">用户名登录</span>
        <span :class="isActiveIndex === 1 ? 'isActiveTitle' : ''" @click="cardLogin">卡号登录</span>
      </p>

      <el-form v-if="isActive" ref="loginForm" :model="loginForm" :rules="loginRules" auto-complete="on"
        label-position="left">
        <el-form-item prop="username">
          <span class="svg-container">
            <svg-icon icon-class="user" />
          </span>
          <el-input ref="username" v-model="loginForm.username" placeholder="用户名" name="username" type="text" tabindex="1"
            auto-complete="on" />
        </el-form-item>

        <el-form-item prop="password">
          <span class="svg-container">
            <svg-icon icon-class="password" />
          </span>
          <el-input :key="passwordType" ref="password" v-model="loginForm.password" :type="passwordType" placeholder="密码"
            name="password" tabindex="2" auto-complete="on" @keyup.enter.native="handleLogin" />
          <span class="show-pwd" @click="showPwd">
            <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
          </span>
        </el-form-item>
        <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;"
        @click.native.prevent="handleLogin">登 录</el-button>
      </el-form>
      <el-form v-else ref="cardLoginForm" :model="cardLoginForm" :rules="cardLoginRules" auto-complete="on"
        label-position="left">
        <el-form-item prop="card">
          <span class="svg-container">
            <svg-icon icon-class="card" />
          </span>
          <el-input ref="card" v-model="cardLoginForm.card" placeholder="卡号" name="card" type="text" tabindex="1"
            auto-complete="on" />
        </el-form-item>
        <el-form-item prop="password">
          <span class="svg-container">
            <svg-icon icon-class="password" />
          </span>
          <el-input :key="passwordType" ref="password" v-model="cardLoginForm.password" :type="passwordType"
            placeholder="密码" name="password" tabindex="2" auto-complete="on" @keyup.enter.native="handleLogin" />
          <span class="show-pwd" @click="showPwd">
            <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
          </span>
        </el-form-item>
        <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;"
        @click.native.prevent="handleCardLogin">登 录</el-button>
      </el-form>
      <div class="register">
        <el-link target="_blank" type="primary" @click="$router.push({ path: '/register' })">没有账号？现在注册！</el-link>
      </div>
    </el-form>
  </div>
</template>

<script>

export default {
  name: 'Login',
  data() {
    // 检查卡号是否合规
    const checkCard = (rule, value, callback) => {
      var regNum = /^\+?[0-9][0-9]*$/ // 整数（可以以0打头）
      if (!regNum.test(value)) {
        return callback(new Error('请输入整数'))
      }
      if (value.length < 3 || value.length > 10) {
        return callback(new Error('请输入3~10位整数'))
      }
      callback()
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码不能少于6位'))
      } else {
        callback()
      }
    }
    return {
      isActive: true, // 用于实现切换登录，作为判断
      isActiveIndex: 0,
      loginForm: {
        username: '',
        password: ''
      },
      cardLoginForm: {
        card: '',
        password: ''
      },
      loginRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      cardLoginRules: {
        card: [{ required: true, trigger: 'blur', validator: checkCard }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created() {
    this.keyupSubmit()
  },
  methods: {
    usernameLogin() { // 用户名登录
      this.isActive = true
      this.isActiveIndex = 0
    },
    cardLogin() { // 卡号登录
      this.isActive = false
      this.isActiveIndex = 1
    },
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
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/login', this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || '/' })
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        } else {
          console.log('提交信息有误')
          return false
        }
      })
    },
    handleCardLogin() {
      this.$refs.cardLoginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/cardLogin', this.cardLoginForm).then(() => {
            this.$router.push({ path: this.redirect || '/' })
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        } else {
          console.log('提交信息有误')
          return false
        }
      })
    },
    // Enter点击事件
    keyupSubmit() {
      document.onkeydown = () => {
        const _key = window.event.keyCode
        if (_key === 13 && this.isActive === true) {
          this.handleLogin()
        }
        if (_key === 13 && this.isActive === false) {
          this.handleCardLogin()
        }
      }
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

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  background-image: url('~@/assets/bg.jpg');
  background-size: 100%;

  display: flex;
  align-items: center;

  .login-form {
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

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
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

  .register {
    text-align: right;
    height: 20px;
  }
}
</style>

<style scoped>
.login_box {
  width: 450px;
  height: 490px;
  background-color: #fff;
  border-radius: 18px;
  padding: 30px 50px 30px 30px;
}

.login_title {
  text-align: center;
  margin: 30px 30px 40px 30px;
  font-size: 24px;
  color: #666;
}

.login_title span {
  margin: 0 20px 0 20px;
  cursor: pointer;
}

.login_title span:hover {
  color: white;
  font-weight: bolder;
}

.isActiveTitle {
  color: white;
  font-weight: bolder;
}

.login_agree {
  margin-left: 30px;
}

.login_btn {
  width: 100%;
  height: 50px;
  margin: 0 0 0 10px;
  background-color: #0cb95f;
  color: #fff;
}
</style>
