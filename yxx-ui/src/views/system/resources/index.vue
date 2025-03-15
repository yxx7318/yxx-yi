<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="资源名称" prop="resourceName">
        <el-input
          v-model="queryParams.resourceName"
          placeholder="请输入资源名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="资源编码" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入资源编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="资源驱动" prop="driverClassname">
        <el-input
          v-model="queryParams.driverClassname"
          placeholder="请输入资源驱动"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="资源状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择资源状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          v-hasPermi="['system:resources:add']"
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
          v-hasPermi="['system:resources:edit']"
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
          v-hasPermi="['system:resources:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:resources:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="upload.open = true"
          v-hasPermi="['temu:goods:import']"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="resourcesList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="资源id" align="center" prop="resourceId" />
      <el-table-column label="资源名称" align="center" prop="resourceName" />
      <el-table-column label="资源编码" align="center" prop="code" />
      <el-table-column label="资源驱动" align="center" prop="driverClassname" />
      <el-table-column label="资源状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:resources:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:resources:remove']"
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

    <!-- 添加或修改数据源管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="资源名称" prop="resourceName">
          <el-input v-model="form.resourceName" placeholder="请输入资源名称" />
        </el-form-item>
        <el-form-item label="资源编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入资源编码" />
        </el-form-item>
        <el-form-item label="资源驱动" prop="driverClassname">
          <el-input v-model="form.driverClassname" placeholder="请输入资源驱动" />
        </el-form-item>
        <el-form-item label="资源地址" prop="url">
          <el-input v-model="form.url" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="资源用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入资源用户名" />
        </el-form-item>
        <el-form-item label="资源密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入资源密码" />
        </el-form-item>
        <el-form-item label="资源状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 导入数据源管理对话框 -->
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
import { listResources, getResources, addResources, updateResources, delResources } from "@/api/system/resources";

export default {
  name: "Resources",
  dicts: ['sys_normal_disable'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 数据源管理表格数据
      resourcesList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 商品导入参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 弹出层标题
        title: "数据源管理导入",
        // 上传的地址
        url: "/system/resources/importData"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        resourceName: null,
        code: null,
        driverClassname: null,
        url: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        resourceName: [
          { required: true, message: "资源名称不能为空", trigger: "blur" }
        ],
        code: [
          { required: true, message: "资源编码不能为空", trigger: "blur" }
        ],
        driverClassname: [
          { required: true, message: "资源驱动不能为空", trigger: "blur" }
        ],
        url: [
          { required: true, message: "资源地址不能为空", trigger: "blur" }
        ],
        username: [
          { required: true, message: "资源用户名不能为空", trigger: "blur" }
        ],
        password: [
          { required: true, message: "资源密码不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "资源状态不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询数据源管理列表 */
    getList() {
      this.loading = true;
      listResources(this.queryParams).then(response => {
        this.resourcesList = response.rows;
        this.total = Number(response.total);
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        resourceId: null,
        resourceName: null,
        code: null,
        driverClassname: null,
        url: null,
        username: null,
        password: null,
        status: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.resourceId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加数据源管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const resourceId = row.resourceId || this.ids
      getResources(resourceId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改数据源管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.resourceId != null) {
            updateResources(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addResources(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const resourceIds = row.resourceId || this.ids;
      this.$modal.confirm('是否确认删除数据源管理编号为"' + resourceIds + '"的数据项？').then(function() {
        return delResources(resourceIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/resources/export', {
        ...this.queryParams
      }, `resources_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
