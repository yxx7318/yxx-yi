<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px" class="el-form-flex">
      <el-form-item label="会话id" prop="chatConversationId">
        <el-input
          v-model="queryParams.chatConversationId"
          placeholder="请输入会话id"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="消息类型" prop="messageType">
        <el-select v-model="queryParams.messageType" placeholder="请选择消息类型" clearable>
          <el-option
            v-for="dict in ai_message_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建者" prop="createByName">
        <el-input
          v-model="queryParams.createByName"
          placeholder="请输入创建者"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
          style="width: 100%"
          clearable
          v-model="queryParams.createTime"
          type="datetime"
          value-format="YYYY-MM-DD HH:mm:ss"
          placeholder="请选择创建时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="更新者" prop="updateByName">
        <el-input
          v-model="queryParams.updateByName"
          placeholder="请输入更新者"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['ai:chatDetail:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['ai:chatDetail:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['ai:chatDetail:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="Upload"
          @click="upload.open = true"
          v-hasPermi="['ai:chatDetail:import']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['ai:chatDetail:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="chatDetailList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="会话详细id" align="center" prop="chatDetailId" v-if="columns.chatDetailId.visible" />
      <el-table-column label="会话id" align="center" prop="chatConversationId" v-if="columns.chatConversationId.visible" />
      <el-table-column label="消息类型" align="center" prop="messageType" v-if="columns.messageType.visible">
        <template #default="scope">
          <dict-tag :options="ai_message_type" :value="scope.row.messageType"/>
        </template>
      </el-table-column>
      <el-table-column label="内容" align="center" prop="content" v-if="columns.content.visible" />
      <el-table-column label="附件" align="center" prop="attachment" v-if="columns.attachment.visible" />
      <el-table-column label="创建者" align="center" prop="createByName" v-if="columns.createByName.visible" />
      <el-table-column label="创建时间" align="center" prop="createTime" v-if="columns.createTime.visible" />
      <el-table-column label="更新者" align="center" prop="updateByName" v-if="columns.updateByName.visible" />
      <el-table-column label="备注" align="center" prop="remark" v-if="columns.remark.visible" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['ai:chatDetail:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['ai:chatDetail:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改会话详细对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="chatDetailRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="会话id" prop="chatConversationId">
          <el-input v-model="form.chatConversationId" placeholder="请输入会话id" />
        </el-form-item>
        <el-form-item label="消息类型" prop="messageType">
          <el-select v-model="form.messageType" placeholder="请选择消息类型">
            <el-option
              v-for="dict in ai_message_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="附件" prop="attachment">
          <el-input v-model="form.attachment" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 导入会话详细对话框 -->
    <excel-upload
      :title="upload.title"
      :uploadUrl="upload.url"
      :open="upload.open"
      @uploadClose="upload.open = false"
      @handleFileSuccess="getList">
    </excel-upload>
  </div>
</template>

<script setup name="ChatDetail">
import { listChatDetail, getChatDetail, delChatDetail, addChatDetail, updateChatDetail } from "@/api/ai/chatDetail"

const { proxy } = getCurrentInstance()
const { ai_message_type } = proxy.useDict('ai_message_type')

const chatDetailList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

// 导入Excel参数
const upload = ref({
  // 是否显示弹出层
  open: false,
  // 弹出层标题
  title: "会话详细导入",
  // 上传的地址
  url: "/ai/chatDetail/importData"
})

// 显隐列
const columns = reactive({
  chatDetailId: {label: `会话详细id`, visible: true},
  chatConversationId: {label: `会话id`, visible: true},
  messageType: {label: `消息类型`, visible: true},
  content: {label: `内容`, visible: true},
  attachment: {label: `附件`, visible: true},
  createByName: {label: `创建者`, visible: true},
  createTime: {label: `创建时间`, visible: true},
  updateByName: {label: `更新者`, visible: true},
  remark: {label: `备注`, visible: true},
})

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    chatConversationId: null,
    messageType: null,
    content: null,
    attachment: null,
    createByName: null,
    createTime: null,
    updateByName: null,
    remark: null
  },
  rules: {
    chatConversationId: [
      { required: true, message: "会话id不能为空", trigger: "blur" }
    ],
    messageType: [
      { required: true, message: "消息类型不能为空", trigger: "change" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询会话详细列表 */
function getList() {
  loading.value = true
  listChatDetail(queryParams.value).then(response => {
    chatDetailList.value = response.rows
    total.value = Number(response.total)
    loading.value = false
  })
}

// 取消按钮
function cancel() {
  open.value = false
  reset()
}

// 表单重置
function reset() {
  form.value = {
    chatDetailId: null,
    chatConversationId: null,
    messageType: null,
    content: null,
    attachment: null,
    createById: null,
    createByName: null,
    createTime: null,
    updateById: null,
    updateByName: null,
    updateTime: null,
    remark: null
  }
  proxy.resetForm("chatDetailRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.chatDetailId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加会话详细"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _chatDetailId = row.chatDetailId || ids.value
  getChatDetail(_chatDetailId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改会话详细"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["chatDetailRef"].validate(valid => {
    if (valid) {
      if (form.value.chatDetailId != null) {
        updateChatDetail(form.value.chatDetailId, form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addChatDetail(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _chatDetailIds = row.chatDetailId || ids.value
  proxy.$modal.confirm('是否确认删除会话详细编号为"' + _chatDetailIds + '"的数据项？').then(function() {
    return delChatDetail(_chatDetailIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('ai/chatDetail/export', {
    ...queryParams.value
  }, `chatDetail_export_${proxy.parseTime(new Date())}.xlsx`)
}

getList()
</script>
