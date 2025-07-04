<template>
  <el-row style="height: 100vh; min-height: 500px; background-color: #353e54;" type="flex" justify="center" align="middle">
    <Logo />
    <el-row style="min-height: 350px; max-height: 350px">
      <el-col>
        <!-- 中心背景图 -->
        <div>
          <img src="@/assets/svg/login-box-bg.svg" alt style="width: 360px; height: 360px" />
        </div>
        <div style="display: flex; justify-content: center;">
          <span style="font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, Arial, sans-serif; color: white; font-size: 30px; margin-top: -26px">欢迎进入系统</span>
        </div>
      </el-col>
    </el-row>
  </el-row>
</template>

<script>
import { defineComponent } from 'vue';
import Logo from '@/components/Logo';

export default defineComponent({
  name: 'SystemBackground',
  components: { Logo },
  setup() {
    // 如果需要响应式数据或方法可以在这里定义
    return {
      // 可以返回需要暴露给模板的数据或方法
    };
  }
});
</script>

<style rel="stylesheet/scss" lang="scss">
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
