-- 会话历史表
drop table if exists ai_chat_history;
create table ai_chat_history
(
    chat_history_id   bigint(20)        not null auto_increment   comment '会话历史id',
    chat_group        varchar(100)      default ''                comment '会话分组',
    chat_title        varchar(200)      default ''                comment '会话标题',
    create_by_id      bigint(20)        default 0                 comment '创建者Id',
    create_by_name    varchar(64)       default ''                comment '创建者名称',
    create_time       datetime not null default current_timestamp comment '创建时间',
    update_by_id      bigint(20)        default 0                 comment '更新者Id',
    update_by_name    varchar(64)       default ''                comment '更新者名称',
    update_time       datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    remark            varchar(500)      default null              comment '备注',
    primary key (chat_history_id)
) engine=innodb auto_increment=200 comment = '会话历史表';

-- 会话详细表
drop table if exists ai_chat_detail;
create table ai_chat_detail
(
    chat_detail_id    bigint(20)        not null auto_increment   comment '会话详细id',
    chat_history_id   bigint(20)        not null                  comment '会话历史id',
    message_type      char(1)           not null                  comment '消息类型（1代表user 2代表assistant 3代表system 4代表tool）',
    content           text                                        comment '内容',
    attachment        text                                        comment '附件',
    create_by_id      bigint(20)        default 0                 comment '创建者Id',
    create_by_name    varchar(64)       default ''                comment '创建者名称',
    create_time       datetime not null default current_timestamp comment '创建时间',
    update_by_id      bigint(20)        default 0                 comment '更新者Id',
    update_by_name    varchar(64)       default ''                comment '更新者名称',
    update_time       datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    remark            varchar(500)      default null              comment '备注',
    primary key (chat_detail_id),
    key idx_chat_history_id (chat_history_id)
) engine=innodb auto_increment=200 comment = '会话详细表';