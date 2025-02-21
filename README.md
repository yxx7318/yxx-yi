## 功能优化

- 优化登录、注册界面，增加对手机端的兼容适配
- 优化模块依赖关系，汇总模块为yxx-yxx模块，可以直接在此模块下编写业务代码
- 新增多环境打包，多yaml文件和多环境sql，可以直接通过maven控制打包
- 增加MyBatisPlus支持，封装新的分页逻辑
- 增加Redisson，全局唯一ID生成、分布式事务、lua脚本分布式锁、分布式队列工具
- 优化druid连接池，合理分配不同环境连接线程，新增连接超时配置
- 优化前端多环节打包、统一前后端环境名称、优化前端debug超时问题
- 修复前端缺乏代理而无法使用preview模式的问题
- 优化后端控制台和日志输出，更加详尽并支持IDEA跳转，支持多环境日志级别
- 增加hutool依赖，封装常用的JsonUtils、ObjectUtils等工具类
- 优化异步线程池，支持函数式编程
- 增加编程式事务支持，可以使用函数式编程
- 优化Session存储，使用Redis作为存储中介
- 优化swagger接口文档的访问安全
- 增加项目部署nginx.conf配置文件，优化windows端和linux端项目启停控制脚本
- 优化代码生成器，支持在代码生成时增加swagger注解
- 优化通用控制器、json规则
- 定时任务优化返回结果和日志内容
- 优化系统监控，新增应用程序监控后端代码

## 待优化
- 前端登录注册逻辑统一
- 代码生成器对excel导入功能的兼容
- 后端代码生成器对excel导入功能的完善

## 优化效果

![image-20250113141947967](img/README/image-20250113141947967.png)

![image-20250113142022464](img/README/image-20250113142022464.png)


## 平台简介

若依是一套全部开源的快速开发平台，毫无保留给个人及企业免费使用。

* 前端采用Vue、Element UI。
* 后端采用Spring Boot、Spring Security、Redis & Jwt。
* 权限认证使用Jwt，支持多终端认证系统。
* 支持加载动态权限菜单，多方式轻松权限控制。
* 高效率开发，使用代码生成器可以一键生成前后端代码。
* 提供了技术栈（[Vue3](https://v3.cn.vuejs.org) [Element Plus](https://element-plus.org/zh-CN) [Vite](https://cn.vitejs.dev)）版本[RuoYi-Vue3](https://gitcode.com/yangzongzhuan/RuoYi-Vue3)，保持同步更新。
* 提供了单应用版本[RuoYi-Vue-fast](https://gitcode.com/yangzongzhuan/RuoYi-Vue-fast)，Oracle版本[RuoYi-Vue-Oracle](https://gitcode.com/yangzongzhuan/RuoYi-Vue-Oracle)，保持同步更新。
* 不分离版本，请移步[RuoYi](https://gitee.com/y_project/RuoYi)，微服务版本，请移步[RuoYi-Cloud](https://gitee.com/y_project/RuoYi-Cloud)

## 内置功能

1.  用户管理：用户是系统操作者，该功能主要完成系统用户配置。
2.  部门管理：配置系统组织机构（公司、部门、小组），树结构展现支持数据权限。
3.  岗位管理：配置系统用户所属担任职务。
4.  菜单管理：配置系统菜单，操作权限，按钮权限标识等。
5.  角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
6.  字典管理：对系统中经常使用的一些较为固定的数据进行维护。
7.  参数管理：对系统动态配置常用参数。
8.  通知公告：系统通知公告信息发布维护。
9.  操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
10. 登录日志：系统登录日志记录查询包含登录异常。
11. 在线用户：当前系统中活跃用户状态监控。
12. 定时任务：在线（添加、修改、删除)任务调度包含执行结果日志。
13. 代码生成：前后端代码的生成（java、html、xml、sql）支持CRUD下载 。
14. 系统接口：根据业务代码自动生成相关的api接口文档。
15. 服务监控：监视当前系统CPU、内存、磁盘、堆栈等相关信息。
16. 缓存监控：对系统的缓存信息查询，命令统计等。
17. 在线构建器：拖动表单元素生成相应的HTML代码。
18. 连接池监视：监视当前系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈。

## 在线体验

- admin/admin123

演示地址：http://meraki-e.fun/yxx-yi/
