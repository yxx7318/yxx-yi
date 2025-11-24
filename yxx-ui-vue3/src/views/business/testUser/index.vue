<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px" class="el-form-flex">
      <el-form-item label="主表ID" prop="parentId">
        <el-input
          v-model="queryParams.parentId"
          placeholder="请输入主表ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户账号" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入用户账号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input
          v-model="queryParams.password"
          placeholder="请输入密码"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账号状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择账号状态" clearable>
          <el-option
            v-for="dict in sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="注册日期">
        <el-date-picker
          style="width: 100%"
          v-model="daterangeRegisterDate"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="注册时间">
        <el-date-picker
          style="width: 100%"
          v-model="datetimerangeRegisterTime"
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
          v-hasPermi="['business:testUser:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['business:testUser:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:testUser:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="Upload"
          @click="upload.open = true"
          v-hasPermi="['business:testUser:import']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['business:testUser:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="testUserList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="用户ID" align="center" prop="userId" v-if="columns.userId.visible" />
      <el-table-column label="主表ID" align="center" prop="parentId" v-if="columns.parentId.visible" />
      <el-table-column label="用户账号" align="center" prop="userName" v-if="columns.userName.visible" />
      <el-table-column label="密码" align="center" prop="password" v-if="columns.password.visible" />
      <el-table-column label="账号状态" align="center" prop="status" v-if="columns.status.visible">
        <template #default="scope">
          <dict-tag :options="sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="注册日期" align="center" prop="registerDate" width="180" v-if="columns.registerDate.visible">
        <template #default="scope">
          <span>{{ parseTime(scope.row.registerDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="注册时间" align="center" prop="registerTime" v-if="columns.registerTime.visible" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['business:testUser:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['business:testUser:remove']">删除</el-button>
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

    <!-- 添加或修改测试单表生成对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="testUserRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="主表ID" prop="parentId">
          <el-input v-model="form.parentId" placeholder="请输入主表ID" />
        </el-form-item>
        <el-form-item label="用户账号" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入用户账号" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="账号状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="注册日期" prop="registerDate">
          <el-date-picker
            clearable
            v-model="form.registerDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择注册日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="注册时间" prop="registerTime">
          <el-date-picker
            clearable
            v-model="form.registerTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择注册时间">
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

    <!-- 导入测试单表生成对话框 -->
    <excel-upload
      :title="upload.title"
      :uploadUrl="upload.url"
      :open="upload.open"
      @uploadClose="upload.open = false"
      @handleFileSuccess="getList">
    </excel-upload>
  </div>
</template>

<script setup name="TestUser">
import { listTestUser, getTestUser, delTestUser, addTestUser, updateTestUser } from "@/api/business/testUser"

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict('sys_normal_disable')

const testUserList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
// 注册时间日期范围
const daterangeRegisterDate = ref([])
// 注册时间时间范围
const datetimerangeRegisterTime = ref([])
// 注册时间时间范围
const datetimerangeCreateTime = ref([])
// 注册时间时间范围
const datetimerangeUpdateTime = ref([])

// 导入Excel参数
const upload = ref({
  // 是否显示弹出层
  open: false,
  // 弹出层标题
  title: "测试单表生成导入",
  // 上传的地址
  url: "/business/testUser/importData"
})

// 显隐列
const columns = reactive({
  userId: {label: `用户ID`, visible: true},
  parentId: {label: `主表ID`, visible: true},
  userName: {label: `用户账号`, visible: true},
  password: {label: `密码`, visible: true},
  status: {label: `账号状态`, visible: true},
  registerDate: {label: `注册日期`, visible: true},
  registerTime: {label: `注册时间`, visible: true},
})

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    parentId: null,
    userName: null,
    password: null,
    status: null,
    registerDate: null,
    registerTime: null,
  },
  rules: {
    userName: [
      { required: true, message: "用户账号不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询测试单表生成列表 */
function getList() {
  loading.value = true
  queryParams.value.params = {}
  if (null != daterangeRegisterDate && '' != daterangeRegisterDate) {
    queryParams.value.params["beginRegisterDate"] = daterangeRegisterDate.value[0]
    queryParams.value.params["endRegisterDate"] = daterangeRegisterDate.value[1]
  }
  if (null != datetimerangeRegisterTime && '' != datetimerangeRegisterTime) {
    queryParams.value.params["beginRegisterTime"] = datetimerangeRegisterTime.value[0]
    queryParams.value.params["endRegisterTime"] = datetimerangeRegisterTime.value[1]
  }
  if (null != datetimerangeCreateTime && '' != datetimerangeCreateTime) {
    queryParams.value.params["beginCreateTime"] = datetimerangeCreateTime.value[0]
    queryParams.value.params["endCreateTime"] = datetimerangeCreateTime.value[1]
  }
  if (null != datetimerangeUpdateTime && '' != datetimerangeUpdateTime) {
    queryParams.value.params["beginUpdateTime"] = datetimerangeUpdateTime.value[0]
    queryParams.value.params["endUpdateTime"] = datetimerangeUpdateTime.value[1]
  }
  listTestUser(queryParams.value).then(response => {
    testUserList.value = response.rows
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
    userId: null,
    parentId: null,
    userName: null,
    password: null,
    status: null,
    registerDate: null,
    registerTime: null,
    createById: null,
    createByName: null,
    createTime: null,
    updateById: null,
    updateByName: null,
    updateTime: null,
    remark: null
  }
  proxy.resetForm("testUserRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeRegisterDate.value = []
  datetimerangeRegisterTime.value = []
  datetimerangeCreateTime.value = []
  datetimerangeUpdateTime.value = []
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.userId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加测试单表生成"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _userId = row.userId || ids.value
  getTestUser(_userId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改测试单表生成"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["testUserRef"].validate(valid => {
    if (valid) {
      if (form.value.userId != null) {
        updateTestUser(form.value.userId, form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addTestUser(form.value).then(response => {
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
  const _userIds = row.userId || ids.value
  proxy.$modal.confirm('是否确认删除测试单表生成编号为"' + _userIds + '"的数据项？').then(function() {
    return delTestUser(_userIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('business/testUser/export', {
    ...queryParams.value
  }, `testUser_export_${proxy.parseTime(new Date())}.xlsx`)
}

getList()
</script>
