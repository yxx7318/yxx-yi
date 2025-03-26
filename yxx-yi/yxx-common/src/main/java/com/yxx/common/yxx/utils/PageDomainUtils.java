package com.yxx.common.yxx.utils;

import com.yxx.common.utils.StringUtils;

public class PageDomainUtils {

    /**
     * 兼容前端排序类型
     */
    public static String getIsAscValue(String isAsc) {
        if (StringUtils.isNotEmpty(isAsc)) {
            if ("ascending".equals(isAsc)) {
                return "asc";
            } else if ("descending".equals(isAsc)) {
                return "desc";
            }
        }
        return "asc";
    }
}
