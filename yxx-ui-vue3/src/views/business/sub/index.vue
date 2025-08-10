<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
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
        <el-select v-model="queryParams.status" placeholder="请选择账号状态" clearable style="width: 180px">
          <el-option
            v-for="dict in sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="注册日期" prop="registerDate">
        <el-date-picker clearable
          v-model="queryParams.registerDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择注册日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="注册时间" prop="registerTime">
        <el-date-picker clearable
          v-model="queryParams.registerTime"
          type="datetime"
          value-format="YYYY-MM-DD HH:mm:ss"
          placeholder="请选择注册时间">
        </el-date-picker>
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
          v-hasPermi="['business:sub:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['business:sub:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:sub:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="Upload"
          @click="upload.open = true"
          v-hasPermi="['business:sub:import']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['business:sub:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="subList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主表ID" align="center" prop="subId" v-if="columns[0].visible" />
      <el-table-column label="用户账号" align="center" prop="userName" v-if="columns[1].visible" />
      <el-table-column label="密码" align="center" prop="password" v-if="columns[2].visible" />
      <el-table-column label="账号状态" align="center" prop="status" v-if="columns[3].visible">
        <template #default="scope">
          <dict-tag :options="sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="注册日期" align="center" prop="registerDate" width="180" v-if="columns[4].visible">
        <template #default="scope">
          <span>{{ parseTime(scope.row.registerDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="注册时间" align="center" prop="registerTime" v-if="columns[5].visible" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['business:sub:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['business:sub:remove']">删除</el-button>
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

    <!-- 添加或修改测试主表生成对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="subRef" :model="form" :rules="rules" label-width="80px">
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
          <el-date-picker clearable
            v-model="form.registerDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择注册日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="注册时间" prop="registerTime">
          <el-date-picker clearable
            v-model="form.registerTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择注册时间">
          </el-date-picker>
        </el-form-item>
        <el-divider content-position="center">测试单表生成信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="Plus" @click="handleAddTbTestUser">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="Delete" @click="handleDeleteTbTestUser">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="tbTestUserList" :row-class-name="rowTbTestUserIndex" @selection-change="handleTbTestUserSelectionChange" ref="tbTestUser">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="用户账号" prop="userName" width="150">
            <template #default="scope">
              <el-input v-model="scope.row.userName" placeholder="请输入用户账号" />
            </template>
          </el-table-column>
          <el-table-column label="密码" prop="password" width="150">
            <template #default="scope">
              <el-input v-model="scope.row.password" placeholder="请输入密码" />
            </template>
          </el-table-column>
          <el-table-column label="账号状态" prop="status" width="150">
            <template #default="scope">
              <el-select v-model="scope.row.status" placeholder="请选择账号状态">
                <el-option
                  v-for="dict in sys_normal_disable"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="注册日期" prop="registerDate" width="240">
            <template #default="scope">
              <el-date-picker clearable v-model="scope.row.registerDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择注册日期">
              </el-date-picker>
            </template>
          </el-table-column>
          <el-table-column label="注册时间" prop="registerTime" width="240">
            <template #default="scope">
              <el-date-picker clearable v-model="scope.row.registerTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择注册时间">
              </el-date-picker>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 导入测试主表生成对话框 -->
    <excel-upload
      :title="upload.title"
      :uploadUrl="upload.url"
      :open="upload.open"
      @uploadClose="upload.open = false"
      @handleFileSuccess="getList">
    </excel-upload>
  </div>
</template>

<script setup name="Sub">
import { listSub, getSub, delSub, addSub, updateSub } from "@/api/business/sub"

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict('sys_normal_disable')

const subList = ref([])
const tbTestUserList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const checkedTbTestUser = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

// 导入Excel参数
const upload = ref({
  // 是否显示弹出层
  open: false,
  // 弹出层标题
  title: "测试主表生成导入",
  // 上传的地址
  url: "/business/sub/importData"
})

// 显隐列
const columns = reactive([
  { key: 1, label: `主表ID`, visible: true },
  { key: 2, label: `用户账号`, visible: true },
  { key: 3, label: `密码`, visible: true },
  { key: 4, label: `账号状态`, visible: true },
  { key: 5, label: `注册日期`, visible: true },
  { key: 6, label: `注册时间`, visible: true },
])

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
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

/** 查询测试主表生成列表 */
function getList() {
  loading.value = true
  listSub(queryParams.value).then(response => {
    subList.value = response.rows
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
    subId: null,
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
  tbTestUserList.value = []
  proxy.resetForm("subRef")
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
  ids.value = selection.map(item => item.subId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加测试主表生成"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _subId = row.subId || ids.value
  getSub(_subId).then(response => {
    form.value = response.data
    tbTestUserList.value = response.data.tbTestUserList
    open.value = true
    title.value = "修改测试主表生成"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["subRef"].validate(valid => {
    if (valid) {
      form.value.tbTestUserList = tbTestUserList.value
      if (form.value.subId != null) {
        updateSub(form.value.subId, form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addSub(form.value).then(response => {
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
  const _subIds = row.subId || ids.value
  proxy.$modal.confirm('是否确认删除测试主表生成编号为"' + _subIds + '"的数据项？').then(function() {
    return delSub(_subIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 测试单表生成序号 */
function rowTbTestUserIndex({ row, rowIndex }) {
  row.index = rowIndex + 1
}

/** 测试单表生成添加按钮操作 */
function handleAddTbTestUser() {
  let obj = {}
  obj.userName = ""
  obj.password = ""
  obj.status = ""
  obj.registerDate = ""
  obj.registerTime = ""
  tbTestUserList.value.push(obj)
}

/** 测试单表生成删除按钮操作 */
function handleDeleteTbTestUser() {
  if (checkedTbTestUser.value.length == 0) {
    proxy.$modal.msgError("请先选择要删除的测试单表生成数据")
  } else {
    const tbTestUsers = tbTestUserList.value
    const checkedTbTestUsers = checkedTbTestUser.value
    tbTestUserList.value = tbTestUsers.filter(function(item) {
      return checkedTbTestUsers.indexOf(item.index) == -1
    })
  }
}

/** 复选框选中数据 */
function handleTbTestUserSelectionChange(selection) {
  checkedTbTestUser.value = selection.map(item => item.index)
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('business/sub/export', {
    ...queryParams.value
  }, `sub_export_${proxy.parseTime(new Date())}.xlsx`)
}

getList()
</script>
