# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
# Build all modules
mvn clean package

# Build specific module
mvn clean package -pl yxx-yxx -am

# Run application
mvn spring-boot:run -pl yxx-yxx

# Run with specific profile
mvn spring-boot:run -pl yxx-yxx -Dspring-boot.run.profiles=dev

# Run tests
mvn test -pl yxx-yxx

# Run single test class
mvn test -pl yxx-yxx -Dtest=RedisTest
```

## Project Structure

Monorepo with Maven multi-module structure:

```
yxx/                          # Root parent POM (yxx v3.8.9)
├── yxx-yi/                   # Framework modules parent
│   ├── yxx-admin/            # Admin controllers (system, monitor)
│   ├── yxx-common/           # Common utilities, annotations, base classes
│   ├── yxx-framework/        # Core framework (security, config, aspectj)
│   ├── yxx-system/           # System domain entities and services
│   ├── yxx-quartz/           # Scheduled job handling
│   └── yxx-generator/        # Code generation
└── yxx-yxx/                  # Main application module (your business logic)
```

## Architecture

- **Spring Boot 2.5.15** with **Java 8**
- **MyBatis Plus 3.5.2** for data persistence
- **Spring Security 5.7.14** with JWT token-based authentication
- **Redis/Redisson** for caching, distributed locks, and queues
- **Druid** connection pool with master-slave datasource support
- **Hutool** and **FastJSON2** for utilities and JSON processing
- **Knife4j** (Swagger 3/OpenAPI) for API documentation

## Module Dependencies

`yxx-yxx` (main app) → `yxx-admin` → (`yxx-framework` → `yxx-system` → `yxx-common`)

## Code Patterns

### Entity Layers

```
DO (Database Object) → VO (View Object) → EditDTO (Input DTO) → QueryDTO (Query params)
```

### Controller Pattern

```java
@RestController
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class XxxController extends BaseControllerPlus {
    private final IXxxService xxxService;

    @GetMapping("/list")
    public PageResult<XxxVO> list(XxxQueryDTO dto) { ... }
}
```

### Service Pattern

```java
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class XxxServiceImpl extends ServiceImplPlus<XxxMapper, XxxDO> implements IXxxService {
    private final IXxxService self;  // For self-invocation
    private final XxxMapper xxxMapper;
}
```

### Permission Annotations

- `@PreAuthorize("@ss.hasPermi('module:action')")` - Check permission
- `@PreAuthorize("@ss.hasRole('role')")` - Check role
- `@Log(title = "...", businessType = BusinessType.INSERT)` - Operation logging

### Common Conventions

- All responses use `R<T>` wrapper: `R.ok(data)`, `R.fail(msg)`
- Pagination via `PageResult<T>` with PageHelper
- Use `@DataScope` for data scope filtering
- Use `@DataSource(DataSourceType.SLAVE)` for read-only queries
- Use `bean.name` format for Spring EL in annotations (e.g., `@ss`)

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

## Configuration

Profiles: `dev` (default), `test`, `prod`

Key config files:
- `application.yml` - Base config
- `application-dev.yml` - Environment-specific (DB, Redis, server port)

Dev server: port 7318, context-path `/`

## Testing

Tests use `@SpringBootTest`. Example:
```java
@SpringBootTest
public class RedisTest {
    @Autowired private RedisLockSimple redisLockSimple;

    @Test
    public void testLock() { ... }
}
```
