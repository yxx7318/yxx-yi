package com.yxx.common.yxx.domain;

import com.yxx.common.constant.HttpStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 分页数据传输对象，用于封装分页查询的结果
 *
 * @param <T> 实际的数据类型
 */
@ApiModel(value = "PageResult", description = "分页结果实体类")
@Data
public class PageResult<T> {

    @ApiModelProperty("总记录数")
    private Long total;

    @ApiModelProperty("当前页的数据列表")
    private List<T> rows;

    @ApiModelProperty(value = "状态码", example = "200")
    private Integer code = HttpStatus.SUCCESS;

    @ApiModelProperty(value = "消息", example = "操作成功")
    private String msg = "查询成功";

    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "页大小", example = "10")
    private Integer pageSize = 10;
}