<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="节点ID" prop="treeId">
        <el-input
          v-model="queryParams.treeId"
          placeholder="请输入节点ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="节点名称" prop="treeName">
        <el-input
          v-model="queryParams.treeName"
          placeholder="请输入节点名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
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
      <el-form-item label="注册日期">
        <el-date-picker
          v-model="daterangeRegisterDate"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="注册时间">
        <el-date-picker
          v-model="datetimerangeRegisterTime"
          style="width: 340px"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="datetimerange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
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
          v-hasPermi="['business:tree:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-sort"
          size="mini"
          @click="toggleExpandAll"
        >展开/折叠</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="upload.open = true"
          v-hasPermi="['business:tree:import']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['business:tree:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="treeList"
      row-key="userId"
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column label="节点ID" prop="treeId" v-if="columns[0].visible" />
      <el-table-column label="节点名称" align="center" prop="treeName" v-if="columns[1].visible" />
      <el-table-column label="用户账号" align="center" prop="userName" v-if="columns[2].visible" />
      <el-table-column label="密码" align="center" prop="password" v-if="columns[3].visible" />
      <el-table-column label="账号状态" align="center" prop="status" v-if="columns[4].visible">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="注册日期" align="center" prop="registerDate" v-if="columns[5].visible">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.registerDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="注册时间" align="center" prop="registerTime" v-if="columns[6].visible" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:tree:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['business:tree:add']"
          >新增</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:tree:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改测试树表生成对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="节点ID" prop="treeId">
          <treeselect v-model="form.treeId" :options="treeOptions" :normalizer="normalizer" placeholder="请选择节点ID" />
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
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="注册日期" prop="registerDate">
          <el-date-picker clearable
            v-model="form.registerDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择注册日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="注册时间" prop="registerTime">
          <el-date-picker clearable
            v-model="form.registerTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择注册时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 导入测试树表生成对话框 -->
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
import { listTree, getTree, delTree, addTree, updateTree } from "@/api/business/tree"
import Treeselect from "@riophae/vue-treeselect"
import "@riophae/vue-treeselect/dist/vue-treeselect.css"

export default {
  name: "Tree",
  dicts: ['sys_normal_disable'],
  components: {
    Treeselect
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 测试树表生成表格数据
      treeList: [],
      // 测试树表生成树选项
      treeOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 注册时间日期范围
      daterangeRegisterDate: [],
      // 注册时间时间范围
      datetimerangeRegisterTime: [],
      // 导入Excel参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "测试树表生成导入",
        // 上传的地址
        url: "/business/tree/importData"
      },
      // 显隐列
      columns: [
          { key: 1, label: `用户ID`, visible: true },
          { key: 2, label: `节点ID`, visible: true },
          { key: 3, label: `节点名称`, visible: true },
          { key: 4, label: `用户账号`, visible: true },
          { key: 5, label: `密码`, visible: true },
          { key: 6, label: `账号状态`, visible: true },
          { key: 7, label: `注册日期`, visible: true },
          { key: 8, label: `注册时间`, visible: true },
      ],
      // 查询参数
      queryParams: {
        treeId: null,
        treeName: null,
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
        treeName: [
          { required: true, message: "节点名称不能为空", trigger: "blur" }
        ],
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
    /** 查询测试树表生成列表 */
    getList() {
      this.loading = true
      this.queryParams.params = {}
      if (null != this.daterangeRegisterDate && '' != this.daterangeRegisterDate) {
        this.queryParams.params["beginRegisterDate"] = this.daterangeRegisterDate[0]
        this.queryParams.params["endRegisterDate"] = this.daterangeRegisterDate[1]
      }
      if (null != this.datetimerangeRegisterTime && '' != this.datetimerangeRegisterTime) {
        this.queryParams.params["beginRegisterTime"] = this.datetimerangeRegisterTime[0]
        this.queryParams.params["endRegisterTime"] = this.datetimerangeRegisterTime[1]
      }
      listTree(this.queryParams).then(response => {
        this.treeList = this.handleTree(response.data, "userId", "treeId")
        this.loading = false
      })
    },
    /** 转换测试树表生成数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: node.userId,
        label: node.treeName,
        children: node.children
      }
    },
	/** 查询测试树表生成下拉树结构 */
    getTreeselect() {
      listTree().then(response => {
        this.treeOptions = []
        const data = { userId: 0, treeName: '顶级节点', children: [] }
        data.children = this.handleTree(response.data, "userId", "treeId")
        this.treeOptions.push(data)
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
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeRegisterDate = []
      this.datetimerangeRegisterTime = []
      this.resetForm("queryForm")
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset()
      this.getTreeselect()
      if (row != null && row.userId) {
        this.form.treeId = row.userId
      } else {
        this.form.treeId = 0
      }
      this.open = true
      this.title = "添加测试树表生成"
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.refreshTable = false
      this.isExpandAll = !this.isExpandAll
      this.$nextTick(() => {
        this.refreshTable = true
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.getTreeselect()
      if (row != null) {
        this.form.treeId = row.treeId
      }
      getTree(row.userId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改测试树表生成"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.userId != null) {
            updateTree(form.value.userId, this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addTree(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除测试树表生成编号为"' + row.userId + '"的数据项？').then(function() {
        return delTree(row.userId)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('business/tree/export', {
        ...this.queryParams
      }, `tree_export_${this.parseTime(new Date())}.xlsx`)
    }
  }
}
</script>
