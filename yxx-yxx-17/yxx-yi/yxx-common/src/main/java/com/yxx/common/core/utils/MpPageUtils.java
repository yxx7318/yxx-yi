package com.yxx.common.core.utils;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxx.common.core.domain.PageQueryEntity;
import com.yxx.common.core.domain.PageResult;
import com.yxx.common.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页工具类
 */
public class MpPageUtils {

    /**
     * 清理分页的线程变量
     */
    public static void clearPage() {
        PageUtils.clearPage();
    }

    /**
     * @param pageNum  当前页码，如果为 null，则默认为第一页（1）
     * @param pageSize 每页大小，如果为 null，则默认为每页 10 条数据，为 0 则获取所有
     * @return 分页结果
     */
    public static <T> Page<T> getSelectPage(Integer pageNum, Integer pageSize) {
        // 创建分页对象
        Page<T> page;
        Integer targetPageNum = ObjectUtils.defaultIfNull(pageNum, 1);
        Integer targetPageSize = ObjectUtils.defaultIfNull(pageSize, 10);
        if (pageNum == 0) {
            page = new Page<>(targetPageNum, Integer.MAX_VALUE);
        } else {
            page = new Page<>(targetPageNum, targetPageSize);
        }
        return page;
    }

    /**
     * @param dto   PageQueryEntity实体类
     * @param <DTO> PageQueryEntity子类
     * @return 分页结果
     */
    public static <DTO extends PageQueryEntity, T> Page<T> getSelectPage(DTO dto) {
        // 创建分页对象
        Page<T> page;
        if (ObjectUtils.isNotNull(dto) && ObjectUtils.isNotNull(dto.getAllData()) && dto.getAllData()) {
            page = new Page<>(1, Integer.MAX_VALUE);
        } else {
            page = new Page<>(dto.getPageNum(), dto.getPageSize());
        }
        return page;
    }

    /**
     * 获取排序对象
     *
     * @param column 排序列
     * @param asc    是否asc
     * @return 排序对象
     */
    private static OrderItem initOrderItem(String column, boolean asc) {
        OrderItem orderItem = new OrderItem();
        orderItem.setColumn(column);
        orderItem.setAsc(asc);
        return orderItem;
    }

    /**
     * 将当前分页查询条件转换为MyBatis-Plus的Page对象，并根据提供的排序项进行排序
     *
     * @param <DTO> 实际的数据类型
     * @param items 可变参数列表，指定排序规则
     * @return 转换后的Page对象
     */
    public static <DTO extends PageQueryEntity, T> Page<T> toMpPage(DTO dto, OrderItem... items) {
        Page<T> page = getSelectPage(dto);
        // 如果排序字段值不为空
        if (!StringUtils.isEmpty(dto.getOrderByColumn())) {
            page.addOrder(initOrderItem(dto.getOrderByColumn(), "asc".equals(dto.getIsAsc())));
            // 如果有输入的排序参数
        } else if (items != null && items.length > 0) {
            page.addOrder(items);
        }
        return page;
    }

    /**
     * 将当前分页查询条件转换为MyBatis-Plus的Page对象，并使用默认的排序字段和顺序
     *
     * @param <DTO>         实际的数据类型
     * @param defaultSortBy 默认排序字段
     * @param defaultIsAsc  默认排序顺序
     * @return 转换后的Page对象
     */
    public static <DTO extends PageQueryEntity, T> Page<T> toMapPage(DTO dto, String defaultSortBy, Boolean defaultIsAsc) {
        return toMpPage(dto, initOrderItem(defaultSortBy, defaultIsAsc));
    }

    /**
     * 将当前分页查询条件转换为MyBatis-Plus的Page对象，并使用"sort"作为默认排序字段，降序排列
     *
     * @param <DTO> 实际的数据类型
     * @return 转换后的Page对象
     */
    public static <DTO extends PageQueryEntity, T> Page<T> toMpPageDefaultSortBySort(DTO dto) {
        return toMpPage(dto, initOrderItem("sort", false));
    }

    /**
     * 将当前分页查询条件转换为MyBatis-Plus的Page对象，并使用"create_time"作为默认排序字段，降序排列
     *
     * @param <DTO> 实际的数据类型
     * @return 转换后的Page对象
     */
    public static <DTO extends PageQueryEntity, T> Page<T> toMpPageDefaultSortByCreateTime(DTO dto) {
        return toMpPage(dto, initOrderItem("create_time", false));
    }

