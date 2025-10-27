<template>
  <el-dialog :title="title" :visible.sync="open" width="400px" :before-close="uploadClose" append-to-body>
    <!-- 导入对话框 -->
    <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="headers" :action="action" :disabled="isUploading" :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
      <i class="el-icon-upload"></i>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <div class="el-upload__tip text-center" slot="tip">
        <div class="el-upload__tip" slot="tip">
          <!-- 插槽内容 -->
          <slot></slot>
        </div>
        <span>仅允许导入xls、xlsx格式文件。</span>
        <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline" @click="importTemplate">下载模板</el-link>
      </div>
    </el-upload>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitFileForm">确 定</el-button>
      <el-button @click="uploadClose">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getToken } from "@/utils/auth"
import { tansParams } from "@/utils/yxx"

export default {
  name: "ExcelUpload",
  props: {
    // 父组件传递的标题
    title: {
      type: String,
      default: "上传文件",
    },
    // 是否显示弹出层（父组件控制）
    open: {
      type: Boolean,
      default: false,
      required: true
    },
    // 模板文件地址
    templateUrl: {
      type: String,
      default: "importTemplate",
    },
    // 上传接口地址（可由父组件动态传递）
    uploadUrl: {
      type: String,
      required: true
    },
    // 上传其它参数
    uploadParams: {
      type: Object,
      default: null
    },
    // 上传的请求头部
    headers: {
      type: Object,
      default: () => { return { Authorization: "Bearer " + getToken() } },
    },
    // 上传的基本地址
    baseUrl: {
      type: String,
      default: process.env.VUE_APP_BASE_API
    }
  },
  data() {
    return {
      // 是否禁用上传
      isUploading: false
    }
  },
  computed: {
    // 最终上传文件的请求地址
    action() {
      return this.baseUrl + this.uploadUrl + (this.uploadParams ? `?${tansParams(this.uploadParams)}` : "")
    },
    // 模板文件的请求地址
    fullTemplateUrl() {
      if (this.templateUrl.indexOf("/") !== -1) {
        return this.templateUrl
      }
      let suffix = this.uploadUrl.split("/").slice(-1)[0]
      return this.uploadUrl.replace(suffix, this.templateUrl)
    },
    // 模板文件的名称
    templateName() {
      return `${this.uploadUrl.split("/").join("_")}_${this.parseTime(new Date())}.xlsx`
    }
  },
  methods: {
    // 下载模板操作
    importTemplate() {
      this.download(this.fullTemplateUrl, {
      }, this.templateName)
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.$modal.loading("正在上传文件中，请稍候！")
      this.isUploading = true
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.$modal.closeLoading()
      this.uploadClose()
      this.isUploading = false
      this.$refs.upload.clearFiles()
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.$emit("handleFileSuccess", response)
    },
    // 提交上传文件
    submitFileForm() {
      const file = this.$refs.upload.uploadFiles
      if (!file || file.length === 0 || !file[0].name.toLowerCase().endsWith('.xls') && !file[0].name.toLowerCase().endsWith('.xlsx')) {
        this.$modal.msgError("请选择后缀为 “xls”或“xlsx”的文件。")
        return
      }
      this.$refs.upload.submit()
    },
    // 通知父组件关闭窗口
    uploadClose() {
      this.$emit('uploadClose')
    }
  }
}
</script>
