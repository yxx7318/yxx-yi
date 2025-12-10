package com.yxx.common.core.query;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.yxx.common.core.domain.BaseColumnEntity;
import com.yxx.common.core.mapper.BaseMapperPlus;
import com.yxx.common.core.utils.WrapperUtils;
import com.yxx.common.utils.spring.SpringUtils;

import java.util.List;


/**
 * Mysql的分组查询最新记录工具类
 */
public class TopGroupQueryUtils<T> extends LambdaQueryWrapper<T> {

    /**
     * Mysql8.0支持窗口函数
     *
     * @param wrapper    LambdaQueryWrapper对象，这里如果有where条件，应该已经构建
     * @param mapperPlus Mapper层类字节码
     * @param groupCol   分组的字段
     * @param orderCol   最新的字段
     * @return 分组后最新的列
     */
    public static <K extends BaseColumnEntity, M extends BaseMapperPlus<K>> List<K> topGroupFunc(LambdaQueryWrapper<K> wrapper, Class<M> mapperPlus, SFunction<K, ?> groupCol, SFunction<K, ?> orderCol) {
        Class<?> kClass = WrapperUtils.getEntityClassByMapper(mapperPlus);
        wrapper.setEntityClass((Class<K>) kClass);
        // 执行SQL并返回结果
        return SpringUtils.getBean(mapperPlus)
                .topGroupFunc(wrapper,
                        WrapperUtils.getTableField(wrapper),
                        WrapperUtils.getTableName(wrapper),
                        WrapperUtils.getDBColumn(groupCol),
                        WrapperUtils.getDBColumn(orderCol));
    }

    /**
     * Mysql8.0支持窗口函数
     *
     * @param mapperPlus Mapper层类字节码
     * @param groupCol   分组的字段
     * @param orderCol   最新的字段
     * @return 分组后最新的列
     */
    public static <K extends BaseColumnEntity, M extends BaseMapperPlus<K>> List<K> topGroupFunc(Class<M> mapperPlus, SFunction<K, ?> groupCol, SFunction<K, ?> orderCol) {
        Class<?> kClass = WrapperUtils.getEntityClassByMapper(mapperPlus);
        LambdaQueryWrapper<K> wrapper = new LambdaQueryWrapper<>((Class<K>) kClass);
        // 执行SQL并返回结果
        return SpringUtils.getBean(mapperPlus)
                .topGroupFunc(wrapper,
                        WrapperUtils.getTableField(wrapper),
                        WrapperUtils.getTableName(wrapper),
                        WrapperUtils.getDBColumn(groupCol),
                        WrapperUtils.getDBColumn(orderCol));
    }
}
