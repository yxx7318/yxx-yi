-- 发票信息表
drop table if exists ai_invoice;
create table ai_invoice
(
    invoice_id        bigint(20)        not null auto_increment   comment '发票id',
    invoice_number    varchar(100)      default ''                comment '发票号码',
    amount            decimal(20,4)     default 0                 comment '发票金额',
    invoice_date      datetime                                    comment '开票日期',
    create_by_id      bigint(20)        default 0                 comment '创建者Id',
    create_by_name    varchar(64)       default ''                comment '创建者名称',
    create_time       datetime not null default current_timestamp comment '创建时间',
    update_by_id      bigint(20)        default 0                 comment '创建者Id',
    update_by_name    varchar(64)       default ''                comment '更新者名称',
    update_time       datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    remark            varchar(500)      default null              comment '备注',
    primary key (invoice_id)
) engine=innodb auto_increment=200 comment = '会话历史表';