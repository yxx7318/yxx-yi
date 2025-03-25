package com.yxx.common.yxx.controllre;

import com.yxx.common.core.controller.BaseController;
import com.yxx.common.core.domain.R;
import com.yxx.common.core.page.TableDataInfo;
import com.yxx.common.utils.bean.BeanUtils;
import com.yxx.common.yxx.domain.PageQueryEntity;
import com.yxx.common.yxx.domain.PageResult;

import java.util.List;

public class BaseControllerPlus extends BaseController {

    /**
     * 响应请求分页数据，转换为R结果对象
     */
    protected PageResult<?> getDataTableToPR(List<?> list)
    {
        PageResult<?> result = new PageResult<>();
        TableDataInfo rspData = getDataTable(list);
        BeanUtils.copyProperties(rspData, result);
        return result;
    }

    /**
     * 响应请求分页数据，转换为R结果对象
     */
    protected <T extends PageQueryEntity> PageResult<?> getDataTableToPR(List<?> list, T t)
    {
        PageResult<?> result = getDataTableToPR(list);
        result.setPageNum(t.getPageNum());
        // 如果是所有数据，则以list长度作为页面长度
        if (t.getAllData()) {
            result.setPageSize(list.size());
        } else {
            result.setPageSize(t.getPageSize());
        }
        return result;
    }

    /**
     * 返回成功
     */
    public R<String> ok()
    {
        return R.ok();
    }

    /**
     * 返回失败消息
     */
    public R<String> fail()
    {
        return R.fail();
    }

    /**
     * 返回成功消息
     */
    public R<String> ok(String message)
    {
        return R.ok(message);
    }

    /**
     * 返回成功消息
     */
    public R<Object> ok(Object data)
    {
        return R.ok(data);
    }

    /**
     * 返回失败消息
     */
    public R<String> fail(String message)
    {
        return R.fail(message);
    }

    /**
     * 返回警告消息
     */
    public R<String> alert(String message)
    {
        return R.alert(message);
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected R<String> toResult(int rows)
    {
        return rows > 0 ? ok() : fail();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected R<String> toResult(boolean result)
    {
        return result ? ok() : fail();
    }

}
