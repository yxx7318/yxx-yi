-- ----------------------------
-- 代码生成测试表
-- ----------------------------
drop table if exists `tb_test_user`;
create table `tb_test_user` (
  user_id           bigint(20)      not null auto_increment    comment '用户ID',
  user_name         varchar(30)     not null                   comment '用户账号',
  password          varchar(100)    default ''                 comment '密码',
  status            char(1)         default '0'                comment '帐号状态（0正常 1停用）',
  register_time 	datetime                                   comment '注册时间',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (user_id)
) engine=innodb auto_increment=100 comment = '测试用户表';


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('测试用户', '0', '1', 'businessUser', 'business/user/index', 1, 0, 'C', '0', '0', 'business:user:list', '#', 'admin', sysdate(), '', null, '测试用户菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('测试用户查询', @parentId, '1',  '#', '', 1, 1, 'F', '0', '0', 'business:user:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('测试用户新增', @parentId, '2',  '#', '', 1, 1, 'F', '0', '0', 'business:user:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('测试用户修改', @parentId, '3',  '#', '', 1, 1, 'F', '0', '0', 'business:user:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('测试用户删除', @parentId, '4',  '#', '', 1, 1, 'F', '0', '0', 'business:user:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('测试用户导出', @parentId, '5',  '#', '', 1, 1, 'F', '0', '0', 'business:user:export',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('测试用户导入', @parentId, '6',  '#', '', 1, 1, 'F', '0', '0', 'business:user:import',       '#', 'admin', sysdate(), '', null, '');
