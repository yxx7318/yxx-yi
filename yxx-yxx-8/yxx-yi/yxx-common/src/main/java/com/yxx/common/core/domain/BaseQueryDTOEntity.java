package com.yxx.common.core.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity查询基类
 */
public class BaseQueryDTOEntity extends PageQueryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 搜索值 */
    @Schema(description = "搜索值")
    private String searchValue;

    /** 请求参数 */
    @Schema(description = "请求参数", hidden = true)
    private Map<String, Object> params;

    public String getSearchValue()
    {
        return searchValue;
    }

    public void setSearchValue(String searchValue)
    {
        this.searchValue = searchValue;
    }

    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }
}
