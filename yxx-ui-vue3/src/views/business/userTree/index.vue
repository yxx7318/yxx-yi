<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px" class="el-form-flex">
      <el-form-item label="节点ID" prop="treeId">
        <el-input
          v-model="queryParams.treeId"
          placeholder="请输入节点ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="节点名称" prop="treeName">
        <el-input
          v-model="queryParams.treeName"
          placeholder="请输入节点名称"
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
      <el-form-item label="注册时间" style="width: 308px">
        <el-date-picker
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
          v-hasPermi="['business:userTree:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="Sort"
          @click="toggleExpandAll"
        >展开/折叠</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="Upload"
          @click="upload.open = true"
          v-hasPermi="['business:userTree:import']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['business:userTree:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="userTreeList"
      row-key="userId"
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column label="节点ID" prop="treeId" v-if="columns.treeId.visible" />
      <el-table-column label="节点名称" align="center" prop="treeName" v-if="columns.treeName.visible" />
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
      <el-table-column label="注册时间" align="center" prop="registerTime" width="240" v-if="columns.registerTime.visible" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['business:userTree:edit']">修改</el-button>
          <el-button link type="primary" icon="Plus" @click="handleAdd(scope.row)" v-hasPermi="['business:userTree:add']">新增</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['business:userTree:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改测试树表生成对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="userTreeRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="节点ID" prop="treeId">
          <el-tree-select
            v-model="form.treeId"
            :data="userTreeOptions"
            :props="{ value: 'userId', label: 'treeName', children: 'children' }"
            value-key="userId"
            placeholder="请选择节点ID"
            check-strictly
          />
        </el-form-item>
        <el-form-item label="节点名称" prop="treeName">
          <el-input v-model="form.treeName" placeholder="请输入节点名称" />
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
              :value="dict.value"
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
  </div>
</template>

<script setup name="UserTree">
import { listUserTree, getUserTree, delUserTree, addUserTree, updateUserTree } from "@/api/business/userTree"

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict('sys_normal_disable')

const userTreeList = ref([])
const userTreeOptions = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const title = ref("")
const isExpandAll = ref(true)
const refreshTable = ref(true)
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
  title: "测试树表生成导入",
  // 上传的地址
  url: "/business/userTree/importData"
})

// 显隐列
const columns = reactive({
  userId: {label: `用户ID`, visible: true},
  treeId: {label: `节点ID`, visible: true},
  treeName: {label: `节点名称`, visible: true},
  userName: {label: `用户账号`, visible: true},
  password: {label: `密码`, visible: true},
  status: {label: `账号状态`, visible: true},
  registerDate: {label: `注册日期`, visible: true},
  registerTime: {label: `注册时间`, visible: true},
})

const data = reactive({
  form: {},
  queryParams: {
    treeId: null,
    treeName: null,
    userName: null,
    password: null,
    status: null,
    registerDate: null,
    registerTime: null,
  },
  rules: {
    treeName: [
      { required: true, message: "节点名称不能为空", trigger: "blur" }
    ],
    userName: [
      { required: true, message: "用户账号不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询测试树表生成列表 */
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
  listUserTree(queryParams.value).then(response => {
    userTreeList.value = proxy.handleTree(response.data, "userId", "treeId")
    loading.value = false
  })
}

/** 查询测试树表生成下拉树结构 */
function getTreeSelect() {
  listUserTree().then(response => {
    userTreeOptions.value = []
    const data = { userId: 0, treeName: '顶级节点', children: [] }
    data.children = proxy.handleTree(response.data, "userId", "treeId")
    userTreeOptions.value.push(data)
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
    treeId: null,
    treeName: null,
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
  proxy.resetForm("userTreeRef")
}

/** 搜索按钮操作 */
function handleQuery() {
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

/** 新增按钮操作 */
function handleAdd(row) {
  reset()
  getTreeSelect()
  if (row != null && row.userId) {
    form.value.treeId = row.userId
  } else {
    form.value.treeId = 0
  }
  open.value = true
  title.value = "添加测试树表生成"
}

/** 展开/折叠操作 */
function toggleExpandAll() {
  refreshTable.value = false
  isExpandAll.value = !isExpandAll.value
  nextTick(() => {
    refreshTable.value = true
  })
}

/** 修改按钮操作 */
async function handleUpdate(row) {
  reset()
  await getTreeSelect()
  if (row != null) {
    form.value.treeId = row.treeId
  }
  getUserTree(row.userId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改测试树表生成"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["userTreeRef"].validate(valid => {
    if (valid) {
      if (form.value.userId != null) {
        updateUserTree(form.value.userId, form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addUserTree(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除测试树表生成编号为"' + row.userId + '"的数据项？').then(function() {
    return delUserTree(row.userId)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('business/userTree/export', {
    ...queryParams.value
  }, `userTree_export_${proxy.parseTime(new Date())}.xlsx`)
}

getList()
</script>
