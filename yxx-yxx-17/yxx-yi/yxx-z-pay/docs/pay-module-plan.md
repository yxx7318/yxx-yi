# yxx-z-pay 支付模块开发规划

## 一、项目背景与目标

### 1.1 核心目标
将 `yxx-z-pay` 支付模块改造为一个完善的具有支付能力的系统，包括：
- 微信支付各方式接入（JSAPI、小程序、App、Native、H5、付款码）- **仅V3版本**
- 支付宝支付各方式接入（PC、Wap、App、扫码、条码）
- 支付发起、支付回调、订单查询、退款
- 支付结果轮询、超时订单处理、退款结果轮询

### 1.2 设计原则
| 原则 | 说明 |
|------|------|
| 单商户模式 | 当前仅支持单个商户号，后续扩展为多商户只需增加配置 |
| 金额单位 | 统一使用"分"（Integer），避免浮点数精度问题 |
| 配置方式 | 支付配置写入 application-pay.yml，渠道状态存数据库 |
| 前端轮询 | 前端通过轮询后端接口获取支付状态，无需主动通知业务系统 |

### 1.3 现有代码状态
| 模块 | 状态 | 说明 |
|------|------|------|
| client/impl | ✅ 基本完整 | 微信/支付宝支付客户端实现已存在，需移除V2逻辑 |
| core/domain | ⚠️ 需调整 | DO实体需继承BaseColumnEntity |
| enums | ✅ 基本完整 | 枚举类已定义 |
| service | ❌ 需重写 | PayOrderServiceImpl等已注释 |
| controller | ❌ 需重写 | PayNotifyController已注释 |
| mapper | ❌ 缺失 | 需要创建Mapper接口和XML |
| SQL | ⚠️ 需调整 | 现有pay.sql需调整字段和金额单位 |

---

## 二、开发阶段规划

### 阶段一：基础设施搭建（Phase 1）✅ 已完成
- [x] 1.1 创建数据库表结构（4张表）
- [x] 1.2 创建Mapper接口和XML文件
- [x] 1.3 完善DO实体类（继承BaseColumnEntity）
- [x] 1.4 创建VO/DTO实体类
- [x] 1.5 完善枚举类和错误码

### 阶段二：核心服务层开发（Phase 2）✅ 已完成
- [x] 2.1 实现PayChannelService（支付渠道服务）
- [x] 2.2 实现PayOrderService（支付订单服务）
  - [x] 2.2.1 创建订单
  - [x] 2.2.2 提交支付
  - [x] 2.2.3 支付回调处理
  - [x] 2.2.4 订单查询
- [x] 2.3 实现PayRefundService（退款服务）
  - [x] 2.3.1 创建退款单
  - [x] 2.3.2 发起退款
  - [x] 2.3.3 退款回调处理
  - [x] 2.3.4 退款查询
- [x] 2.4 编写核心Service单元测试

### 阶段三：控制器层开发（Phase 3）✅ 已完成
- [x] 3.1 实现PayNotifyController（支付回调控制器）
- [x] 3.2 实现PayOrderController（订单管理控制器）
- [x] 3.3 实现PayRefundController（退款管理控制器）
- [x] 3.4 实现PayChannelController（渠道管理控制器）

### 阶段四：定时任务开发（Phase 4）✅ 已完成
- [x] 4.1 支付结果轮询任务
- [x] 4.2 超时订单关闭任务
- [x] 4.3 退款结果轮询任务

### 阶段五：API层完善（Phase 5）✅ 已完成
- [x] 5.1 完善PayOrderApi（跨模块调用接口）
- [x] 5.2 完善PayRefundApi（跨模块调用接口）

### 阶段六：测试与验证（Phase 6）
- [ ] 6.1 单元测试
- [ ] 6.2 集成测试
- [ ] 6.3 回归测试

---

## 三、详细设计

### 3.1 数据库表设计

