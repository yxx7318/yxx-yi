package com.yxx.common.core.domain;

import java.io.Serializable;
import com.yxx.common.constant.HttpStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 响应信息主体
 *
 * @author ruoyi
 */
@ApiModel(value = "R", description = "通用结果相应类")
public class R<T> implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 成功 */
    public static final int SUCCESS = HttpStatus.SUCCESS;

    /** 失败 */
    public static final int FAIL = HttpStatus.ERROR;

    /** 失败 */
    public static final int WARN = HttpStatus.WARN;

    @ApiModelProperty(value = "状态码", example = "200")
    private int code;

    @ApiModelProperty(value = "消息", example = "操作成功")
    private String msg;

    @ApiModelProperty("数据体")
    private T data;

    public static <T> R<T> ok()
    {
        return restResult(null, SUCCESS, "操作成功");
    }

    public static <T> R<T> ok(T data)
    {
        return restResult(data, SUCCESS, "操作成功");
    }

    public static <T> R<T> ok(T data, String msg)
    {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> R<T> fail()
    {
        return restResult(null, FAIL, "操作失败");
    }

    public static <T> R<T> fail(String msg)
    {
        return restResult(null, FAIL, msg);
    }

    public static <T> R<T> fail(T data)
    {
        return restResult(data, FAIL, "操作失败");
    }

    public static <T> R<T> fail(T data, String msg)
    {
        return restResult(data, FAIL, msg);
    }

    public static <T> R<T> fail(int code, String msg)
    {
        return restResult(null, code, msg);
    }

    public static <T> R<T> alert()
    {
        return restResult(null, WARN, "操作失败");
    }

    public static <T> R<T> alert(String msg)
    {
        return restResult(null, WARN, msg);
    }

    public static <T> R<T> alert(T data)
    {
        return restResult(data, WARN, "操作失败");
    }

    public static <T> R<T> alert(T data, String msg)
    {
        return restResult(data, WARN, msg);
    }

    public static <T> R<T> alert(int code, String msg)
    {
        return restResult(null, code, msg);
    }

    private static <T> R<T> restResult(T data, int code, String msg)
    {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public static <T> Boolean isFail(R<T> ret)
    {
        return !isOk(ret);
    }

    public static <T> Boolean isOk(R<T> ret)
    {
        return R.SUCCESS == ret.getCode();
    }
}
