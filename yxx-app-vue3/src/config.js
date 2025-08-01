// 应用全局配置
const config = {
   baseUrl: 'https://vue.ruoyi.vip/prod-api',
  // baseUrl: 'http://localhost/prod-api',
  // baseUrl: 'http://localhost:7318',
   //cloud后台网关地址
  //  baseUrl: 'http://192.168.10.3:8080',
   // 应用信息
   appInfo: {
     // 应用名称
     name: "yxx-app-vue3",
     // 应用版本
     version: "1.1.0",
     // 应用logo
     logo: "/static/logo.png",
     // 官方网站
     site_url: "https://gitee.com/yxx7318/yxx-yi",
     // 政策协议
     agreements: [{
         title: "隐私政策",
         url: ""
       },
       {
         title: "用户服务协议",
         url: ""
       }
     ]
   }
 }

 export default config
