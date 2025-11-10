insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by_id, create_by_name, create_time, update_by_id, update_by_name, update_time, remark)
values('AI智能体', '0', '1', 'ai', 'ai/dialog/index', 1, 0, 'C', '0', '0', 'ai:chat:list', '#', 1, 'admin', sysdate(), 1, 'admin', sysdate(), 'AI智能体菜单');
