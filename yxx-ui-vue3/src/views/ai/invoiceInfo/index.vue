<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px" class="el-form-flex">
      <el-form-item label="发票号码" prop="invoiceNumber">
        <el-input
          v-model="queryParams.invoiceNumber"
          placeholder="请输入发票号码"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发票金额" prop="amount">
        <el-input
          v-model="queryParams.amount"
          placeholder="请输入发票金额"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开票日期">
        <el-date-picker
          style="width: 100%"
          v-model="datetimerangeInvoiceDate"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="datetimerange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
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
          v-hasPermi="['ai:invoiceInfo:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['ai:invoiceInfo:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['ai:invoiceInfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="Upload"
          @click="upload.open = true"
          v-hasPermi="['ai:invoiceInfo:import']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['ai:invoiceInfo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="invoiceInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="发票id" align="center" prop="invoiceInfoId" v-if="columns.invoiceInfoId.visible" />
      <el-table-column label="发票号码" align="center" prop="invoiceNumber" v-if="columns.invoiceNumber.visible" />
      <el-table-column label="发票金额" align="center" prop="amount" v-if="columns.amount.visible" />
      <el-table-column label="开票日期" align="center" prop="invoiceDate" v-if="columns.invoiceDate.visible" />
      <el-table-column label="创建者" align="center" prop="createByName" v-if="columns.createByName.visible" />
      <el-table-column label="创建时间" align="center" prop="createTime" v-if="columns.createTime.visible" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['ai:invoiceInfo:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['ai:invoiceInfo:remove']">删除</el-button>
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

    <!-- 添加或修改发票信息对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="invoiceInfoRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="发票号码" prop="invoiceNumber">
          <el-input v-model="form.invoiceNumber" placeholder="请输入发票号码" />
        </el-form-item>
        <el-form-item label="发票金额" prop="amount">
          <el-input v-model="form.amount" placeholder="请输入发票金额" />
        </el-form-item>
        <el-form-item label="开票日期" prop="invoiceDate">
          <el-date-picker
            clearable
            v-model="form.invoiceDate"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择开票日期">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 导入发票信息对话框 -->
    <excel-upload
      :title="upload.title"
      :uploadUrl="upload.url"
      :open="upload.open"
      @uploadClose="upload.open = false"
      @handleFileSuccess="getList">
    </excel-upload>
  </div>
</template>

<script setup name="InvoiceInfo">
import { listInvoiceInfo, getInvoiceInfo, delInvoiceInfo, addInvoiceInfo, updateInvoiceInfo } from "@/api/ai/invoiceInfo"

const { proxy } = getCurrentInstance()

const invoiceInfoList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
// 开票日期时间范围
const datetimerangeInvoiceDate = ref([])
// 开票日期时间范围
const datetimerangeCreateTime = ref([])

// 导入Excel参数
const upload = ref({
  // 是否显示弹出层
  open: false,
  // 弹出层标题
  title: "发票信息导入",
  // 上传的地址
  url: "/ai/invoiceInfo/importData"
})

// 显隐列
const columns = reactive({
  invoiceInfoId: {label: `发票id`, visible: true},
  invoiceNumber: {label: `发票号码`, visible: true},
  amount: {label: `发票金额`, visible: true},
  invoiceDate: {label: `开票日期`, visible: true},
  createByName: {label: `创建者`, visible: true},
  createTime: {label: `创建时间`, visible: true},
})

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    invoiceNumber: null,
    amount: null,
    invoiceDate: null,
    createByName: null,
    createTime: null,
  },
  rules: {
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询发票信息列表 */
function getList() {
  loading.value = true
  queryParams.value.params = {}
  if (null != datetimerangeInvoiceDate && '' != datetimerangeInvoiceDate) {
    queryParams.value.params["beginInvoiceDate"] = datetimerangeInvoiceDate.value[0]
    queryParams.value.params["endInvoiceDate"] = datetimerangeInvoiceDate.value[1]
  }
  if (null != datetimerangeCreateTime && '' != datetimerangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = datetimerangeCreateTime.value[0]
    queryParams.value.params["endCreateTime"] = datetimerangeCreateTime.value[1]
  }
  listInvoiceInfo(queryParams.value).then(response => {
    invoiceInfoList.value = response.rows
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
    invoiceInfoId: null,
    invoiceNumber: null,
    amount: null,
    invoiceDate: null,
    createById: null,
    createByName: null,
    createTime: null,
    updateById: null,
    updateByName: null,
    updateTime: null,
    remark: null
  }
  proxy.resetForm("invoiceInfoRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  datetimerangeInvoiceDate.value = []
  datetimerangeCreateTime.value = []
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.invoiceInfoId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加发票信息"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _invoiceInfoId = row.invoiceInfoId || ids.value
  getInvoiceInfo(_invoiceInfoId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改发票信息"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["invoiceInfoRef"].validate(valid => {
    if (valid) {
      if (form.value.invoiceInfoId != null) {
        updateInvoiceInfo(form.value.invoiceInfoId, form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addInvoiceInfo(form.value).then(response => {
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
  const _invoiceInfoIds = row.invoiceInfoId || ids.value
  proxy.$modal.confirm('是否确认删除发票信息编号为"' + _invoiceInfoIds + '"的数据项？').then(function() {
    return delInvoiceInfo(_invoiceInfoIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('ai/invoiceInfo/export', {
    ...queryParams.value
  }, `invoiceInfo_export_${proxy.parseTime(new Date())}.xlsx`)
}

getList()
</script>