#### 3.1.1 支付渠道表 (pay_channel)
```sql
-- 支付渠道表：存储各支付渠道的配置和状态
DROP TABLE IF EXISTS pay_channel;
CREATE TABLE pay_channel
(
    channel_id          BIGINT(20)        NOT NULL AUTO_INCREMENT   COMMENT '渠道编号',
    channel_code        VARCHAR(32)       NOT NULL                  COMMENT '渠道编码',
    channel_name        VARCHAR(64)       NOT NULL DEFAULT ''       COMMENT '渠道名称',
    channel_config      TEXT              NULL                      COMMENT '渠道配置(JSON格式)',
    channel_fee_rate    DECIMAL(5,2)      DEFAULT 0                 COMMENT '渠道手续费率(百分比)',
    status              TINYINT           DEFAULT 0                 COMMENT '状态(0正常 1停用)',
    create_by_id        BIGINT(20)        DEFAULT 0                 COMMENT '创建者Id',
    create_by_name      VARCHAR(64)       DEFAULT ''                COMMENT '创建者名称',
    create_time         DATETIME          NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by_id        BIGINT(20)        DEFAULT 0                 COMMENT '更新者Id',
    update_by_name      VARCHAR(64)       DEFAULT ''                COMMENT '更新者名称',
    update_time         DATETIME          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark              VARCHAR(500)      DEFAULT NULL              COMMENT '备注',
    PRIMARY KEY (channel_id)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='支付渠道表';
```

#### 3.1.2 支付订单表 (pay_order)
```sql
-- 支付订单表
DROP TABLE IF EXISTS pay_order;
CREATE TABLE pay_order
(
    order_id            BIGINT(20)        NOT NULL AUTO_INCREMENT   COMMENT '订单编号',
    order_no            VARCHAR(64)       NOT NULL                  COMMENT '支付订单号',
    -- 用户信息
    user_id             BIGINT(20)        NOT NULL                  COMMENT '用户编号',
    -- 商户信息
    merchant_order_id   VARCHAR(64)       NOT NULL                  COMMENT '商户订单编号',
    subject             VARCHAR(32)       NOT NULL                  COMMENT '商品标题',
    body                VARCHAR(128)      DEFAULT NULL              COMMENT '商品描述',
    -- 渠道信息
    channel_id          BIGINT(20)        DEFAULT NULL              COMMENT '渠道编号',
    channel_code        VARCHAR(32)       DEFAULT NULL              COMMENT '渠道编码',
    channel_user_id     VARCHAR(64)       DEFAULT NULL              COMMENT '渠道用户编号(openid等)',
    channel_order_no    VARCHAR(64)       DEFAULT NULL              COMMENT '渠道订单号',
    -- 订单信息
    pay_price           INT               NOT NULL                  COMMENT '支付金额(分)',
    channel_fee_rate    DECIMAL(5,2)      DEFAULT 0                 COMMENT '渠道手续费率',
    channel_fee_price   INT               DEFAULT 0                 COMMENT '渠道手续费(分)',
    order_status        TINYINT           NOT NULL DEFAULT 0        COMMENT '支付状态(0待支付 10成功 20退款 30关闭)',
    expire_time         DATETIME          NOT NULL                  COMMENT '订单失效时间',
    success_time        DATETIME          DEFAULT NULL              COMMENT '支付成功时间',
    extension_id        BIGINT(20)        DEFAULT NULL              COMMENT '成功扩展单编号',
    -- 退款信息
    refund_price        INT               NOT NULL DEFAULT 0        COMMENT '已退款金额(分)',
    -- 审计字段
    create_by_id        BIGINT(20)        DEFAULT 0                 COMMENT '创建者Id',
    create_by_name      VARCHAR(64)       DEFAULT ''                COMMENT '创建者名称',
    create_time         DATETIME          NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by_id        BIGINT(20)        DEFAULT 0                 COMMENT '更新者Id',
    update_by_name      VARCHAR(64)       DEFAULT ''                COMMENT '更新者名称',
    update_time         DATETIME          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark              VARCHAR(500)      DEFAULT NULL              COMMENT '备注',
    PRIMARY KEY (order_id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_merchant_order_id (merchant_order_id),
    KEY idx_user_id (user_id),
    KEY idx_order_status (order_status),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='支付订单表';
```

