-- ----------------------------
-- 代码生成测试主表
-- ----------------------------
drop table if exists `tb_test_user_sub`;
create table `tb_test_user_sub`
(
  sub_id            bigint(20)        not null auto_increment   comment '主表ID',
  user_name         varchar(30)       not null                  comment '用户账号',
  password          varchar(100)      default ''                comment '密码',
  status            char(1)           default '0'               comment '账号状态（0正常 1停用）',
  register_date     date                                        comment '注册日期',
  register_time     datetime                                    comment '注册时间',
  create_by_id      bigint(20)        default 0                 comment '创建者Id',
  create_by_name    varchar(64)       default ''                comment '创建者名称',
  create_time       datetime not null default current_timestamp comment '创建时间',
  update_by_id      bigint(20)        default 0                 comment '更新者Id',
  update_by_name    varchar(64)       default ''                comment '更新者名称',
  update_time       datetime not null default current_timestamp on update current_timestamp comment '更新时间',
  remark            varchar(500)      default null              comment '备注',
  primary key (sub_id)
) engine=innodb auto_increment=100 comment = '测试主表生成';


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('测试主表生成', '0', '1', 'businessUserSub', 'business/userSub/index', 1, 0, 'C', '0', '0', 'business:userSub:list', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '测试主表生成菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('测试主表生成查询', @parentId, '1',  '#', '', 1, 1, 'F', '0', '0', 'business:userSub:query', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('测试主表生成新增', @parentId, '2',  '#', '', 1, 1, 'F', '0', '0', 'business:userSub:add', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('测试主表生成修改', @parentId, '3',  '#', '', 1, 1, 'F', '0', '0', 'business:userSub:edit', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('测试主表生成删除', @parentId, '4',  '#', '', 1, 1, 'F', '0', '0', 'business:userSub:remove', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('测试主表生成导出', @parentId, '5',  '#', '', 1, 1, 'F', '0', '0', 'business:userSub:export', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('测试主表生成导入', @parentId, '6',  '#', '', 1, 1, 'F', '0', '0', 'business:userSub:import', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');
