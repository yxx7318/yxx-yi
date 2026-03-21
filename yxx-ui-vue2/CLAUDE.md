# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
# Install dependencies
npm install --registry=https://registry.npmmirror.com

# Development server (port 83, proxies to http://localhost:7318)
npm run dev

# Build for test
npm run build:test

# Build for production
npm run build:prod

# Preview production build
npm run preview
```

## Project Structure

Vue 2 + Element UI admin dashboard template (RuoYi-based):

```
yxx-ui-vue2/
├── src/
│   ├── api/              # Backend API modules (by domain: business, system, monitor, tool)
│   ├── assets/           # Static assets (styles, icons, images)
│   ├── components/       # Reusable Vue components
│   │   ├── DictData/     # Dictionary data component
│   │   ├── DictTag/      # Dictionary tag display
│   │   ├── Editor/       # Rich text editor
│   │   ├── FileUpload/   # File upload component
│   │   ├── ImageUpload/  # Image upload component
│   │   ├── Pagination/   # Pagination component
│   │   └── ...
│   ├── config/           # App configuration (permission, settings)
│   ├── directive/        # Custom Vue directives (hasPermi, hasRole, clipboard)
│   ├── layout/           # Layout components (Sidebar, Navbar, TagsView)
│   ├── plugins/          # Vue plugins (auth, cache, modal, download)
│   ├── router/           # Vue Router (constantRoutes, dynamicRoutes)
│   ├── store/            # Vuex modules (app, user, permission, tagsView, dict, settings)
│   ├── utils/            # Utilities (request, auth, dict, permission, yxx.js)
│   └── views/            # Page components (by domain: business, system, monitor, tool)
├── .env.dev              # Development environment variables
├── .env.test             # Test environment variables
├── .env.prod             # Production environment variables
├── vue.config.js         # Vue CLI configuration
└── babel.config.js       # Babel configuration
```

## Architecture

- **Vue 2.6.12** with **Vue Router 3.4.9** and **Vuex 3.6.0**
- **Element UI 2.15.14** for UI components
- **Axios 0.28.1** for HTTP requests with interceptors
- **pnpm** for package management

### Request/Response Pattern

All API responses use the `R<T>` wrapper format:
```js
{ code: 200, msg: 'success', data: {...}, rows: [...], total: 100 }
```

The request interceptor (`src/utils/request.js`) automatically:
- Adds `Authorization: Bearer <token>` header
- Converts GET params to URL query strings
- Prevents duplicate form submissions (POST/PUT)

### Permission System

- **Token-based auth**: JWT tokens stored via `js-cookie`
- **Dynamic routes**: Loaded from `permission` module based on user roles
- **Directives**: `v-hasPermi="['module:action']"` for button-level permissions
- **Store**: `store.getters.permissions` for programmatic checks

### Dictionary System

Global dictionary handling via `@/api/system/dict/data`:
```js
// In component
export default {
  dicts: ['sys_normal_disable'],
  data() { return { dict: { type: {} } } }
}

// In template
<dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
```

## Code Patterns

### API Module Pattern

```js
// src/api/business/testUser.js
import request from '@/utils/request'

export function listTestUser(query) {
  return request({ url: '/business/testUser/list', method: 'get', params: query })
}
export function getTestUser(userId) {
  return request({ url: '/business/testUser/' + userId, method: 'get' })
}
export function addTestUser(data) {
  return request({ url: '/business/testUser', method: 'post', data: data })
}
export function updateTestUser(userId, data) {
  return request({ url: '/business/testUser/' + userId, method: 'put', data: data })
}
export function delTestUser(userId) {
  return request({ url: '/business/testUser/' + userId, method: 'delete' })
}
```

### CRUD Page Pattern

```vue
<template>
  <div class="app-container">
    <!-- Search form -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
      <el-form-item label="用户名" prop="userName">
        <el-input v-model="queryParams.userName" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-button @click="handleQuery">搜索</el-button>
      <el-button @click="resetQuery">重置</el-button>
    </el-form>

    <!-- Action buttons -->
    <el-row :gutter="10" class="mb8">
      <el-button type="primary" @click="handleAdd" v-hasPermi="['module:add']">新增</el-button>
      <el-button type="danger" @click="handleDelete" :disabled="multiple" v-hasPermi="['module:remove']">删除</el-button>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"/>
    </el-row>

    <!-- Data table -->
    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"/>
      <el-table-column prop="userName" label="用户名"/>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button @click="handleUpdate(scope.row)">修改</el-button>
          <el-button @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Pagination -->
    <pagination :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
  </div>