#### 3.1.3 支付订单扩展表 (pay_order_extension)
```sql
-- 支付订单扩展表：每次支付尝试创建一条记录
DROP TABLE IF EXISTS pay_order_extension;
CREATE TABLE pay_order_extension
(
    extension_id        BIGINT(20)        NOT NULL AUTO_INCREMENT   COMMENT '扩展编号',
    order_id            BIGINT(20)        NOT NULL                  COMMENT '订单编号',
    order_no            VARCHAR(64)       NOT NULL                  COMMENT '支付订单号',
    channel_id          BIGINT(20)        NOT NULL                  COMMENT '渠道编号',
    channel_code        VARCHAR(32)       NOT NULL                  COMMENT '渠道编码',
    user_ip             VARCHAR(50)       DEFAULT NULL              COMMENT '用户IP',
    order_status        TINYINT           NOT NULL DEFAULT 0        COMMENT '支付状态',
    channel_notify_data TEXT              DEFAULT NULL              COMMENT '渠道回调数据',
    channel_error_code  VARCHAR(64)       DEFAULT NULL              COMMENT '渠道错误码',
    channel_error_msg   VARCHAR(255)      DEFAULT NULL              COMMENT '渠道错误信息',
    -- 审计字段
    create_by_id        BIGINT(20)        DEFAULT 0                 COMMENT '创建者Id',
    create_by_name      VARCHAR(64)       DEFAULT ''                COMMENT '创建者名称',
    create_time         DATETIME          NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by_id        BIGINT(20)        DEFAULT 0                 COMMENT '更新者Id',
    update_by_name      VARCHAR(64)       DEFAULT ''                COMMENT '更新者名称',
    update_time         DATETIME          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark              VARCHAR(500)      DEFAULT NULL              COMMENT '备注',
    PRIMARY KEY (extension_id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_order_id (order_id)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='支付订单扩展表';
```

#### 3.1.4 支付退款表 (pay_refund)
```sql
-- 支付退款表
DROP TABLE IF EXISTS pay_refund;
CREATE TABLE pay_refund
(
    refund_id           BIGINT(20)        NOT NULL AUTO_INCREMENT   COMMENT '退款编号',
    refund_no           VARCHAR(64)       NOT NULL                  COMMENT '退款单号',
    order_id            BIGINT(20)        NOT NULL                  COMMENT '订单编号',
    -- 用户信息
    user_id             BIGINT(20)        NOT NULL                  COMMENT '用户编号',
    -- 商户信息
    merchant_order_id   VARCHAR(64)       NOT NULL                  COMMENT '商户订单编号',
    merchant_refund_id  VARCHAR(64)       NOT NULL                  COMMENT '商户退款单号',
    -- 渠道信息
    channel_id          BIGINT(20)        NOT NULL                  COMMENT '渠道编号',
    channel_code        VARCHAR(32)       NOT NULL                  COMMENT '渠道编码',
    channel_order_no    VARCHAR(64)       DEFAULT NULL              COMMENT '渠道订单号',
    channel_refund_no   VARCHAR(64)       DEFAULT NULL              COMMENT '渠道退款单号',
    -- 退款信息
    pay_price           INT               NOT NULL                  COMMENT '原支付金额(分)',
    refund_price        INT               NOT NULL                  COMMENT '退款金额(分)',
    refund_reason       VARCHAR(256)      DEFAULT NULL              COMMENT '退款原因',
    refund_status       TINYINT           NOT NULL DEFAULT 0        COMMENT '退款状态(0待退款 10成功 20失败)',
    success_time        DATETIME          DEFAULT NULL              COMMENT '退款成功时间',
    user_ip             VARCHAR(50)       DEFAULT NULL              COMMENT '用户IP',
    channel_error_code  VARCHAR(64)       DEFAULT NULL              COMMENT '渠道错误码',
    channel_error_msg   VARCHAR(255)      DEFAULT NULL              COMMENT '渠道错误信息',
    channel_notify_data TEXT              DEFAULT NULL              COMMENT '渠道回调数据',
    -- 审计字段
    create_by_id        BIGINT(20)        DEFAULT 0                 COMMENT '创建者Id',
    create_by_name      VARCHAR(64)       DEFAULT ''                COMMENT '创建者名称',
    create_time         DATETIME          NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by_id        BIGINT(20)        DEFAULT 0                 COMMENT '更新者Id',
    update_by_name      VARCHAR(64)       DEFAULT ''                COMMENT '更新者名称',
    update_time         DATETIME          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark              VARCHAR(500)      DEFAULT NULL              COMMENT '备注',
    PRIMARY KEY (refund_id),
    UNIQUE KEY uk_refund_no (refund_no),
    KEY idx_order_id (order_id),
    KEY idx_user_id (user_id),
    KEY idx_refund_status (refund_status)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='支付退款表';
```

