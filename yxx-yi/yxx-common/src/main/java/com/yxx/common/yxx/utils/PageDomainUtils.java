package com.yxx.common.yxx.utils;

import com.yxx.common.utils.StringUtils;

public class PageDomainUtils {

    /**
     * 兼容前端排序类型
     */
    public static String getIsAscValue(String isAsc) {
        if (StringUtils.isNotEmpty(isAsc)) {
            if ("ascending".equals(isAsc)) {
                isAsc = "asc";
            } else if ("descending".equals(isAsc)) {
                isAsc = "desc";
            }
            return isAsc;
        }
        return "asc";
    }
}
