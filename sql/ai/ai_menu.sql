INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by_id`, `create_by_name`, `create_time`, `update_by_id`, `update_by_name`, `update_time`, `remark`) VALUES (3000, 'AI管理', 0, 5, 'ai', NULL, NULL, '', 1, 0, 'M', '0', '0', '', '#', 1, 'admin', '2025-11-13 10:00:37', 1, 'admin', '2025-11-13 10:00:49', '');

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by_id`, `create_by_name`, `create_time`, `update_by_id`, `update_by_name`, `update_time`, `remark`) VALUES (3001, 'AI智能体', 3000, 1, 'aiDialog', 'ai/dialog/index', NULL, '', 1, 0, 'C', '0', '0', 'ai:dialog:list', '#', 1, 'admin', '2025-10-30 13:39:47', 1, 'admin', '2025-11-13 10:14:39', 'AI智能体菜单');


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('会话历史', '3000', '1', 'aiChatHistory', 'ai/chatHistory/index', 1, 0, 'C', '0', '0', 'ai:chatHistory:list', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '会话历史菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('会话历史查询', @parentId, '1',  '#', '', 1, 1, 'F', '0', '0', 'ai:chatHistory:query', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('会话历史新增', @parentId, '2',  '#', '', 1, 1, 'F', '0', '0', 'ai:chatHistory:add', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('会话历史修改', @parentId, '3',  '#', '', 1, 1, 'F', '0', '0', 'ai:chatHistory:edit', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('会话历史删除', @parentId, '4',  '#', '', 1, 1, 'F', '0', '0', 'ai:chatHistory:remove', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('会话历史导出', @parentId, '5',  '#', '', 1, 1, 'F', '0', '0', 'ai:chatHistory:export', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('会话历史导入', @parentId, '6',  '#', '', 1, 1, 'F', '0', '0', 'ai:chatHistory:import', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('会话详细', '3000', '1', 'aiChatDetail', 'ai/chatDetail/index', 1, 0, 'C', '0', '0', 'ai:chatDetail:list', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '会话详细菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('会话详细查询', @parentId, '1',  '#', '', 1, 1, 'F', '0', '0', 'ai:chatDetail:query', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('会话详细新增', @parentId, '2',  '#', '', 1, 1, 'F', '0', '0', 'ai:chatDetail:add', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('会话详细修改', @parentId, '3',  '#', '', 1, 1, 'F', '0', '0', 'ai:chatDetail:edit', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('会话详细删除', @parentId, '4',  '#', '', 1, 1, 'F', '0', '0', 'ai:chatDetail:remove', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('会话详细导出', @parentId, '5',  '#', '', 1, 1, 'F', '0', '0', 'ai:chatDetail:export', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('会话详细导入', @parentId, '6',  '#', '', 1, 1, 'F', '0', '0', 'ai:chatDetail:import', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), '');
