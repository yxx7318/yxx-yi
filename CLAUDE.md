# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Monorepo containing a full-stack rapid development platform based on RuoYi framework with Spring AI integration. The project provides AI capabilities including chat sessions, RAG, function calling, and MCP.

Demo: http://meraki-e.fun/yxx-yi/

## Repository Structure

```
yxx-yi/
├── yxx-yxx-8/           # Java 8 + Spring Boot 2.5.15 backend (legacy)
├── yxx-yxx-17/          # Java 17 + Spring Boot 3.3.5 backend (current)
├── yxx-ui-vue2/         # Vue 2 + Element UI admin panel
├── yxx-ui-vue3/         # Vue 3 + Element Plus + Vite admin panel
├── yxx-app-vue3/        # Vue 2 + Element UI (alternative frontend)
├── sql/                 # Database scripts
│   └── gen-test/        # Code generation test SQL files
└── img/README/          # Documentation images
```

## Backend Build & Run (yxx-yxx-17)

```bash
# Build all modules
mvn clean package -pl yxx-yxx-17/yxx-yxx -am

# Run application
mvn spring-boot:run -pl yxx-yxx-17/yxx-yxx

# Run with profile
mvn spring-boot:run -pl yxx-yxx-17/yxx-yxx -Dspring-boot.run.profiles=dev

# Run tests
mvn test -pl yxx-yxx-17/yxx-yxx

# Run single test
mvn test -pl yxx-yxx-17/yxx-yxx -Dtest=RedisTest
```

## Frontend Build & Run

### Vue 3 Admin (yxx-ui-vue3)
```bash
cd yxx-ui-vue3
yarn --registry=https://registry.npmmirror.com
yarn dev          # Port 83, proxies to http://localhost:7318
yarn build:prod
```

### Vue 2 Admin (yxx-ui-vue2)
```bash
cd yxx-ui-vue2
pnpm install --registry=https://registry.npmmirror.com
npm run dev       # Port 83, proxies to http://localhost:7318
npm run build:prod
```

### Vue 2 App (yxx-app-vue3)
```bash
cd yxx-app-vue3
pnpm install --registry=https://registry.npmmirror.com
npm run dev
npm run build:prod
```

## Backend Architecture (yxx-yxx-17)

### Module Dependencies
```
yxx-yxx (main app) → yxx-admin → (yxx-framework → yxx-system → yxx-common)
                          └─→ yxx-z-ai (Spring AI integration)
```

### Tech Stack
- Spring Boot 3.3.5 + Java 17 (virtual threads enabled)
- MyBatis Plus 3.5.10 + PageHelper
- Spring Security + JWT
- Redis/Redisson (cache, distributed locks, queues)
- Druid (multi-datasource, master-slave)
- Knife4j (OpenAPI 3 docs)
- Spring AI (chat, RAG, function calling, MCP)

### Entity Layers
```
DO (Database Object) → VO (View Object) → EditDTO (Input DTO) → QueryDTO (Query params)
```

### Common Annotations
- `@PreAuthorize("@ss.hasPermi('module:action')")` - Permission check
- `@PreAuthorize("@ss.hasRole('role')")` - Role check
- `@Log(title = "...", businessType = BusinessType.INSERT)` - Operation logging
- `@Anonymous` - Anonymous access
- `@RepeatSubmit` - Prevent duplicate submissions
- `@DataScope` - Data scope filtering
- `@DataSource(DataSourceType.SLAVE)` - Read-only query from slave DB

### Response Format
All responses use `R<T>` wrapper:
```java
R.ok(data)      // { code: 200, msg: "success", data: {...} }
R.fail(msg)     // { code: 500, msg: "error message" }
```
List responses include `rows` and `total` for pagination.

## Frontend Architecture

### yxx-ui-vue3 (Vue 3 + Vite)
- Vue 3.5 + Composition API (`<script setup>`)
- Pinia state management
- Element Plus UI
- Vite 6

### yxx-ui-vue2 (Vue 2 + Webpack)
- Vue 2.6.12 + Options API
- Vuex state management
- Element UI 2.15.14
- Vue CLI

### yxx-app-vue3 (uni-app)
- Vue 3 + Vite + TypeScript + Pinia
- Cross-platform (WeChat Mini Program, H5)
- Uses `uni.*` API instead of browser APIs

### API Response Pattern
All API responses follow unified format:
```js
{ code: 200, msg: 'success', data: {...}, rows: [...], total: 100 }
```

### Permission System
- Token-based JWT auth
- Dynamic routes loaded from permission module
- `v-hasPermi="['module:action']"` for button permissions
- `v-hasRole="['admin']"` for role checks

## SQL Code Specification

Based on `sql/gen-test/tb_test_*.sql` files, follow these conventions:

### Table Definition Format
```sql
-- ----------------------------
-- Table description
-- ----------------------------
drop table if exists `tb_test_user`;
create table `tb_test_user`
(
  user_id           bigint(20)        not null auto_increment   comment '用户 ID',
  parent_id         bigint(20)                                  comment '主表 ID',
  user_name         varchar(30)       not null                  comment '用户账号',
  password          varchar(100)      default ''                comment '密码',
  status            char(1)           default '0'               comment '账号状态（0 正常 1 停用）',
  register_date     date                                        comment '注册日期',
  register_time     datetime                                    comment '注册时间',
  create_by_id      bigint(20)        default 0                 comment '创建者 Id',
  create_by_name    varchar(64)       default ''                comment '创建者名称',
  create_time       datetime not null default current_timestamp comment '创建时间',
  update_by_id      bigint(20)        default 0                 comment '更新者 Id',
  update_by_name    varchar(64)       default ''                comment '更新者名称',
  update_time       datetime not null default current_timestamp on update current_timestamp comment '更新时间',
  remark            varchar(500)      default null              comment '备注',
  primary key (user_id)
) engine=innodb auto_increment=100 comment = '表注释';
```

### Field Naming Conventions
- Primary key: `{table}_id` or `id` with `bigint(20) not null auto_increment`
- Audit fields: `create_by_id`, `create_by_name`, `create_time`, `update_by_id`, `update_by_name`, `update_time`
- Status fields: `char(1)` with default '0'
- Foreign keys: `{related_table}_id`
- Timestamps: `create_time`, `update_time` with `current_timestamp` defaults
- Use `default 0` for numeric fields, `default ''` for strings, `default null` for text

## Additional Resources

Refer to module-specific CLAUDE.md files for detailed patterns:
- `yxx-yxx-17/CLAUDE.md` - Backend Java patterns
- `yxx-ui-vue3/CLAUDE.md` - Vue 3 frontend patterns
- `yxx-ui-vue2/CLAUDE.md` - Vue 2 frontend patterns
- `yxx-app-vue3/CLAUDE.md` - uni-app patterns
