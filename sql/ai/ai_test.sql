-- 发票信息表
drop table if exists ai_invoice_info;
create table ai_invoice_info
(
    invoice_info_id   bigint(20)        not null auto_increment   comment '发票id',
    invoice_number    varchar(100)      not null                  comment '发票号码',
    amount            decimal(20,4)     not null                  comment '发票金额',
    invoice_date      datetime          not null                  comment '开票日期',
    create_by_id      bigint(20)        default 0                 comment '创建者Id',
    create_by_name    varchar(64)       default ''                comment '创建者名称',
    create_time       datetime not null default current_timestamp comment '创建时间',
    update_by_id      bigint(20)        default 0                 comment '更新者Id',
    update_by_name    varchar(64)       default ''                comment '更新者名称',
    update_time       datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    remark            varchar(500)      default null              comment '备注',
    primary key (invoice_info_id)
) engine=innodb auto_increment=200 comment = '发票信息表';

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('发票信息', '3000', '5', 'aiInvoiceInfo', 'ai/invoiceInfo/index', 1, 0, 'C', '0', '0', 'ai:invoiceInfo:list', 'excel', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '发票信息菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('发票信息查询', @parentId, '1',  '#', '', 1, 1, 'F', '0', '0', 'ai:invoiceInfo:query', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('发票信息新增', @parentId, '2',  '#', '', 1, 1, 'F', '0', '0', 'ai:invoiceInfo:add', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('发票信息修改', @parentId, '3',  '#', '', 1, 1, 'F', '0', '0', 'ai:invoiceInfo:edit', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('发票信息删除', @parentId, '4',  '#', '', 1, 1, 'F', '0', '0', 'ai:invoiceInfo:remove', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('发票信息导出', @parentId, '5',  '#', '', 1, 1, 'F', '0', '0', 'ai:invoiceInfo:export', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('发票信息导入', @parentId, '6',  '#', '', 1, 1, 'F', '0', '0', 'ai:invoiceInfo:import', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');