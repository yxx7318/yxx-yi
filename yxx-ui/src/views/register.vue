<template>
  <div style="height: 100vh">
    <el-row>
      <el-col :span="isMobile ? 0 : 12" v-show="!isMobile">
        <login-left></login-left>
      </el-col>
      <el-col :span="isMobile ? 24 : 12">
        <register-right :isMobile="isMobile"></register-right>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import loginLeft from './loginLeft.vue';
import registerRight from './registerRight.vue';

export default {
  name: 'Register',
  // 注册组件
  components: {loginLeft, registerRight},
  data() {
    return {
      isMobile: false
    };
  },
  mounted() {
    this.checkScreenSize();
    window.addEventListener('resize', this.checkScreenSize);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.checkScreenSize);
  },
  methods: {
    checkScreenSize() {
      this.isMobile = window.innerWidth < 768; // 768px通常是平板和手机屏幕的分界线
    }
  }
}
</script>

<style>
</style>
