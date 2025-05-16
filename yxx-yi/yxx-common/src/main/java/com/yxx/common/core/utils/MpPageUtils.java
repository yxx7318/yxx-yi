package com.yxx.common.core.utils;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxx.common.core.domain.BaseEntity;
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
     * @param <PO>     PageQueryEntity子类
     * @return 分页结果
     */
    public static <PO extends BaseEntity> Page<PO> getSelectPage(Integer pageNum, Integer pageSize) {
        // 创建分页对象
        Page<PO> page;
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
     * @param po   PageQueryEntity实体类
     * @param <PO> PageQueryEntity子类
     * @return 分页结果
     */
    public static <PO extends BaseEntity> Page<PO> getSelectPage(PO po) {
        // 创建分页对象
        Page<PO> page;
        if (ObjectUtils.isNotNull(po) && ObjectUtils.isNotNull(po.getAllData()) && po.getAllData()) {
            page = new Page<>(1, Integer.MAX_VALUE);
        } else {
            page = new Page<>(po.getPageNum(), po.getPageSize());
        }
        return page;
    }

    /**
     * 将当前分页查询条件转换为MyBatis-Plus的Page对象，并根据提供的排序项进行排序
     *
     * @param <PO>  实际的数据类型
     * @param items 可变参数列表，指定排序规则
     * @return 转换后的Page对象
     */
    public static <PO extends BaseEntity> Page<PO> toMpPage(PO po, OrderItem... items) {
        Page<PO> page = getSelectPage(po);
        // 如果排序字段值不为空
        if (!StringUtils.isEmpty(po.getOrderByColumn())) {
            page.addOrder(new OrderItem(po.getOrderByColumn(), "asc".equals(po.getIsAsc())));
            // 如果有输入的排序参数
        } else if (items != null && items.length > 0) {
            page.addOrder(items);
        }
        return page;
    }

    /**
     * 将当前分页查询条件转换为MyBatis-Plus的Page对象，并使用默认的排序字段和顺序
     *
     * @param <PO>          实际的数据类型
     * @param defaultSortBy 默认排序字段
     * @param defaultIsAsc  默认排序顺序
     * @return 转换后的Page对象
     */
    public static <PO extends BaseEntity> Page<PO> toMapPage(PO po, String defaultSortBy, Boolean defaultIsAsc) {
        return toMpPage(po, new OrderItem(defaultSortBy, defaultIsAsc));
    }

    /**
     * 将当前分页查询条件转换为MyBatis-Plus的Page对象，并使用"sort"作为默认排序字段，降序排列
     *
     * @param <PO> 实际的数据类型
     * @return 转换后的Page对象
     */
    public static <PO extends BaseEntity> Page<PO> toMpPageDefaultSortBySort(PO po) {
        return toMpPage(po, new OrderItem("sort", false));
    }

    /**
     * 将当前分页查询条件转换为MyBatis-Plus的Page对象，并使用"create_time"作为默认排序字段，降序排列
     *
     * @param <PO> 实际的数据类型
     * @return 转换后的Page对象
     */
    public static <PO extends BaseEntity> Page<PO> toMpPageDefaultSortByCreateTime(PO po) {
        return toMpPage(po, new OrderItem("create_time", false));
    }

    /**
     * 将当前分页查询条件转换为MyBatis-Plus的Page对象，并使用"update_time"作为默认排序字段，降序排列
     *
     * @param <PO> 实际的数据类型
     * @return 转换后的Page对象
     */
    public static <PO extends BaseEntity> Page<PO> toMpPageDefaultSortByUpdateTime(PO po) {
        return toMpPage(po, new OrderItem("update_time", false));
    }

    /**
     * 根据分页参数和查询条件构造器，执行分页查询并返回结果
     *
     * @param <PO>       分页查询实体类型，必须继承自 PageQueryEntity
     * @param pageNum    当前页码
     * @param pageSize   每页大小
     * @param baseMapper MyBatis-Plus 的 BaseMapper 对象，用于执行数据库操作
     * @param wrapper    查询条件构造器，用于构建查询条件
     * @return 包含分页结果的 Page 对象，其中包含分页信息和查询到的数据
     */
    public static <PO extends BaseEntity> Page<PO> getSelectPage(Integer pageNum, Integer pageSize, BaseMapper<PO> baseMapper, Wrapper<PO> wrapper) {
        clearPage();
        return baseMapper.selectPage(getSelectPage(pageNum, pageSize), wrapper);
    }

    /**
     * 根据分页查询条件和查询条件构造器，执行分页查询并返回结果
     *
     * @param <PO>       分页查询实体类型，必须继承自 PageQueryEntity
     * @param po         分页查询条件对象，包含分页参数（如 pageNum 和 pageSize）以及是否获取所有数据的标志
     * @param baseMapper MyBatis-Plus 的 BaseMapper 对象，用于执行数据库操作
     * @param wrapper    查询条件构造器，用于构建查询条件
     * @return 包含分页结果的 Page 对象，其中包含分页信息和查询到的数据
     */
    public static <PO extends BaseEntity> Page<PO> getSelectPage(PO po, BaseMapper<PO> baseMapper, Wrapper<PO> wrapper) {
        clearPage();
        return baseMapper.selectPage(getSelectPage(po), wrapper);
    }

    /**
     * 处理Page对象的基本信息（总记录数、总页数），并创建一个对应的PageDTO对象
     *
     * @param <VO> 目标视图类型
     * @param <PO> 原始实体类型
     * @param p    MyBatis-Plus的Page对象
     * @return 转换后的PageDTO对象
     */
    public static <PO extends BaseEntity, VO> PageResult<VO> dealWith(Page<PO> p) {
        PageResult<VO> dto = new PageResult<>();
        dto.setPageNum((int) p.getCurrent());
        dto.setPageSize((int) p.getSize());
        dto.setTotal(p.getTotal());
        dto.setAllData(p.getSize() == 0 || p.getTotal() <= p.getSize());
        return dto;
    }

    /**
     * 将MyBatis-Plus的Page对象转换为PageVo对象，并使用BeanUtils进行属性拷贝
     *
     * @param <PO>    原始实体类型
     * @param <VO>    目标视图类型
     * @param p       MyBatis-Plus的Page对象
     * @param voClass 目标视图类型的Class对象
     * @return 转换后的PageDTO对象
     */
    public static <PO extends BaseEntity, VO> PageResult<VO> of(Page<PO> p, Class<VO> voClass) {
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
     * 将MyBatis-Plus的Page对象转换为PageVo对象，并使用自定义转换器进行转换
     *
     * @param <PO>      原始实体类型
     * @param <VO>      目标视图类型
     * @param p         MyBatis-Plus的Page对象
     * @param convertor 自定义转换器，用于将原始实体转换为目标视图类型
     * @return 转换后的PageDTO对象
     */
    public static <PO extends BaseEntity, VO> PageResult<VO> of(Page<PO> p, Function<PO, VO> convertor) {
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
}
