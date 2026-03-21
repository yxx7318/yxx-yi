# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
# Install dependencies
pnpm install
# or
yarn
# or
npm install

# Development - WeChat Mini Program
pnpm dev:mp-weixin

# Development - H5
pnpm dev:h5

# Build - WeChat Mini Program
pnpm build:mp-weixin

# Build - H5
pnpm build:h5

# Type check
pnpm type-check
```

## Project Structure

This is a **uni-app** cross-platform frontend project (not standard Vue 3 web app). It uses Vue 3.5 + Vite + Pinia + TypeScript.

```
src/
├── api/              # Backend API calls (login.ts, oauth.ts, system/)
├── components/       # Reusable components
│   ├── geek-xd/     # Custom geek components (color-picker, qrcode, menu, etc.)
│   ├── qiun-data-charts/  # Chart components (ucharts)
│   └── uni_*        # uni-app standard components
├── directive/        # Custom directives (hasPermi, hasRole, copyText, focus)
├── pages/           # Main pages (index, login, work, template, mine)
├── pages_geek/      # Geek feature pages
├── pages_mine/      # User profile pages (avatar, info, pwd, setting)
├── pages_qiun/      # Chart demo pages
├── pages_template/  # Template pages (mall, order, login templates)
├── plugins/         # Global plugins
│   ├── auth.ts      # Permission checks (hasPermi, hasRole)
│   ├── tab.ts       # Page navigation (navigateTo, switchTab, etc.)
│   ├── modal.ts     # Toast, alert, confirm, loading
│   ├── bus.ts       # Event bus ($on, $off, $emit)
│   └── socket.ts    # WebSocket client
├── store/           # Pinia stores (user.ts, dict.ts)
├── uni_modules/     # uni-app module marketplace packages
│   ├── uni-badge/
│   ├── uni-calendar/
│   ├── ...
├── utils/           # Utilities
│   ├── auth.ts      # Token management (getToken, setToken, removeToken)
│   ├── request.ts   # uni.request wrapper (getAction, postAction, etc.)
│   ├── yxx.js       # Common helpers (parseTime, resetForm, handleTree)
│   ├── dict.ts      # Dictionary utility
│   └── upload.ts    # File upload utility
├── main.js          # Entry point (createSSRApp)
├── pages.json       # Page routing & tabBar config
├── manifest.json    # App manifest
└── config.js        # Base URL config
```

## Architecture

### Key Differences from Standard Vue 3

- Uses `uni.*` API instead of browser APIs (`uni.request`, `uni.navigateTo`, `uni.getStorageSync`)
- SSR-compatible app creation with `createSSRApp`
- Conditional compilation for platform-specific code (`#ifdef MP-WEIXIN`)
- Uses `pages.json` for routing instead of Vue Router

### State Management (Pinia)

- `user` store - Token, user info, roles, permissions
- `dict` store - Dictionary cache

### Permission System

**Directives:**
- `v-hasPermi="['system:user:add']"` - Permission check
- `v-hasRole="['admin']"` - Role check (admin = super admin)

**Plugins:**
```javascript
this.$auth.hasPermi('system:user:add')
this.$auth.hasRole('admin')
// or in composition API
import { auth } from '@/plugins'
auth.hasPermi('system:user:add')
```

### Navigation Plugin

```javascript
this.$tab.navigateTo(url)
this.$tab.switchTab(url)
this.$tab.reLaunch(url)
this.$tab.redirectTo(url)
this.$tab.navigateBack()
// or
import { tab } from '@/plugins'
```

### Modal Plugin

```javascript
this.$modal.msg('message')
this.$modal.msgSuccess('success')
this.$modal.msgError('error')
this.$modal.confirm('message')
this.$modal.loading('loading')
this.$modal.closeLoading()
// or
import { modal } from '@/plugins'
```

### API Layer

Uses `uni.request` wrapper in `src/utils/request.ts`:

```typescript
import { getAction, postAction, putAction, deleteAction } from '@/utils/request'

// Example API call
export function getUserInfo() {
  return getAction('/system/user/info')
}

export function updateUserInfo(data) {
  return putAction('/system/user', data)
}
```

Features:
- Auto token injection (`Authorization: Bearer <token>`)
- Unified error handling (401, 500)
- Configurable timeout (default 10s)

### Utility Functions

Key helpers in `src/utils/yxx.js`:
- `parseTime(time, pattern)` - Date formatting
- `resetForm(refName)` - Reset form
- `addDateRange(params, dateRange)` - Add date range to params
- `selectDictLabel(datas, value)` - Dictionary label lookup
- `handleTree(data, id, parentId, children)` - Build tree structure

## Component Patterns

### Page Component Structure

```vue
<template>
  <view class="container">
    <up-form :model="form" :rules="rules" ref="formRef">
      <!-- form fields -->
    </up-form>
    <up-button @click="handleSubmit">Submit</up-button>
  </view>
</template>

<script setup name="PageName">
import { ref, reactive } from 'vue'
import { getUserInfo, updateUserInfo } from '@/api/system/user'
import { modal } from '@/plugins'

const { proxy } = getCurrentInstance()
const formRef = ref(null)
const form = reactive({
  name: '',
  age: 0
})
const rules = {
  name: [{ required: true, message: 'Required', trigger: 'blur' }]
}

async function handleSubmit() {
  await updateUserInfo(form)
  modal.msgSuccess('Success')
}
</script>

<style lang="scss" scoped>
.container {
  padding: 20rpx;
}
</style>
```

## Notes

- uni-app uses `rpx` for responsive units (750rpx = full width)
- Use `<view>` instead of `<div>`, `<text>` instead of `<span>`
- Pages are configured in `pages.json`, not Vue Router
- Sub-packages (`pages_mine`, `pages_template`, etc.) are lazy-loaded
- WeChat mini-program does not support custom directives, only H5 and App-Vue
