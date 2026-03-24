package com.yxx.web.controller.system;

import com.yxx.common.core.domain.properties.ResourceProperties;
import com.yxx.common.utils.ServletUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "vue-resource", name = "enabled", havingValue = "true")
public class VueResourceViewController {

    private final ResourceProperties resourceProperties;

    /**
     * controller先触发，再触发静态资源的HandlerMapping
     */
    @GetMapping("${vue-resource.public-path}/**")
    public String vueResourceView() {
        String requestURI = ServletUtils.getRequestURI().replace("/" + resourceProperties.getPublicPath(), "");
        String[] pathSplit = requestURI.split("/");
        if ("/".equals(requestURI) || !pathSplit[pathSplit.length - 1].contains(".")) {
            // 匹配到请求"/system/role"的路径，重定向给index.html
            return String.format("forward:/%s/index.html", resourceProperties.getVueResource());
        } else {
            // 如果是静态资源，则重定向给目标路径
            return String.format("forward:/%s", resourceProperties.getVueResource()) + requestURI;
        }
    }
}