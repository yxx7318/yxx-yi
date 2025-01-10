package com.yxx.common.domain;

import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 分页查询条件对象，用于封装分页请求中的参数。
 */
@Data
public class PageQuery {

    /**
     * 当前页码，默认为1
     */
    @TableField(exist = false)
    private Integer pageNo = 1;

    /**
     * 每页显示的记录数，默认为10
     */
    @TableField(exist = false)
    private Integer pageSize = 10;

    /**
     * 排序字段名称
     */
    @TableField(exist = false)
    private String sortBy;

    /**
     * 是否按升序排序，默认为false（降序）
     */
    @TableField(exist = false)
    private Boolean isAsc;

    /**
     * 将当前分页查询条件转换为MyBatis-Plus的Page对象，并根据提供的排序项进行排序
     *
     * @param <T>   实际的数据类型
     * @param items 可变参数列表，指定排序规则
     * @return 转换后的Page对象
     */
    public <T> Page<T> toMpPage(OrderItem... items) {
        Page<T> page = new Page<>(pageNo, pageSize);

        // 如果排序字段值不为空
        if (!StringUtils.isEmpty(sortBy)) {
            page.addOrder(new OrderItem(sortBy, isAsc));
        } else if (items != null && items.length > 0) {
            page.addOrder(items);
        }
        return page;
    }

    /**
     * 将当前分页查询条件转换为MyBatis-Plus的Page对象，并使用默认的排序字段和顺序。
     *
     * @param <T>           实际的数据类型
     * @param defaultSortBy 默认排序字段
     * @param defaultIsAsc  默认排序顺序
     * @return 转换后的Page对象
     */
    public <T> Page<T> toMapPage(String defaultSortBy, Boolean defaultIsAsc) {
        return toMpPage(new OrderItem(defaultSortBy, defaultIsAsc));
    }

    /**
     * 将当前分页查询条件转换为MyBatis-Plus的Page对象，并使用"create_time"作为默认排序字段，降序排列
     *
     * @param <T> 实际的数据类型
     * @return 转换后的Page对象
     */
    public <T> Page<T> toMpPageDefaultSortByCreateTime() {
        return toMpPage(new OrderItem("create_time", false));
    }

    /**
     * 将当前分页查询条件转换为MyBatis-Plus的Page对象，并使用"update_time"作为默认排序字段，降序排列
     *
     * @param <T> 实际的数据类型
     * @return 转换后的Page对象
     */
    public <T> Page<T> toMpPageDefaultSortByUpdateTime() {
        return toMpPage(new OrderItem("update_time", false));
    }
}