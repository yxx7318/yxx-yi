package com.yxx.common.interceptor;

import com.yxx.common.constant.Constants;
import com.yxx.common.utils.MDCUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import java.io.IOException;


/**
 * RestTemplate TraceId 拦截器
 * <p>
 * restTemplate.setInterceptors(Collections.singletonList((new RestTemplateTraceIdInterceptor()));
 */
public class RestTemplateTraceIdInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        String traceId = MDCUtils.getTraceId();
        if (traceId != null) {
            httpRequest.getHeaders().set(Constants.TRACE_ID, traceId);
        }
        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }
}