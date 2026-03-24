package com.yxx.common.core.domain.properties;

import com.yxx.common.utils.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ConfigurationProperties(prefix = "vue-resource")
@Component
public class ResourceProperties {

    private Boolean enabled;

    private String publicPath;

    private String vueResource;

    private List<String> apiPrefix;

    private List<String> resources;

    /**
     * 获取SpringSecurity GET 请求放行地址
     */
    public String[] getResourcesMatchers() {
        if (StringUtils.isNotEmpty(getResources())) {
            List<String> matchers = new ArrayList<>(Collections.singletonList("/favicon.ico"));
            getResources().forEach(resource -> matchers.add(String.format("/%s/**", resource)));
            matchers.add(String.format("/%s/**", getPublicPath()));
            matchers.add(String.format("/%s/**", getVueResource()));
            return matchers.toArray(new String[0]);
        }
        return new String[0];
    }

    /**
     * 获取SpringSecurity 重写请求放行地址
     */
    public String[] getApiMatchers() {
        if (StringUtils.isNotEmpty(getApiPrefix())) {
            List<String> matchers = new ArrayList<>();
            getApiPrefix().forEach(resource -> matchers.add(String.format("/%s/**", resource)));
            return matchers.toArray(new String[0]);
        }
        return new String[0];
    }

    public Boolean getEnabled() {
        return Boolean.TRUE.equals(enabled);
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getPublicPath() {
        return publicPath;
    }

    public void setPublicPath(String publicPath) {
        this.publicPath = publicPath.replace("/", "");
    }

    public String getVueResource() {
        return vueResource;
    }

    public void setVueResource(String vueResource) {
        this.vueResource = vueResource.replace("/", "");
    }

    public List<String> getApiPrefix() {
        return apiPrefix;
    }

    public void setApiPrefix(List<String> apiPrefix) {
        this.apiPrefix = apiPrefix.stream().map(api -> api.replace("/", "")).collect(Collectors.toList());
    }

    public List<String> getResources() {
        return resources;
    }

    public void setResources(List<String> resources) {
        this.resources = resources.stream().map(api -> api.replace("/", "")).collect(Collectors.toList());
    }

}
