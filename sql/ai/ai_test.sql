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


insert into `yxx_dev`.`gen_table` (`table_id`, `table_name`, `table_comment`, `sub_table_name`, `sub_table_fk_name`, `class_name`, `tpl_category`, `tpl_web_type`, `package_name`, `module_name`, `business_name`, `function_name`, `function_author`, `gen_type`, `gen_path`, `options`, `create_by_id`, `create_by_name`, `create_time`, `update_by_id`, `update_by_name`, `update_time`, `remark`) values (32, 'ai_invoice_info', '发票信息表', NULL, NULL, 'AiInvoiceInfo', 'crud', 'element-plus', 'com.yxx.ai', 'ai', 'invoiceInfo', '发票信息', 'yxx', '0', '/', '{\"parentMenuId\":3000}', 1, 'admin', '2026-01-22 11:02:50', 1, 'admin', '2026-01-27 11:37:01', '');

insert into `yxx_dev`.`gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by_id`, `create_by_name`, `create_time`, `update_by_id`, `update_by_name`, `update_time`, `remark`) values (3050, 32, 'invoice_info_id', '发票id', 'bigint', 'Long', 'invoiceInfoId', '1', '1', '0', '0', NULL, NULL, NULL, 'EQ', 'input', '', 1, 1, 'admin', '2026-01-27 11:28:57', 0, 'admin', '2026-01-27 11:28:56', '');
insert into `yxx_dev`.`gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by_id`, `create_by_name`, `create_time`, `update_by_id`, `update_by_name`, `update_time`, `remark`) values (3051, 32, 'invoice_number', '发票号码', 'varchar(100)', 'String', 'invoiceNumber', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 2, 1, 'admin', '2026-01-27 11:28:57', 0, 'admin', '2026-01-27 11:28:56', '');
insert into `yxx_dev`.`gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by_id`, `create_by_name`, `create_time`, `update_by_id`, `update_by_name`, `update_time`, `remark`) values (3052, 32, 'amount', '发票金额', 'decimal(20,4)', 'BigDecimal', 'amount', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 1, 'admin', '2026-01-27 11:28:57', 0, 'admin', '2026-01-27 11:28:56', '');
insert into `yxx_dev`.`gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by_id`, `create_by_name`, `create_time`, `update_by_id`, `update_by_name`, `update_time`, `remark`) values (3053, 32, 'invoice_date', '开票日期', 'datetime', 'LocalDateTime', 'invoiceDate', '0', '0', '1', '1', '1', '1', '1', 'BETWEEN', 'datetime', '', 4, 1, 'admin', '2026-01-27 11:28:57', 0, 'admin', '2026-01-27 11:28:56', '');
insert into `yxx_dev`.`gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by_id`, `create_by_name`, `create_time`, `update_by_id`, `update_by_name`, `update_time`, `remark`) values (3054, 32, 'create_by_id', '创建者Id', 'bigint', 'Long', 'createById', '0', '0', '0', '0', NULL, NULL, NULL, 'EQ', 'input', '', 5, 1, 'admin', '2026-01-27 11:28:57', 0, 'admin', '2026-01-27 11:28:56', '');
insert into `yxx_dev`.`gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by_id`, `create_by_name`, `create_time`, `update_by_id`, `update_by_name`, `update_time`, `remark`) values (3055, 32, 'create_by_name', '创建者', 'varchar(64)', 'String', 'createByName', '0', '0', '0', '0', NULL, '1', '1', 'LIKE', 'input', '', 6, 1, 'admin', '2026-01-27 11:28:57', 0, 'admin', '2026-01-27 11:28:56', '');
insert into `yxx_dev`.`gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by_id`, `create_by_name`, `create_time`, `update_by_id`, `update_by_name`, `update_time`, `remark`) values (3056, 32, 'create_time', '创建时间', 'datetime', 'LocalDateTime', 'createTime', '0', '0', '1', '0', NULL, '1', '1', 'EQ', 'datetime', '', 7, 1, 'admin', '2026-01-27 11:28:57', 0, 'admin', '2026-01-27 11:28:56', '');
insert into `yxx_dev`.`gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by_id`, `create_by_name`, `create_time`, `update_by_id`, `update_by_name`, `update_time`, `remark`) values (3057, 32, 'update_by_id', '更新者Id', 'bigint', 'Long', 'updateById', '0', '0', '0', '0', '0', NULL, NULL, 'EQ', 'input', '', 8, 1, 'admin', '2026-01-27 11:28:57', 0, 'admin', '2026-01-27 11:28:56', '');
insert into `yxx_dev`.`gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by_id`, `create_by_name`, `create_time`, `update_by_id`, `update_by_name`, `update_time`, `remark`) values (3058, 32, 'update_by_name', '更新者', 'varchar(64)', 'String', 'updateByName', '0', '0', '0', '0', '0', '1', '1', 'LIKE', 'input', '', 9, 1, 'admin', '2026-01-27 11:28:57', 0, 'admin', '2026-01-27 11:28:56', '');
insert into `yxx_dev`.`gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by_id`, `create_by_name`, `create_time`, `update_by_id`, `update_by_name`, `update_time`, `remark`) values (3059, 32, 'update_time', '更新时间', 'datetime', 'LocalDateTime', 'updateTime', '0', '0', '1', '0', '0', NULL, NULL, 'EQ', 'datetime', '', 10, 1, 'admin', '2026-01-27 11:28:57', 0, 'admin', '2026-01-27 11:28:56', '');
insert into `yxx_dev`.`gen_table_column` (`column_id`, `table_id`, `column_name`, `column_comment`, `column_type`, `java_type`, `java_field`, `is_pk`, `is_increment`, `is_required`, `is_insert`, `is_edit`, `is_list`, `is_query`, `query_type`, `html_type`, `dict_type`, `sort`, `create_by_id`, `create_by_name`, `create_time`, `update_by_id`, `update_by_name`, `update_time`, `remark`) values (3060, 32, 'remark', '备注', 'varchar(500)', 'String', 'remark', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'textarea', '', 11, 1, 'admin', '2026-01-27 11:28:57', 0, 'admin', '2026-01-27 11:28:56', '');


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