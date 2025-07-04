package com.yxx.web.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
//import org.springdoc.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * springdoc-openapi 配置
 */
@Slf4j
@Configuration
public class OpenapiConfig {

    @Value("com.yxx.business")
    private String businessPath;

    @Bean
    public OpenAPI api() {
        // 联系人信息(contact)，构建API的联系人信息，用于描述API开发者的联系信息，包括名称、URL、邮箱等
        Contact contact = new Contact()
                .name("YXX")  // 作者名称
                .email("1303490776@qq.com") // 作者邮箱
                .url("https://gitee.com/yxx7318"); // 介绍作者的URL地址

        License license = new License()
                .name("MIT")                         // 授权名称
                .url("https://www.apache.org/licenses/LICENSE-2.0.html")    // 授权信息
                .identifier("MIT");                   // 标识授权许可

        //创建Api帮助文档的描述信息、联系人信息(contact)、授权许可信息(license)
        Info info = new Info()
                .title("YXX Api接口文档标题")      // Api接口文档标题（必填）
                .description("项目描述")     // Api接口文档描述
                .version("1.0.0")                                  // Api接口版本
                .termsOfService("https://gitee.com/yxx7318/yxx-yi")    // Api接口的服务条款地址
                .license(license)  //   授权名称
                .contact(contact); // 设置联系人信息

        //多服务
        List<Server> servers = new ArrayList<>();
        // 表示服务器地址或者URL模板列表，多个服务地址随时切换（只不过是有多台IP有当前的服务API）
        servers.add(new Server().url("/"));
        servers.add(new Server().url("/dev-api/").description("开发环境API"));
        servers.add(new Server().url("/test-api/").description("测试环境API"));
        servers.add(new Server().url("/prod-api/").description("正式环境API"));
        servers.add(new Server().url("https://dev.domain.com/dev-api/").description("开发环境"));
        servers.add(new Server().url("https://test.domain.com/test-api/").description("测试环境"));
        servers.add(new Server().url("https://prod.domain.com/test-api/").description("正式环境"));

        // 设置 spring security apikey accessToken 认证的请求头 Authorization: Bearer xxxxxxxx
        SecurityScheme securityScheme = new SecurityScheme()
                .scheme("Bearer")                     // 指定认证方案类型为 Bearer（即 Token 认证）
                .name("Authorization")               // 定义请求头名称为 "Authorization"
                .type(SecurityScheme.Type.APIKEY)    // 安全方案类型为 APIKEY（通过请求头传递 Token）
                .description("请输入token,格式为[Bearer xxxxxxxx]")  // 描述认证格式
                .in(SecurityScheme.In.HEADER);       // 指定 Token 传递位置为请求头（HEADER）

        // 将定义好的 SecurityScheme（Bearer Token 认证方案）注册到 OpenAPI 的 Components 组件中
        Components components = new Components().addSecuritySchemes("Authorization", securityScheme);

        // 创建全局安全需求对象，声明所有 API 接口需要使用 "Authorization" 安全方案进行认证
        SecurityRequirement authorization = new SecurityRequirement().addList("Authorization");

        return new OpenAPI()
                .components(components)
                .info(info)
                .addSecurityItem(authorization)
                .servers(servers);
    }

//    @Bean
//    public GroupedOpenApi businessApi() {
//        return GroupedOpenApi.builder()
//                .group("业务接口(Business)")
//                .packagesToScan(businessPath)
//                .build();
//
//    }
//
//    @Bean
//    public GroupedOpenApi supportApi() {
//        return GroupedOpenApi.builder()
//                .group("支撑接口(Support)")
//                .pathsToMatch("/**")
//                .packagesToExclude(businessPath)
//                .build();
//    }
}
