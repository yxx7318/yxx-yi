package com.yxx.framework.config;

import java.util.concurrent.TimeUnit;

import com.yxx.common.core.domain.properties.ResourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.yxx.common.config.YxxConfig;
import com.yxx.common.constant.Constants;
import com.yxx.framework.interceptor.RepeatSubmitInterceptor;

/**
 * 通用配置
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer
{
    @Autowired
    private RepeatSubmitInterceptor repeatSubmitInterceptor;

    @Autowired
    private ResourceProperties resourceProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        /** 本地文件上传路径 */
        registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**")
                .addResourceLocations("file:" + YxxConfig.getProfile() + "/");

        /** swagger配置 */
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .setCacheControl(CacheControl.maxAge(5, TimeUnit.HOURS).cachePublic());

        /** knife4j配置 */
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .setCacheControl(CacheControl.maxAge(5, TimeUnit.HOURS).cachePublic());
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/")
                .setCacheControl(CacheControl.maxAge(5, TimeUnit.HOURS).cachePublic());

        if (resourceProperties.getEnabled())
        {
            /** 静态资源配置 */
            resourceProperties.getResources().forEach(resource ->
                    registry.addResourceHandler(String.format("/%s/**", resource))
                            .addResourceLocations(String.format("classpath:/%s/", resource)));

            /** yxx-ui资源配置 */
            registry.addResourceHandler(String.format("/%s/**", resourceProperties.getPublicPath()))
                    .addResourceLocations(String.format("classpath:/%s/", resourceProperties.getUiResource()));
        }
    }

    /**
     * 使用旧的路径匹配规则
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer)
    {
        configurer.setUseTrailingSlashMatch(true);
        configurer.setPathMatcher(new AntPathMatcher());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        if (resourceProperties.getEnabled())
        {
            // 配置视图
            String publicPath = resourceProperties.getPublicPath();
            registry.addViewController("/favicon.ico")
                    .setViewName(String.format("forward:/%s/favicon.ico", publicPath));

            // 根目录匹配文件
            registry.addViewController(String.format("/%s", publicPath))
                    .setViewName(String.format("forward:/%s/index.html", publicPath));

            // 处理以 publicPath 开头的多级路径（且不包含文件扩展名）
            registry.addViewController(String.format("/%s/**/{path:^(?!.*\\..*$).*}", publicPath))
                    .setViewName(String.format("forward:/%s", publicPath));
        }
    }

    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter()
    {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置访问源地址
        config.addAllowedOriginPattern("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 有效期 1800秒
        config.setMaxAge(1800L);
        // 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 返回新的CorsFilter
        return new CorsFilter(source);
    }
}