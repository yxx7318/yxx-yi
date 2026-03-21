# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
# Install dependencies
yarn --registry=https://registry.npmmirror.com

# Development server (port 83, proxy to http://localhost:7318)
yarn dev

# Build for test environment
yarn build:test

# Build for production
yarn build:prod

# Preview production build
yarn preview
```

## Project Structure

This is a Vue 3 + Vite frontend project based on RuoYi-Vue3, paired with a Java/Spring Boot backend (yxx-yi parent module).

```
yxx-ui-vue3/
├── src/
│   ├── api/              # Backend API calls (organized by domain)
│   ├── assets/           # Static assets
│   ├── components/       # Reusable components
│   │   ├── Crontab/      # Cron expression builder
│   │   ├── ExcelUpload/  # Excel import component
│   │   ├── FileUpload/   # File upload component
│   │   ├── ImageUpload/  # Image upload with preview
│   │   ├── Editor/       # Rich text editor (Quill)
│   │   └── ...
│   ├── config/           # App configuration (permission.js)
│   ├── directive/        # Custom directives (hasPermi, hasRole)
│   ├── layout/           # Layout components (Sidebar, Navbar, TagsView)
│   ├── plugins/          # Global plugins (auth, cache, modal, download)
│   ├── router/           # Vue Router configuration
│   ├── store/            # Pinia stores (user, permission, tagsView, settings, dict)
│   ├── utils/            # Utilities (auth, request, validate, dict, yxx.js)
│   ├── views/            # Page components (organized by domain)
│   └── main.js           # Entry point
├── vite/                 # Vite plugins config
├── .env.dev              # Development env config
├── .env.test             # Test env config
├── .env.prod             # Production env config
└── vite.config.js        # Vite configuration
```

## Architecture

- **Vue 3.5** with Composition API (`<script setup>`)
- **Pinia** for state management
- **Vue Router 4** with dynamic routes based on permissions
- **Element Plus** UI framework
- **Axios** for HTTP requests with interceptors
- **Vite 6** for build tooling

### State Management (Pinia)

- `user` - User info, token, roles, permissions
- `permission` - Dynamic route generation
- `tagsView` - Browser tabs/cached views
- `settings` - App settings (theme, layout)
- `dict` - System dictionary cache

### API Layer

API files are organized by domain in `src/api/`:
- `system/` - System modules (user, role, menu, dict, etc.)
- `monitor/` - Monitoring modules (job, cache, server, logs)
- `tool/` - Developer tools (gen, swagger)
- `business/` - Business modules
- `ai/` - AI-related modules

All requests use the unified `request.js` interceptor with:
- Token injection (`Authorization: Bearer <token>`)
- Auto-retry on 401
- Deduplication for repeated submissions
- Unified error handling

### Permission System

- Menu-level: `v-hasPermi="['module:action']"` directive
- Role-level: `v-hasRole="['admin']"` directive
- Route-level: `meta.permissions` or `meta.roles`
- Button-level: Permission checks in components

### Common Patterns

**Component Structure:**
```vue
<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true">
      <!-- Search form -->
    </el-form>
    <el-row :gutter="10" class="mb8">
      <!-- Toolbar buttons -->
    </el-row>
    <el-table v-loading="loading" :data="tableList">
      <!-- Table columns -->
    </el-table>
    <pagination v-show="total>0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    <el-dialog v-model="open" :title="title">
      <!-- Form dialog -->
    </el-dialog>
  </div>
</template>

<script setup name="ModuleName">
import { listModule, getModule, delModule, addModule, updateModule } from "@/api/domain/module"

const { proxy } = getCurrentInstance()
const { dictType } = proxy.useDict('dictType')

// Reactive state using toRefs pattern
const data = reactive({
  form: {},
  queryParams: { pageNum: 1, pageSize: 10 },
  rules: {}
})
const { queryParams, form, rules } = toRefs(data)

// CRUD functions
function getList() { /* ... */ }
function handleAdd() { /* ... */ }
function handleUpdate(row) { /* ... */ }
function handleDelete(row) { /* ... */ }
function submitForm() { /* ... */ }
</script>
```

**API Call Pattern:**
```javascript
// src/api/domain/module.js
import request from '@/utils/request'

export function listModule(query) {
  return request({ url: '/domain/module/list', method: 'get', params: query })
}

export function getModule(id) {
  return request({ url: `/domain/module/${id}`, method: 'get' })
}

export function addModule(data) {
  return request({ url: '/domain/module', method: 'post', data })
}

export function updateModule(id, data) {
  return request({ url: `/domain/module/${id}`, method: 'put', data })
}

export function delModule(id) {
  return request({ url: `/domain/module/${id}`, method: 'delete' })
}
```

## Configuration

### Environment Variables

| Variable | Description | Dev | Test | Prod |
|----------|-------------|-----|------|------|
| `VITE_APP_ENV` | Environment | development | staging | production |
| `VITE_APP_BASE_API` | API prefix | /dev-api | /stage-api | /prod-api |
| `VITE_BASE_URL` | Backend URL | http://localhost:7318 | - | - |
| `VITE_PORT` | Dev server port | 83 | - | - |
| `VITE_APP_PUBLIC_PATH` | Sub-path | /yxx-yi | /yxx-yi | /yxx-yi |

### Proxy Configuration

Vite proxies `/dev-api`, `/stage-api`, `/prod-api` to the backend server. API URLs in code should use the relative path without the prefix (e.g., `/system/user/list`).

## Utility Functions

Key utilities in `src/utils/`:

- `auth.js` - Token management (`getToken`, `setToken`, `removeToken`)
- `request.js` - Axios instance with interceptors, `download()` function
- `yxx.js` - Common helpers (`parseTime`, `resetForm`, `addDateRange`, `handleTree`, `selectDictLabel`)
- `dict.js` - `useDict()` composable for dictionary data
- `validate.js` - Validation helpers (`isHttp`, `isEmpty`, `isPathMatch`)
- `permission.js` - Permission check helpers

## Global Components

Registered in `main.js`:
- `<Pagination>` - Pagination component
- `<RightToolbar>` - Search toggle and column visibility
- `<DictTag>` - Dictionary value display
- `<Editor>` - Rich text editor
- `<FileUpload>` - File upload
- `<ImageUpload>` - Image upload
- `<ImagePreview>` - Image preview
- `<ExcelUpload>` - Excel import

## Directives

- `v-hasPermi` - Permission check: `v-hasPermi="['system:user:add']"`
- `v-hasRole` - Role check: `v-hasRole="['admin']"`
- `v-copyText` - Copy to clipboard

## Backend Integration

The frontend expects backend responses in this format:
```json
{
  "code": 200,
  "msg": "success",
  "data": { ... }
}
```

List responses:
```json
{
  "code": 200,
  "rows": [...],
  "total": 100
}
```

Error codes:
- `200` - Success
- `401` - Unauthorized (triggers re-login)
- `500` - Server error
- `601` - Warning
