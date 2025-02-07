package com.yxx.common.yxx.domain;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxx.common.constant.HttpStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private int code = HttpStatus.SUCCESS;

    @ApiModelProperty(value = "消息", example = "操作成功")
    private String msg = "查询成功";

    /**
     * 将MyBatis-Plus的Page对象转换为PageDTO对象，并使用BeanUtils进行属性拷贝。
     *
     * @param <PO>    原始实体类型
     * @param <VO>    目标视图类型
     * @param p       MyBatis-Plus的Page对象
     * @param voClass 目标视图类型的Class对象
     * @return 转换后的PageDTO对象
     */
    public static <PO, VO> PageResult<VO> of(Page<PO> p, Class<VO> voClass) {
        PageResult<VO> dto = dealWith(p);

        List<PO> records = p.getRecords();
        // 如果记录结果为空
        if (records == null || records.isEmpty()) {
            dto.setRows(Collections.emptyList());
            return dto;
        }

        // 拷贝VO
        dto.setRows(BeanUtil.copyToList(records, voClass));
        return dto;
    }

    /**
     * 将MyBatis-Plus的Page对象转换为PageDTO对象，并使用自定义转换器进行转换。
     *
     * @param <PO>      原始实体类型
     * @param <VO>      目标视图类型
     * @param p         MyBatis-Plus的Page对象
     * @param convertor 自定义转换器，用于将原始实体转换为目标视图类型
     * @return 转换后的PageDTO对象
     */
    public static <PO, VO> PageResult<VO> of(Page<PO> p, Function<PO, VO> convertor) {
        PageResult<VO> dto = dealWith(p);

        List<PO> records = p.getRecords();
        // 如果记录结果为空
        if (records == null || records.isEmpty()) {
            dto.setRows(Collections.emptyList());
            return dto;
        }

        // 使用转换器拷贝VO
        dto.setRows(records.stream().map(convertor).collect(Collectors.toList()));
        return dto;
    }

    /**
     * 处理Page对象的基本信息（总记录数、总页数），并创建一个对应的PageDTO对象。
     *
     * @param <PO> 原始实体类型
     * @param <VO> 目标视图类型
     * @param p    MyBatis-Plus的Page对象
     * @return 转换后的PageDTO对象
     */
    private static <VO, PO> PageResult<VO> dealWith(Page<PO> p) {
        PageResult<VO> dto = new PageResult<>();
        dto.setTotal(p.getTotal());
        return dto;
    }
}