insert into `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by_id`, `create_by_name`, `create_time`, `update_by_id`, `update_by_name`, `update_time`, `remark`)
values (300, '消息类型', 'ai_message_type', '0', 1, 'admin', '2026-01-21 16:08:46', 1, 'admin', '2026-01-21 16:09:11', '消息类型列表');


insert into `sys_dict_data` (`dict_data_id`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by_id`, `create_by_name`, `create_time`, `update_by_id`, `update_by_name`, `update_time`, `remark`)
values (301, 1, 'user', '1', 'ai_message_type', NULL, 'primary', 'N', '0', 1, 'admin', '2026-01-21 16:09:50', 0, '', '2026-01-21 16:09:50', NULL);
insert into `sys_dict_data` (`dict_data_id`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by_id`, `create_by_name`, `create_time`, `update_by_id`, `update_by_name`, `update_time`, `remark`)
values (302, 2, 'assistant', '2', 'ai_message_type', NULL, 'success', 'N', '0', 1, 'admin', '2026-01-21 16:10:05', 1, 'admin', '2026-01-21 16:10:08', NULL);
insert into `sys_dict_data` (`dict_data_id`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by_id`, `create_by_name`, `create_time`, `update_by_id`, `update_by_name`, `update_time`, `remark`)
values (303, 3, 'system', '3', 'ai_message_type', NULL, 'warning', 'N', '0', 1, 'admin', '2026-01-21 16:10:20', 0, '', '2026-01-21 16:10:19', NULL);
insert into `sys_dict_data` (`dict_data_id`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by_id`, `create_by_name`, `create_time`, `update_by_id`, `update_by_name`, `update_time`, `remark`)
values (304, 4, 'tool', '4', 'ai_message_type', NULL, 'info', 'N', '0', 1, 'admin', '2026-01-21 16:10:33', 0, '', '2026-01-21 16:10:33', NULL);
