package com.yxx.common.core.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.reflect.GenericTypeUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.yxx.common.core.domain.BaseColumnEntity;
import com.yxx.common.core.mapper.BaseMapperPlus;
import com.yxx.common.utils.StringUtils;
import org.apache.ibatis.reflection.property.PropertyNamer;

public class WrapperUtils<T> extends LambdaQueryWrapper<T> {

    /**
     * 通过方法引用获取到对应的MyBatisPlus映射到的数据库字段名
     */
    public static <C> String getDBColumn(SFunction<C, ?> column) {
        return new WrapperUtils<C>().columnToString(column, true);
    }

    /**
     * 通过方法引用获取到对应的Java属性名
     */
    public static <K> String getJavaField(SFunction<K, ?> key) {
        return PropertyNamer.methodToProperty(LambdaUtils.extract(key).getImplMethodName());
    }

    /**
     * 通过Mapper获取到实体类字节码
     */
    public static <K extends BaseColumnEntity, T extends BaseMapperPlus<K>> Class<?> getEntityClassByMapper(Class<T> mapperPlus) {
        return GenericTypeUtils.resolveTypeArguments(mapperPlus, BaseMapperPlus.class)[0];
    }

    /**
     * 获取实体类对应的表名
     *
     * @param entityClass 实体类Class对象
     * @return 数据库表名
     */
    public static String getTableName(Class<?> entityClass) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
        return tableInfo != null ? tableInfo.getTableName() : null;
    }

    /**
     * 获取实体类对应的表名
     *
     * @param lambdaQueryWrapper 实体类QueryWrapper对象
     * @return 数据库表名
     */
    public static <T> String getTableName(LambdaQueryWrapper<T> lambdaQueryWrapper) {
        Class<T> entityClass = lambdaQueryWrapper.getEntityClass();
        if (entityClass == null) {
            throw new MybatisPlusException("ElementClass cannot be empty");
        }
        return getTableName(entityClass);
    }

    /**
     * 获取表的所有字段
     *
     * @param entityClass 实体类Class对象
     * @return 数据库所有字段
     */
    public static <T> String getAllTableFields(Class<T> entityClass) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
        return StreamUtils.join(tableInfo.getFieldList(), TableFieldInfo::getColumn, StringUtils.COMMA);
    }

    /**
     * 获取表的所有字段
     *
     * @param lambdaQueryWrapper 实体类QueryWrapper对象
     * @return 数据库所有字段
     */
    public static <T> String getAllTableFields(LambdaQueryWrapper<T> lambdaQueryWrapper) {
        Class<T> entityClass = lambdaQueryWrapper.getEntityClass();
        if (entityClass == null) {
            throw new MybatisPlusException("ElementClass cannot be empty");
        }
        return getAllTableFields(entityClass);
    }

    /**
     * 获取查询指定的字段，没有指定则返回所有
     *
     * @param lambdaQueryWrapper 实体类QueryWrapper对象
     * @return 数据库所有字段
     */
    public static <T> String getTableField(LambdaQueryWrapper<T> lambdaQueryWrapper) {
        return lambdaQueryWrapper.getSqlSelect() == null ? WrapperUtils.getAllTableFields(lambdaQueryWrapper) : lambdaQueryWrapper.getSqlSelect();
    }
}