### 3.2 支付流程设计

#### 3.2.1 支付发起流程
```
业务系统 → PayOrderApi.createOrder() → 创建PayOrderDO(状态:WAITING)
         ↓
业务系统 → PayOrderApi.submitOrder() → 创建PayOrderExtensionDO
         ↓
         → PayClient.unifiedOrder() → 调用微信/支付宝API
         ↓
         → 返回支付参数(qrCode/url/app参数等)
         ↓
前端 → 展示支付页面/跳转支付
         ↓
前端 → 定时轮询 PayOrderApi.getOrder() 查询支付状态
```

#### 3.2.2 支付回调流程
```
微信/支付宝 → PayNotifyController.notifyOrder()
            ↓
            → PayClient.parseOrderNotify() → 解析回调数据
            ↓
            → PayOrderService.notifyOrder() → 更新订单状态
            ↓
            → 返回 success 给支付渠道
```

#### 3.2.3 退款流程
```
业务系统 → PayRefundApi.createRefund() → 创建PayRefundDO(状态:WAITING)
         ↓
         → PayClient.unifiedRefund() → 调用微信/支付宝退款API
         ↓
         → 更新退款状态
```

### 3.3 定时任务设计

#### 3.3.1 支付结果轮询任务
- **触发条件**：每分钟执行一次
- **处理逻辑**：查询创建时间超过5分钟且状态为WAITING的订单扩展单，调用PayClient.getOrder()同步状态

#### 3.3.2 超时订单关闭任务
- **触发条件**：每分钟执行一次
- **处理逻辑**：查询expire_time < now()且状态为WAITING的订单，更新为CLOSED状态

#### 3.3.3 退款结果轮询任务
- **触发条件**：每分钟执行一次
- **处理逻辑**：查询创建时间超过5分钟且状态为WAITING的退款单，调用PayClient.getRefund()同步状态

---

## 四、代码规范与注意事项

### 4.1 遵循项目代码规范
1. Service实现类继承 `ServiceImplPlus<Mapper, DO>`
2. Controller继承 `BaseControllerPlus`
3. DO实体继承 `BaseColumnEntity`
4. 使用 `R<T>` 作为统一响应封装
5. 使用 `@PreAuthorize("@ss.hasPermi('module:action')")` 进行权限控制
6. 使用 `@Log` 记录操作日志
7. 使用 `@RepeatSubmit` 防止重复提交

### 4.2 支付模块特殊注意事项
1. **金额单位**：统一使用"分"为单位存储，避免浮点数精度问题
2. **幂等性**：所有支付接口必须支持幂等调用
3. **并发控制**：使用Redis分布式锁处理并发回调
4. **日志记录**：关键操作必须记录详细日志，便于问题排查
5. **异常处理**：区分业务异常和系统异常，业务异常直接返回，系统异常记录日志后返回友好提示
6. **微信支付V3**：仅支持V3版本，移除所有V2相关代码

### 4.3 安全注意事项
1. 回调接口必须验签
2. 敏感配置（私钥等）加密存储
3. 支付金额必须从服务端获取，不能信任客户端传值
4. 订单状态变更必须使用乐观锁或分布式锁

---

## 五、开发顺序建议

建议按以下顺序进行开发：

1. **Phase 1** → 创建SQL文件和Mapper（基础）
2. **Phase 2** → 实现Service层（核心）
3. **Phase 3** → 实现Controller层（接口）
4. **Phase 4** → 实现定时任务（补偿）
5. **Phase 5** → 完善API层（集成）
6. **Phase 6** → 测试验证（质量）

---

## 六、风险与依赖

### 6.1 外部依赖
- 微信支付SDK：`weixin-java-pay`（V3版本）
- 支付宝SDK：`alipay-sdk-java`

### 6.2 潜在风险
1. 支付宝证书模式和公钥模式需要分别处理
2. 回调并发问题需要分布式锁保护
3. 网络超时需要重试机制

---

*文档版本：v2.0*
*创建时间：2026-03-21*
*最后更新：2026-03-21*
