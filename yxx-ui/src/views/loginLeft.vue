<template>
  <div style="height: 100vh; min-height: 500px; background-color: #353e54;">
    <login-logo></login-logo>
    <el-row style="min-height: 350px; max-height: 350px;">
      <el-col>
        <!-- 中心背景图 -->
        <div style="display: flex; justify-content: center;">
          <img src="../assets/svg/login-box-bg.svg" alt style="width: 360px; height: 360px;" />
        </div>
      </el-col>
    </el-row>
    <el-row>
      <el-col>
        <div style="display: flex; justify-content: center;">
          <span style="font-family: Helvetica Neue; color: white; font-size: 30px; margin-top: -30px;">欢迎进入系统</span>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
  
<script>
import loginLogo from './loginLogo.vue';
export default {
  name: 'LoginLeft',
  // 注册组件
  components: {loginLogo},
  data() {
    return {
    };
  },
  methods: {
  }
}
</script>
  
<style>
</style>