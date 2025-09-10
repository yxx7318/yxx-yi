package com.yxx.common.filter;

import com.yxx.common.constant.Constants;
import com.yxx.common.utils.MDCUtils;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TraceId过滤器
 */
public class TraceFilter implements Filter
{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        //如果有上层调用就用上层的ID
        String traceId = req.getHeader(Constants.TRACE_HEADER);
        if (traceId == null)
        {
            traceId = MDCUtils.setTraceIdIfAbsent();
        }
        resp.setHeader(Constants.TRACE_HEADER, traceId);
        try
        {
            chain.doFilter(request, response);
        }
        finally
        {
            MDCUtils.removeTraceId();
        }
    }

    @Override
    public void destroy()
    {

    }
}