    /**
     * 将当前分页查询条件转换为MyBatis-Plus的Page对象，并使用"update_time"作为默认排序字段，降序排列
     *
     * @param <DTO> 实际的数据类型
     * @return 转换后的Page对象
     */
    public static <DTO extends PageQueryEntity, T> Page<T> toMpPageDefaultSortByUpdateTime(DTO dto) {
        return toMpPage(dto, initOrderItem("update_time", false));
    }

    /**
     * 根据分页参数和查询条件构造器，执行分页查询并返回结果
     *
     * @param pageNum    当前页码
     * @param pageSize   每页大小
     * @param baseMapper MyBatis-Plus 的 BaseMapper 对象，用于执行数据库操作
     * @param wrapper    查询条件构造器，用于构建查询条件
     * @return 包含分页结果的 Page 对象，其中包含分页信息和查询到的数据
     */
    public static <T> Page<T> getSelectPage(Integer pageNum, Integer pageSize, BaseMapper<T> baseMapper, Wrapper<T> wrapper) {
        clearPage();
        return baseMapper.selectPage(getSelectPage(pageNum, pageSize), wrapper);
    }

    /**
     * 根据分页查询条件和查询条件构造器，执行分页查询并返回结果
     *
     * @param <DTO>      分页查询实体类型，必须继承自 PageQueryEntity
     * @param dto        分页查询条件对象，包含分页参数（如 pageNum 和 pageSize）以及是否获取所有数据的标志
     * @param baseMapper MyBatis-Plus 的 BaseMapper 对象，用于执行数据库操作
     * @param wrapper    查询条件构造器，用于构建查询条件
     * @return 包含分页结果的 Page 对象，其中包含分页信息和查询到的数据
     */
    public static <DTO extends PageQueryEntity, T> Page<T> getSelectPage(DTO dto, BaseMapper<T> baseMapper, Wrapper<T> wrapper) {
        clearPage();
        return baseMapper.selectPage(getSelectPage(dto), wrapper);
    }

    /**
     * 处理Page对象的基本信息（总记录数、总页数），并创建一个对应的PageResult对象
     *
     * @param <VO> 目标视图类型
     * @param p    MyBatis-Plus的Page对象
     * @return 转换后的PageDTO对象
     */
    private static <T, VO> PageResult<VO> basePageResult(Page<T> p) {
        PageResult<VO> pageResult = new PageResult<>();
        pageResult.setPageNum((int) p.getCurrent());
        pageResult.setPageSize((int) p.getSize());
        pageResult.setTotal(p.getTotal());
        pageResult.setAllData(p.getSize() == 0 || p.getTotal() <= p.getSize());
        return pageResult;
    }

    /**
     * 将MyBatis-Plus的Page对象转换为PageVo对象，并使用BeanUtils进行属性拷贝
     *
     * @param <T> 基本数据库类型
     * @param p   MyBatis-Plus的Page对象
     * @return 转换后的PageDTO对象
     */
    public static <T> PageResult<T> of(Page<T> p) {
        PageResult<T> pageResult = basePageResult(p);

        List<T> records = p.getRecords();
        // 如果记录结果为空
        if (records == null || records.isEmpty()) {
            pageResult.setRows(Collections.emptyList());
            return pageResult;
        }

        return pageResult;
    }

    /**
     * 将MyBatis-Plus的Page对象转换为PageVo对象，并使用BeanUtils进行属性拷贝
     *
     * @param <VO>    目标视图类型
     * @param p       MyBatis-Plus的Page对象
     * @param voClass 目标视图类型的Class对象
     * @return 转换后的PageDTO对象
     */
    public static <T, VO> PageResult<VO> of(Page<T> p, Class<VO> voClass) {
        PageResult<VO> dto = basePageResult(p);

        List<T> records = p.getRecords();
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
     * 将MyBatis-Plus的Page对象转换为PageVo对象，并使用自定义转换器进行转换
     *
     * @param <VO>      目标视图类型
     * @param p         MyBatis-Plus的Page对象
     * @param convertor 自定义转换器，用于将原始实体转换为目标视图类型
     * @return 转换后的PageDTO对象
     */
    public static <T, VO> PageResult<VO> of(Page<T> p, Function<T, VO> convertor) {
        PageResult<VO> dto = basePageResult(p);

        List<T> records = p.getRecords();
        // 如果记录结果为空
        if (records == null || records.isEmpty()) {
            dto.setRows(Collections.emptyList());
            return dto;
        }

        // 使用转换器拷贝VO
        dto.setRows(records.stream().map(convertor).collect(Collectors.toList()));
        return dto;
    }
}
