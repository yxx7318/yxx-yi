<template>
  <el-dialog :title="title" v-model="dialogVisible" width="400px" :before-close="uploadClose" append-to-body>
    <!-- 用户导入对话框 -->
    <el-upload ref="uploadRef" :limit="1" accept=".xlsx, .xls" :headers="headers" :action="action" :disabled="isUploading" :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <template #tip>
        <div class="el-upload__tip text-center">
          <div class="el-upload__tip">
            <!-- 插槽内容 -->
            <slot></slot>
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline" @click="importTemplate">下载模板</el-link>
        </div>
      </template>
    </el-upload>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="uploadClose">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="ExcelUpload">
import { ref, computed } from 'vue'
import { getToken } from "@/utils/auth"
import { tansParams } from "@/utils/yxx"

// 定义props
const props = defineProps({
  // 父组件传递的标题
  title: { type: String, default: "上传文件" },
  // 是否显示弹出层（父组件控制）
  open: { type: Boolean, default: false, required: true },
  // 模板文件地址
  templateUrl: { type: String, default: "importTemplate" },
  // 上传接口地址（由父组件动态传递）
  uploadUrl: { type: String, required: true },
  // 上传其它参数
  uploadParams: { type: Object, default: null },
  // 上传的请求头部
  headers: { type: Object, default: () => ({ Authorization: "Bearer " + getToken() }) },
  // 上传的基本地址
  baseUrl: { type: String, default: import.meta.env.VITE_APP_BASE_API }
})

// 定义emits
const emit = defineEmits(['update:open', 'uploadClose', 'handleFileSuccess'])

// 获取组件实例
const { proxy } = getCurrentInstance()

// 控制dialog显示状态(props.open只读)
const dialogVisible = ref(props.open)
// 当父组件更新 props.open 时，同步更新子组件的 dialogVisible
watch(
  () => props.open, (newVal) => {dialogVisible.value = newVal},
  { immediate: true }
)
// 子组件内部修改 dialogVisible 时，通知父组件更新 props.open
watch(dialogVisible, (newVal) => {
  emit('update:open', newVal)
})

// 控制上传状态
const isUploading = ref(false)

// 计算属性
const action = computed(() => props.baseUrl + props.uploadUrl + (props.uploadParams ? `?${tansParams(props.uploadParams)}` : ""))
const fullTemplateUrl = computed(() => {
  if (props.templateUrl.indexOf("/") !== -1) return props.templateUrl
  let suffix = props.uploadUrl.split("/").slice(-1)[0]
  return props.uploadUrl.replace(suffix, props.templateUrl)
})
const templateName = computed(() => `${props.uploadUrl.split("/").join("_")}_${proxy.parseTime(new Date())}.xlsx`)

// 方法定义
/** 下载模板操作 */
function importTemplate() {
  proxy.download(fullTemplateUrl.value, {}, templateName.value)
}

// 文件上传中处理
function handleFileUploadProgress(event, file, fileList) {
  proxy.$modal.loading("正在上传文件中，请稍候！")
  isUploading.value = true
}

// 文件上传成功处理
function handleFileSuccess(response, file, fileList) {
  proxy.$modal.closeLoading()
  uploadClose()
  isUploading.value = false
  proxy.$refs["uploadRef"].handleRemove(file)
  proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true })
  emit('handleFileSuccess', response)
}

// 提交上传文件
function submitFileForm() {
  proxy.$refs["uploadRef"].submit()
}

// 通知父组件关闭窗口
function uploadClose() {
  emit('update:open', false)
  emit('uploadClose')
}
</script>