-- 支付渠道配置
drop table if exists pay_channel;
create table pay_channel
(
    channel_id            bigint(20)        not null auto_increment   comment '支付渠道id',
    channel_code          varchar(32)       not null                  comment '渠道编码',
    channel_name          varchar(64)       not null default ''       comment '渠道名称',
    channel_config        text              null                      comment '渠道配置(JSON格式)',
    channel_fee_rate      decimal(5, 2)     default 0                 comment '渠道手续费，单位：百分比',
    channel_fee_price     decimal(12, 2)    default 0                 comment '渠道手续金额，单位：元',
    status                tinyint           default 0                 comment '状态（0正常 1停用）',
    create_by_id          bigint(20)        default 0                 comment '创建者Id',
    create_by_name        varchar(64)       default ''                comment '创建者名称',
    create_time           datetime not null default current_timestamp comment '创建时间',
    update_by_id          bigint(20)        default 0                 comment '更新者Id',
    update_by_name        varchar(64)       default ''                comment '更新者名称',
    update_time           datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    remark                varchar(500)      default null              comment '备注',
    primary key (channel_id)
) engine=innodb auto_increment=200 comment = '支付渠道表';

-- 支付订单信息
drop table if exists pay_order;
create table pay_order
(
    order_id              bigint(20)        not null auto_increment   comment '支付订单id',
    order_no              varchar(64)       not null                  comment '支付订单号',
    channel_id            bigint(20)        not null                  comment '渠道id',
    extension_id          bigint(20)        default 0                 comment '支付信息拓展id',
    success_time          datetime          null                      comment '支付成功时间',
    expire_time           datetime          null                      comment '订单过期时间',
    order_status          tinyint           not null                  comment '支付状态',
    pay_price             decimal(12, 2)    not null                  comment '支付金额，单位：元',
    refund_price          decimal(12, 2)    not null                  comment '退款总金额，单位：元',
    create_by_id          bigint(20)        default 0                 comment '创建者Id',
    create_by_name        varchar(64)       default ''                comment '创建者名称',
    create_time           datetime not null default current_timestamp comment '创建时间',
    update_by_id          bigint(20)        default 0                 comment '更新者Id',
    update_by_name        varchar(64)       default ''                comment '更新者名称',
    update_time           datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    remark                varchar(500)      default null              comment '备注',
    primary key (order_id)
) engine=innodb auto_increment=200 comment = '支付订单信息表';

-- 支付订单扩展信息
drop table if exists pay_order_extension;
create table pay_order_extension
(
    order_extension_id    bigint(20)        not null auto_increment   comment '订单扩展id',
    order_id              bigint(20)        not null                  comment '支付订单id',
    order_no              varchar(64)       not null                  comment '支付订单号',
    channel_id            bigint(20)        not null                  comment '渠道id',
    channel_order_no      varchar(128)      not null                  comment '渠道订单号',
    channel_header        text              null                      comment '渠道异步通知响应头',
    channel_body          text              null                      comment '渠道异步通知响应体',
    channel_error_code    varchar(128)      not null                  comment '渠道调用报错时，错误码',
    channel_error_msg     varchar(256)      not null                  comment '渠道调用报错时，错误信息',
    order_status          tinyint           not null                  comment '支付状态',
    user_id               bigint(20)        not null                  comment '用户id',
    user_ip               varchar(64)       not null                  comment '用户ip',
    create_by_id          bigint(20)        default 0                 comment '创建者Id',
    create_by_name        varchar(64)       default ''                comment '创建者名称',
    create_time           datetime not null default current_timestamp comment '创建时间',
    update_by_id          bigint(20)        default 0                 comment '更新者Id',
    update_by_name        varchar(64)       default ''                comment '更新者名称',
    update_time           datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    remark                varchar(500)      default null              comment '备注',
    primary key (order_extension_id)
) engine=innodb auto_increment=200 comment = '支付订单扩展信息表';

-- 支付退款信息
drop table if exists pay_refund;
create table pay_refund
(
    pay_refund_id         bigint(20)        not null auto_increment   comment '支付退款编号',
    order_id              bigint(20)        not null                  comment '支付订单编号 pay_order 表id',
    refund_no             varchar(64)       not null                  comment '退款单号',
    pay_price             decimal(12, 2)    not null                  comment '支付金额，单位分',
    refund_price          decimal(12, 2)    not null                  comment '退款金额，单位分',
    refund_reason         varchar(256)      null                      comment '退款原因',
    refund_success_time   datetime          null                      comment '退款成功时间',
    channel_id            bigint(20)        not null                  comment '渠道id',
    channel_order_no      varchar(128)      not null                  comment '渠道订单号，pay_order 中的 channel_order_no 对应',
    channel_refund_no     varchar(128)      not null                  comment '渠道退款单号，渠道返回',
    channel_error_code    varchar(128)      not null                  comment '渠道调用报错时，错误码',
    channel_error_msg     varchar(256)      not null                  comment '渠道调用报错时，错误信息',
    channel_notify_data   varchar(4096)     not null                  comment '渠道异步通知的内容',
    refund_status         tinyint           not null                  comment '退款状态',
    create_by_id          bigint(20)        default 0                 comment '创建者Id',
    create_by_name        varchar(64)       default ''                comment '创建者名称',
    create_time           datetime not null default current_timestamp comment '创建时间',
    update_by_id          bigint(20)        default 0                 comment '更新者Id',
    update_by_name        varchar(64)       default ''                comment '更新者名称',
    update_time           datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    remark                varchar(500)      default null              comment '备注',
    primary key (pay_refund_id)
) engine=innodb auto_increment=200 comment = '支付退款信息表';
