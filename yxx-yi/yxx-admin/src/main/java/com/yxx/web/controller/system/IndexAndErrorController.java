package com.yxx.web.controller.system;

import com.yxx.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yxx.common.config.YxxConfig;
import com.yxx.common.utils.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * 首页和错误页面
 */
@RestController
public class IndexAndErrorController implements ErrorController
{
    /** 系统基础配置 */
    @Autowired
    private YxxConfig yxxConfig;

    /**
     * 访问首页，提示语
     */
    @RequestMapping("/")
    public String index()
    {
        return StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", yxxConfig.getName(), yxxConfig.getVersion());
    }

    @RequestMapping("/error")
    public ResponseEntity<AjaxResult> handleError(HttpServletRequest request)
    {
        // 获取错误状态码
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            HttpStatus httpStatus = HttpStatus.valueOf(statusCode);

            // 处理404错误
            if (httpStatus == HttpStatus.NOT_FOUND) {
                return ResponseEntity
                        .status(httpStatus)
                        // 获取原始的资源请求地址
                        .body(AjaxResult.error(httpStatus.value(), String.format("资源'%s'未找到", request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI))));
            }

            // 处理其他错误（如500）
            return ResponseEntity
                    .status(httpStatus)
                    .body(AjaxResult.error(httpStatus.value(), httpStatus.getReasonPhrase()));
        }

        // 未知错误（默认502 错误网关）
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(AjaxResult.error(HttpStatus.BAD_GATEWAY.value(), "服务器未知错误"));
    }
}