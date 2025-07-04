<template>
  <div style="height: 100vh">
    <el-row>
      <el-col :span="isMobile ? 0 : 12" v-show="!isMobile">
        <SystemBackground />
      </el-col>
      <el-col :span="isMobile ? 24 : 12">
        <div>
          <Logo v-show="isMobile" />
          <div :class="['register', isMobile ? 'mobileLogin' : 'noMobileLogin']">
            <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" class="register-form">
              <h2 class="title">注 册</h2>
              <el-form-item prop="username">
                <el-input v-model="registerForm.username" type="text" auto-complete="off" placeholder="账号">
                  <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon"/>
                </el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input
                  v-model="registerForm.password"
                  type="password"
                  auto-complete="off"
                  placeholder="密码"
                  @keyup.enter="handleRegister"
                >
                  <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon"/>
                </el-input>
              </el-form-item>
              <el-form-item prop="confirmPassword">
                <el-input
                  v-model="registerForm.confirmPassword"
                  type="password"
                  auto-complete="off"
                  placeholder="确认密码"
                  @keyup.enter="handleRegister"
                >
                  <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon"/>
                </el-input>
              </el-form-item>
              <el-form-item prop="code" v-if="captchaEnabled">
                <el-input
                  v-model="registerForm.code"
                  auto-complete="off"
                  placeholder="验证码"
                  style="width: 63%"
                  @keyup.enter="handleRegister"
                >
                  <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon"/>
                </el-input>
                <div class="register-code">
                  <img :src="codeUrl" @click="getCode" class="register-code-img"/>
                </div>
              </el-form-item>
              <el-form-item style="width:100%">
                <el-button
                  :loading="loading"
                  size="medium"
                  type="primary"
                  style="width:100%"
                  @click.prevent="handleRegister"
                >
                  <span v-if="!loading">注 册</span>
                  <span v-else>注 册 中...</span>
                </el-button>
                <div style="float: right">
                  <router-link class="link-type" :to="'/login'">使用已有账户登录</router-link>
                </div>
              </el-form-item>
            </el-form>
            <div class="el-register-footer">
              <span>{{ footerContent}}</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import SystemBackground from '@/components/SystemBackground'
import Logo from "@/components/Logo"
import { getCodeImg, register } from "@/api/login"
import { mobileFlag } from "@/utils/yxx"
import settings from "@/settings";

// 响应式数据
const isMobile = ref(false)
const codeUrl = ref("")
const loading = ref(false)
const captchaEnabled = ref(true)
const registerFormRef = ref(null)
const footerContent = settings.footerContent

const registerForm = ref({
  username: "",
  password: "",
  confirmPassword: "",
  code: "",
  uuid: ""
})

// 密码验证规则
const validatePassword = (rule, value, callback) => {
  if (value !== registerForm.value.password) {
    callback(new Error("两次输入的密码不一致"))
  } else {
    callback()
  }
}

const registerRules = ref({
  username: [
    { required: true, trigger: "blur", message: "请输入您的账号" },
    { min: 2, max: 20, message: '用户账号长度必须介于 2 和 20 之间', trigger: 'blur' }
  ],
  password: [
    { required: true, trigger: "blur", message: "请输入您的密码" },
    { min: 5, max: 20, message: "用户密码长度必须介于 5 和 20 之间", trigger: "blur" },
    { pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符：< > \" ' \\\ |", trigger: "blur" }
  ],
  confirmPassword: [
    { required: true, trigger: "blur", message: "请再次输入您的密码" },
    { required: true, validator: validatePassword, trigger: "blur" }
  ],
  code: [{ required: true, trigger: "change", message: "请输入验证码" }]
})

// 方法
const checkScreenSize = () => {
  isMobile.value = mobileFlag()
}

const getCode = () => {
  getCodeImg().then(res => {
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
    if (captchaEnabled.value) {
      codeUrl.value = "data:image/gif;base64," + res.img
      registerForm.value.uuid = res.uuid
    }
  })
}

const handleRegister = () => {
  registerFormRef.value.validate(valid => {
    if (valid) {
      loading.value = true
      register(registerForm.value).then(res => {
        const username = registerForm.value.username
        ElMessageBox.alert(`<font color='red'>恭喜你，您的账号 ${username} 注册成功！</font>`, '系统提示', {
          dangerouslyUseHTMLString: true,
          type: 'success'
        }).then(() => {
          router.push("/login")
        }).catch(() => {})
      }).catch(() => {
        loading.value = false
        if (captchaEnabled.value) {
          getCode()
        }
      })
    }
  })
}

// 生命周期钩子
onMounted(() => {
  checkScreenSize()
  window.addEventListener('resize', checkScreenSize)
  getCode()
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', checkScreenSize)
})

const router = useRouter()
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
.register {
  display: flex;
  justify-content: center;
  align-items: center;
}

.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}

.register-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;

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

.register-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}

.register-code {
  width: 33%;
  height: 38px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
  }
}

.el-register-footer {
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

.register-code-img {
  height: 38px;
}
</style>
