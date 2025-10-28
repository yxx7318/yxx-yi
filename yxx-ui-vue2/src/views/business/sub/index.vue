<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户账号" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入用户账号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input
          v-model="queryParams.password"
          placeholder="请输入密码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账号状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择账号状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="注册日期" prop="registerDate">
        <el-date-picker
          clearable
          v-model="queryParams.registerDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择注册日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="注册时间" prop="registerTime">
        <el-date-picker
          clearable
          v-model="queryParams.registerTime"
          type="datetime"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="请选择注册时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['business:sub:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['business:sub:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:sub:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="upload.open = true"
          v-hasPermi="['business:sub:import']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['business:sub:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="subList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主表ID" align="center" prop="subId" v-if="columns.subId.visible" />
      <el-table-column label="用户账号" align="center" prop="userName" v-if="columns.userName.visible" />
      <el-table-column label="密码" align="center" prop="password" v-if="columns.password.visible" />
      <el-table-column label="账号状态" align="center" prop="status" v-if="columns.status.visible">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="注册日期" align="center" prop="registerDate" v-if="columns.registerDate.visible">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.registerDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="注册时间" align="center" prop="registerTime" v-if="columns.registerTime.visible" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:sub:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:sub:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改测试主表生成对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户账号" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入用户账号" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="账号状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
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
            value-format="yyyy-MM-dd"
            placeholder="请选择注册日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="注册时间" prop="registerTime">
          <el-date-picker
            clearable
            v-model="form.registerTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择注册时间">
          </el-date-picker>
        </el-form-item>
        <el-divider content-position="center">测试单表生成信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddTbTestUser">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteTbTestUser">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="tbTestUserList" :row-class-name="rowTbTestUserIndex" @selection-change="handleTbTestUserSelectionChange" ref="tbTestUser">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="用户账号" prop="userName" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.userName" placeholder="请输入用户账号" />
            </template>
          </el-table-column>
          <el-table-column label="密码" prop="password" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.password" placeholder="请输入密码" />
            </template>
          </el-table-column>
          <el-table-column label="账号状态" prop="status" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.status" placeholder="请选择账号状态">
                <el-option
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="注册日期" prop="registerDate" width="180">
            <template slot-scope="scope">
              <el-date-picker clearable v-model="scope.row.registerDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择注册日期" />
            </template>
          </el-table-column>
          <el-table-column label="注册时间" prop="registerTime" width="240">
            <template slot-scope="scope">
              <el-date-picker clearable v-model="scope.row.registerTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择注册时间" />
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
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

<script>
import { listSub, getSub, addSub, updateSub, delSub } from "@/api/business/sub"

export default {
  name: "Sub",
  dicts: ['sys_normal_disable'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedTbTestUser: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 测试主表生成表格数据
      subList: [],
      // 测试单表生成表格数据
      tbTestUserList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 导入Excel参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "测试主表生成导入",
        // 上传的地址
        url: "/business/sub/importData"
      },
      // 显隐列
      columns: {
        subId: { label: `主表ID`, visible: true },
        userName: { label: `用户账号`, visible: true },
        password: { label: `密码`, visible: true },
        status: { label: `账号状态`, visible: true },
        registerDate: { label: `注册日期`, visible: true },
        registerTime: { label: `注册时间`, visible: true },
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: null,
        password: null,
        status: null,
        registerDate: null,
        registerTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userName: [
          { required: true, message: "用户账号不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询测试主表生成列表 */
    getList() {
      this.loading = true
      listSub(this.queryParams).then(response => {
        this.subList = response.rows
        this.total = Number(response.total)
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
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
      this.tbTestUserList = []
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.subId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加测试主表生成"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const subId = row.subId || this.ids
      getSub(subId).then(response => {
        this.form = response.data
        this.tbTestUserList = response.data.tbTestUserList
        this.open = true
        this.title = "修改测试主表生成"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.tbTestUserList = this.tbTestUserList
          if (this.form.subId != null) {
            updateSub(this.form.subId, this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addSub(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const subIds = row.subId || this.ids
      this.$modal.confirm('是否确认删除测试主表生成编号为"' + subIds + '"的数据项？').then(function() {
        return delSub(subIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 测试单表生成序号 */
    rowTbTestUserIndex({ row, rowIndex }) {
      row.index = rowIndex + 1
    },
    /** 测试单表生成添加按钮操作 */
    handleAddTbTestUser() {
      let obj = {}
      obj.userName = ""
      obj.password = ""
      obj.status = ""
      obj.registerDate = ""
      obj.registerTime = ""
      this.tbTestUserList.push(obj)
    },
    /** 测试单表生成删除按钮操作 */
    handleDeleteTbTestUser() {
      if (this.checkedTbTestUser.length == 0) {
        this.$modal.msgError("请先选择要删除的测试单表生成数据")
      } else {
        const tbTestUserList = this.tbTestUserList
        const checkedTbTestUser = this.checkedTbTestUser
        this.tbTestUserList = tbTestUserList.filter(function(item) {
          return checkedTbTestUser.indexOf(item.index) == -1
        })
      }
    },
    /** 复选框选中数据 */
    handleTbTestUserSelectionChange(selection) {
      this.checkedTbTestUser = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('business/sub/export', {
        ...this.queryParams
      }, `sub_export_${this.parseTime(new Date())}.xlsx`)
    }
  }
}
</script>
