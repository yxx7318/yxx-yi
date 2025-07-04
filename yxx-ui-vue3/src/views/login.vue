<template>
  <div style="height: 100vh">
    <el-row>
      <el-col :span="isMobile ? 0 : 12" v-show="!isMobile">
        <SystemBackground />
      </el-col>
      <el-col :span="isMobile ? 24 : 12">
        <div>
          <Logo v-show="isMobile" />
          <div :class="['login', isMobile ? 'mobileLogin' : 'noMobileLogin']">
            <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
              <h2 class="title">登 录</h2>
              <el-form-item prop="username">
                <el-input
                  v-model="loginForm.username"
                  type="text"
                  auto-complete="off"
                  placeholder="账号"
                >
                  <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
                </el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input
                  v-model="loginForm.password"
                  type="password"
                  auto-complete="off"
                  placeholder="密码"
                  @keyup.enter="handleLogin"
                >
                  <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
                </el-input>
              </el-form-item>
              <el-form-item prop="code" v-if="captchaEnabled">
                <el-input
                  v-model="loginForm.code"
                  auto-complete="off"
                  placeholder="验证码"
                  style="width: 63%"
                  @keyup.enter="handleLogin"
                >
                  <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
                </el-input>
                <div class="login-code">
                  <img alt="please retry" :src="codeUrl" @click="getCode" class="login-code-img" />
                </div>
              </el-form-item>
              <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px">记住密码</el-checkbox>
              <el-form-item style="width:100%">
                <el-button
                  :loading="loading"
                  size="medium"
                  type="primary"
                  style="width:100%"
                  @click.prevent="handleLogin"
                >
                  <span v-if="!loading">登 录</span>
                  <span v-else>登 录 中...</span>
                </el-button>
                <div style="float: right;" v-if="register">
                  <router-link class="link-type" :to="'/register'">立即注册</router-link>
                </div>
              </el-form-item>
            </el-form>
            <div class="el-login-footer">
              <span>{{ footerContent }}</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Cookies from 'js-cookie'
import { encrypt, decrypt } from '@/utils/jsencrypt'
import { getCodeImg } from '@/api/login'
import SystemBackground from '@/components/SystemBackground'
import Logo from '@/components/Logo'
import settings from '@/settings'
import useUserStore from '@/store/modules/user'
import {mobileFlag} from "@/utils/yxx.js";


const isMobile = ref(false)
const title = import.meta.env.VITE_APP_TITLE
const userStore = useUserStore()
const route = useRoute()
const router = useRouter()
const { proxy } = getCurrentInstance()
const footerContent = settings.footerContent

const checkScreenSize = () => {
  isMobile.value = mobileFlag()
}

// 响应式数据
const loginForm = ref({
  username: 'admin',
  password: 'admin123',
  rememberMe: false,
  code: '',
  uuid: ''
})

const loginRules = {
  username: [{ required: true, trigger: 'blur', message: '请输入您的账号' }],
  password: [{ required: true, trigger: 'blur', message: '请输入您的密码' }],
  code: [{ required: true, trigger: 'change', message: '请输入验证码' }]
}

const codeUrl = ref('')
const loading = ref(false)
const captchaEnabled = ref(true)
const register = ref(false)
const redirect = ref(undefined)

onMounted(() => {
  checkScreenSize() // 初始化检查
  window.addEventListener('resize', checkScreenSize) // 监听窗口变化
  getCode() // 初始获取验证码
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', checkScreenSize) // 组件卸载时移除监听
})

// 方法
function handleLogin() {
  proxy.$refs.loginRef.validate(valid => {
    if (valid) {
      loading.value = true
      // 勾选了需要记住密码设置在 cookie 中设置记住用户名和密码
      if (loginForm.value.rememberMe) {
        Cookies.set("username", loginForm.value.username, { expires: 30 })
        Cookies.set("password", encrypt(loginForm.value.password), { expires: 30 })
        Cookies.set("rememberMe", loginForm.value.rememberMe, { expires: 30 })
      } else {
        // 否则移除
        Cookies.remove("username")
        Cookies.remove("password")
        Cookies.remove("rememberMe")
      }
      // 调用action的登录方法
      userStore.login(loginForm.value).then(() => {
        const query = route.query
        const otherQueryParams = Object.keys(query).reduce((acc, cur) => {
          if (cur !== "redirect") {
            acc[cur] = query[cur]
          }
          return acc
        }, {})
        router.push({ path: redirect.value || "/", query: otherQueryParams })
      }).catch(() => {
        loading.value = false
        // 重新获取验证码
        if (captchaEnabled.value) {
          getCode()
        }
      })
    }
  })
}

const getCode = () => {
  getCodeImg().then(res => {
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
    if (captchaEnabled.value) {
      codeUrl.value = 'data:image/gif;base64,' + res.img
      loginForm.value.uuid = res.uuid
    }
  })
}

const getCookie = () => {
  const username = Cookies.get('username')
  const password = Cookies.get('password')
  const rememberMe = Cookies.get('rememberMe')
  loginForm.value = {
    username: username ?? loginForm.value.username,
    password: password ? decrypt(password) : loginForm.value.password,
    rememberMe: rememberMe ? Boolean(rememberMe) : false
  }
}

// 监听路由变化
watch(() => route.query, (query) => {
  redirect.value = query?.redirect
}, { immediate: true })

// 初始化
getCode()
getCookie()
</script>

<style scoped>
.mobileLogin {
  background-color: #353e54;
  height: 100vh;
  min-height: 400px;
}
.noMobileLogin {
  border-color: #bfbfbf;
  height: 100vh;
  background-color: #bfbfbf;
  min-height: 500px;
}
.login {
  display: flex;
  justify-content: center;
  align-items: center;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
  z-index: 1;
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.login-code-img {
  height: 38px;
}
</style>