</template>

<script>
import { list, get, add, update, del } from "@/api/module/xxx"

export default {
  name: "Xxx",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      list: [],
      queryParams: { pageNum: 1, pageSize: 10 },
      form: {},
      rules: {}
    }
  },
  created() { this.getList() },
  methods: {
    getList() {
      this.loading = true
      list(this.queryParams).then(r => {
        this.list = r.rows
        this.total = Number(r.total)
        this.loading = false
      })
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery() },
    handleSelectionChange(s) {
      this.ids = s.map(i => i.id)
      this.single = s.length !== 1
      this.multiple = !s.length
    },
    handleAdd() { this.reset(); this.open = true; this.title = "添加" },
    handleUpdate(row) {
      this.reset()
      get(row.id || this.ids).then(r => { this.form = r.data; this.open = true })
    },
    submitForm() {
      this.$refs.form.validate(v => {
        if (v) {
          (this.form.id ? update : add)(this.form).then(() => {
            this.$modal.msgSuccess("操作成功")
            this.open = false; this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      this.$modal.confirm("确认删除？").then(() => del(row.id || this.ids)).then(() => {
        this.getList(); this.$modal.msgSuccess("删除成功")
      })
    },
    reset() { this.form = {}; this.resetForm("form") }
  }
}
</script>
```

### Date Range Handling

```js
// In component data
daterangeXxx: []

// In query method
if (null != this.daterangeXxx && '' != this.daterangeXxx) {
  this.queryParams.params["beginXxx"] = this.daterangeXxx[0]
  this.queryParams.params["endXxx"] = this.daterangeXxx[1]
}
```

### Common Utilities (`@/utils/yxx.js`)

- `parseTime(time, pattern)` - Date formatting
- `resetForm(refName)` - Reset form via ref
- `addDateRange(params, dateRange, propName)` - Add begin/end time to params
- `selectDictLabel(datas, value)` - Get dictionary label by value
- `handleTree(data, id, parentId, children)` - Build tree from flat array

## Configuration

### Environment Variables

| Variable | Dev | Test | Prod |
|----------|-----|------|------|
| `VUE_APP_BASE_API` | `/dev-api` | `/test-api` | `/prod-api` |
| `VUE_APP_PUBLIC_PATH` | `/yxx-yi` | `/yxx-yi` | `/yxx-yi` |
| `VUE_PORT` | 83 | - | - |

### Proxy Configuration (vue.config.js)

- `/dev-api`, `/test-api`, `/prod-api` → `http://localhost:7318`
- `/v3/api-docs` → `http://localhost:7318` (SpringDoc/Swagger)

## Global Components

Registered in `src/main.js`:
- `<Pagination>` - Pagination component
- `<RightToolbar>` - Search/export toolbar
- `<Editor>` - Rich text editor (Quill)
- `<FileUpload>` - File upload
- `<ExcelUpload>` - Excel import
- `<ImageUpload>` - Image upload
- `<ImagePreview>` - Image preview
- `<DictTag>` - Dictionary tag display

## Global Methods

- `this.getDicts(dictType)` - Fetch dictionary data
- `this.parseTime(time, pattern)` - Format date
- `this.download(url, params, filename)` - Download file
- `this.handleTree(data)` - Convert list to tree
- `this.$modal.msgSuccess(msg)` - Success message
- `this.$modal.confirm(msg)` - Confirmation dialog
