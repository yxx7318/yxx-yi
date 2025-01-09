-- 设置变量
SET @custom_user = 'yxx_test';
SET @custom_password = 'yxx12345678*&!';
SET @custom_db = 'yxx_test';
SET @custom_host = '%';

-- 检查用户是否存在
SET @sql_check = CONCAT('SELECT EXISTS(SELECT 1 FROM mysql.user WHERE User = ''', @custom_user, ''' AND Host = ''', @custom_host, ''')');
PREPARE stmt_check FROM @sql_check;
EXECUTE stmt_check;
DEALLOCATE PREPARE stmt_check;

-- 如果用户存在，则删除用户
SET @sql_drop = CONCAT('DROP USER IF EXISTS ''', @custom_user, '''@''', @custom_host, '''');
PREPARE stmt_drop FROM @sql_drop;
EXECUTE stmt_drop;
DEALLOCATE PREPARE stmt_drop;

-- 创建用户
SET @sql1 = CONCAT('CREATE USER ''', @custom_user, '''@''', @custom_host, ''' IDENTIFIED BY ''', @custom_password, '''');
PREPARE stmt1 FROM @sql1;
EXECUTE stmt1;
DEALLOCATE PREPARE stmt1;

-- 查询MySQL版本
SELECT VERSION() INTO @mysql_version;

-- 设置字符集变量
SET @charset = IF(@mysql_version LIKE '5.7.%', 'utf8', 'utf8mb4');
SET @collate = IF(@mysql_version LIKE '5.7.%', 'utf8_general_ci', 'utf8mb4_general_ci');

-- 构建创建数据库命令
SET @sql2 = CONCAT('CREATE DATABASE IF NOT EXISTS `', @custom_db, '` CHARACTER SET ', @charset, ' COLLATE ', @collate);

-- 准备并执行SQL语句
PREPARE stmt2 FROM @sql2;
EXECUTE stmt2;
DEALLOCATE PREPARE stmt2;

-- 授予数据库所有权限
SET @sql3 = CONCAT('GRANT ALL PRIVILEGES ON `', @custom_db, '`.* TO ''', @custom_user, '''@''', @custom_host, '''');
PREPARE stmt3 FROM @sql3;
EXECUTE stmt3;
DEALLOCATE PREPARE stmt3;

-- 刷新权限
FLUSH PRIVILEGES;
