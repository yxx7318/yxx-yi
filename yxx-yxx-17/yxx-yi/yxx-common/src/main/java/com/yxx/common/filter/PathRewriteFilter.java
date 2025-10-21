package com.yxx.common.filter;

import com.yxx.common.core.domain.properties.ResourceProperties;
import com.yxx.common.core.utils.StreamUtils;
import com.yxx.common.utils.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PathRewriteFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(PathRewriteFilter.class);

    private static final ResourceProperties RESOURCE_PROPERTIES = SpringUtils.getBean(ResourceProperties.class);

    private static final List<String> URL_PATTERNS = new ArrayList<>();

    static
    {
        RESOURCE_PROPERTIES.getApiPrefix().forEach(apiPrefix -> URL_PATTERNS.add(String.format("/%s", apiPrefix)));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI();

        Optional<String> any = StreamUtils.findAny(URL_PATTERNS, path::startsWith);
        // 重写路径
        if (any.isPresent())
        {
            String newPath = path.replaceFirst(any.get(), "");
            HttpServletRequest wrappedRequest = new HttpServletRequestWrapper(req)
            {
                @Override
                public String getRequestURI()
                {
                    return newPath;
                }

                @Override
                public String getServletPath()
                {
                    return newPath;
                }
            };
            logger.info("rewrite: [{}] ==> [{}]", path, newPath);
            chain.doFilter(wrappedRequest, response);
        }
        else
        {
            chain.doFilter(request, response);
        }
    }
}