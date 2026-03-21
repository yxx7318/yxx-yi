<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px" class="el-form-flex">
      <el-form-item label="会话分组" prop="chatGroup">
        <el-input
          v-model="queryParams.chatGroup"
          placeholder="请输入会话分组"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="会话标题" prop="chatTitle">
        <el-input
          v-model="queryParams.chatTitle"
          placeholder="请输入会话标题"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建者" prop="createByName">
        <el-input
          v-model="queryParams.createByName"
          placeholder="请输入创建者"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          style="width: 100%"
          v-model="datetimerangeCreateTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="datetimerange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
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
          v-hasPermi="['ai:chatConversation:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['ai:chatConversation:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['ai:chatConversation:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="Upload"
          @click="upload.open = true"
          v-hasPermi="['ai:chatConversation:import']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['ai:chatConversation:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="chatConversationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="会话id" align="center" prop="chatConversationId" v-if="columns.chatConversationId.visible" />
      <el-table-column label="会话分组" align="center" prop="chatGroup" v-if="columns.chatGroup.visible" />
      <el-table-column label="会话标题" align="center" prop="chatTitle" v-if="columns.chatTitle.visible" />
      <el-table-column label="创建者" align="center" prop="createByName" v-if="columns.createByName.visible" />
      <el-table-column label="创建时间" align="center" prop="createTime" v-if="columns.createTime.visible" />
      <el-table-column label="更新者" align="center" prop="updateByName" v-if="columns.updateByName.visible" />
      <el-table-column label="备注" align="center" prop="remark" v-if="columns.remark.visible" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['ai:chatConversation:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['ai:chatConversation:remove']">删除</el-button>
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

    <!-- 添加或修改会话对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="chatConversationRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="会话分组" prop="chatGroup">
          <el-input v-model="form.chatGroup" placeholder="请输入会话分组" />
        </el-form-item>
        <el-form-item label="会话标题" prop="chatTitle">
          <el-input v-model="form.chatTitle" placeholder="请输入会话标题" />
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

    <!-- 导入会话对话框 -->
    <excel-upload
      :title="upload.title"
      :uploadUrl="upload.url"
      :open="upload.open"
      @uploadClose="upload.open = false"
      @handleFileSuccess="getList">
    </excel-upload>
  </div>
</template>

<script setup name="ChatConversation">
import { listChatConversation, getChatConversation, delChatConversation, addChatConversation, updateChatConversation } from "@/api/ai/chatConversation"

const { proxy } = getCurrentInstance()

const chatConversationList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
// 备注时间范围
const datetimerangeCreateTime = ref([])
// 备注时间范围
const datetimerangeUpdateTime = ref([])

// 导入Excel参数
const upload = ref({
  // 是否显示弹出层
  open: false,
  // 弹出层标题
  title: "会话导入",
  // 上传的地址
  url: "/ai/chatConversation/importData"
})

// 显隐列
const columns = reactive({
  chatConversationId: {label: `会话id`, visible: true},
  chatGroup: {label: `会话分组`, visible: true},
  chatTitle: {label: `会话标题`, visible: true},
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
    chatGroup: null,
    chatTitle: null,
    createByName: null,
    createTime: null,
    updateByName: null,
    remark: null
  },
  rules: {
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询会话列表 */
function getList() {
  loading.value = true
  queryParams.value.params = {}
  if (null != datetimerangeCreateTime && '' != datetimerangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = datetimerangeCreateTime.value[0]
    queryParams.value.params["endCreateTime"] = datetimerangeCreateTime.value[1]
  }
  if (null != datetimerangeUpdateTime && '' != datetimerangeUpdateTime) {
    queryParams.value.params["beginUpdateTime"] = datetimerangeUpdateTime.value[0]
    queryParams.value.params["endUpdateTime"] = datetimerangeUpdateTime.value[1]
  }
  listChatConversation(queryParams.value).then(response => {
    chatConversationList.value = response.rows
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
    chatConversationId: null,
    chatGroup: null,
    chatTitle: null,
    createById: null,
    createByName: null,
    createTime: null,
    updateById: null,
    updateByName: null,
    updateTime: null,
    remark: null
  }
  proxy.resetForm("chatConversationRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  datetimerangeCreateTime.value = []
  datetimerangeUpdateTime.value = []
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.chatConversationId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加会话"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _chatConversationId = row.chatConversationId || ids.value
  getChatConversation(_chatConversationId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改会话"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["chatConversationRef"].validate(valid => {
    if (valid) {
      if (form.value.chatConversationId != null) {
        updateChatConversation(form.value.chatConversationId, form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addChatConversation(form.value).then(response => {
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
  const _chatConversationIds = row.chatConversationId || ids.value
  proxy.$modal.confirm('是否确认删除会话编号为"' + _chatConversationIds + '"的数据项？').then(function() {
    return delChatConversation(_chatConversationIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('ai/chatConversation/export', {
    ...queryParams.value
  }, `chatConversation_export_${proxy.parseTime(new Date())}.xlsx`)
}

getList()
</script>
