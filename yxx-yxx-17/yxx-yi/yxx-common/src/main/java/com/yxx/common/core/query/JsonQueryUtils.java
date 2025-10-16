package com.yxx.common.core.query;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.yxx.common.core.utils.WrapperUtils;
import com.yxx.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import static com.yxx.common.constant.MySqlConstats.QUERY_JSON_SQL;


/**
 * Mysql的JSON字段查询工具类
 */
@Slf4j
public class JsonQueryUtils<T> extends LambdaQueryWrapper<T> {

    /**
     * 构建JSON字段的查询条件（安全处理非JSON数据）
     *
     * @param wrapper LambdaQueryWrapper对象
     * @param column  数据库JSON列名
     * @param key     JSON字段的键
     * @param value   要匹配的值
     * @return 增强后的LambdaQueryWrapper
     */
    public LambdaQueryWrapper<T> queryJsonColumn(LambdaQueryWrapper<T> wrapper, String column, String query, String key, Object value) {
        if (StringUtils.isNull(value)) {
            return wrapper; // 空值直接跳过
        }

        // 安全过滤：仅当列是有效JSON时才应用条件
        String safeCondition = String.format(
                QUERY_JSON_SQL,
                column, column, query, key
        );
        return wrapper.apply(true, safeCondition, value);
    }

    /**
     * 通过方法引用构建JSON字段的查询条件
     *
     * @param wrapper LambdaQueryWrapper对象
     * @param column  数据库JSON列名，DO对象方法引用
     * @param key     JSON字段的键
     * @param value   要匹配的值
     * @return 增强后的LambdaQueryWrapper
     */
    public static <C> LambdaQueryWrapper<C> eq(LambdaQueryWrapper<C> wrapper, SFunction<C, ?> column, String key, Object value) {
        return new JsonQueryUtils<C>().queryJsonColumn(wrapper, WrapperUtils.getDBColumn(column), key, "= {0}", value);
    }

    /**
     * 通过方法引用构建JSON字段的查询条件
     *
     * @param wrapper LambdaQueryWrapper对象
     * @param column  数据库JSON列名，DO对象方法引用
     * @param key     JSON字段的键，Java对象方法引用
     * @param value   要匹配的值
     * @return 增强后的LambdaQueryWrapper
     */
    public static <C, K> LambdaQueryWrapper<C> eq(LambdaQueryWrapper<C> wrapper, SFunction<C, ?> column, SFunction<K, ?> key, Object value) {
        return new JsonQueryUtils<C>().queryJsonColumn(wrapper, WrapperUtils.getDBColumn(column), WrapperUtils.getJavaField(key), "= {0}", value);
    }

    /**
     * 通过方法引用构建JSON字段的查询条件
     *
     * @param wrapper LambdaQueryWrapper对象
     * @param column  数据库JSON列名，DO对象方法引用
     * @param key     JSON字段的键
     * @param value   要匹配的值
     * @return 增强后的LambdaQueryWrapper
     */
    public static <C> LambdaQueryWrapper<C> like(LambdaQueryWrapper<C> wrapper, SFunction<C, ?> column, String key, Object value) {
        return new JsonQueryUtils<C>().queryJsonColumn(wrapper, WrapperUtils.getDBColumn(column), key, "LIKE CONCAT('%', {0}, '%')", value);
    }

    /**
     * 通过方法引用构建JSON字段的查询条件
     *
     * @param wrapper LambdaQueryWrapper对象
     * @param column  数据库JSON列名，DO对象方法引用
     * @param key     JSON字段的键，Java对象方法引用
     * @param value   要匹配的值
     * @return 增强后的LambdaQueryWrapper
     */
    public static <C, K> LambdaQueryWrapper<C> like(LambdaQueryWrapper<C> wrapper, SFunction<C, ?> column, SFunction<K, ?> key, Object value) {
        return new JsonQueryUtils<C>().queryJsonColumn(wrapper, WrapperUtils.getDBColumn(column), WrapperUtils.getJavaField(key), "LIKE CONCAT('%', {0}, '%')", value);
    }
}
