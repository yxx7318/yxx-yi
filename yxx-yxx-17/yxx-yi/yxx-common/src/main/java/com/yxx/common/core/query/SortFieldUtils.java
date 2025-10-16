package com.yxx.common.core.query;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.yxx.common.core.utils.WrapperUtils;
import com.yxx.common.core.utils.StreamUtils;
import com.yxx.common.utils.StringUtils;

import java.util.List;

import static com.yxx.common.constant.MySqlConstats.SORT_FIELD_SQL;

/**
 * Mysql的字段排序工具类
 */
public class SortFieldUtils<T> extends LambdaQueryWrapper<T> {

    /**
     * 对查询结果按照字段排序
     *
     * @param wrapper  LambdaQueryWrapper对象
     * @param orderCol 排序字段
     * @param sortIds  排序顺序
     * @return 增强后的LambdaQueryWrapper
     */
    public static <C, K> LambdaQueryWrapper<C> orderAndSortField(LambdaQueryWrapper<C> wrapper, SFunction<K, ?> orderCol, List<Object> sortIds) {
        return wrapper.last(String.format(SORT_FIELD_SQL,
                WrapperUtils.getDBColumn(orderCol),
                StreamUtils.join(sortIds, Object::toString, StringUtils.COMMA)));
    }
}
