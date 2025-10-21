package com.yxx.common.core.domain.properties;

import com.yxx.common.utils.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ConfigurationProperties(prefix = "resource")
@Component
public class ResourceProperties {

    private Boolean enabled;

    private List<String> resources;

    private String publicPath;

    private String uiResource;

    private List<String> apiPrefix;

    /**
     * 获取SpringSecurity GET 请求放行地址
     */
    public String[] getResourcesMatchers() {
        if (StringUtils.isNotEmpty(getResources())) {
            List<String> matchers = new ArrayList<>(Collections.singletonList("/favicon.ico"));
            getResources().forEach(resource -> matchers.add(String.format("/%s/**", resource)));
            matchers.add(String.format("/%s/**", getPublicPath()));
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

    public List<String> getResources() {
        return resources != null ? resources.stream().map(api -> api.replace("/", "")).collect(Collectors.toList()) : new ArrayList<>();
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
    }

    public String getPublicPath() {
        return publicPath != null ? publicPath.replace("/", "") : null;
    }

    public void setPublicPath(String publicPath) {
        this.publicPath = publicPath;
    }

    public String getUiResource() {
        return uiResource != null ? uiResource.replace("/", "") : null;
    }

    public void setUiResource(String uiResource) {
        this.uiResource = uiResource;
    }

    public List<String> getApiPrefix() {
        return apiPrefix != null ? apiPrefix.stream().map(api -> api.replace("/", "")).collect(Collectors.toList()) : new ArrayList<>();
    }

    public void setApiPrefix(List<String> apiPrefix) {
        this.apiPrefix = apiPrefix;
    }
}
