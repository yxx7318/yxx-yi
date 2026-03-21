package com.yxx.common.core.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.yxx.common.core.domain.BaseColumnEntity;
import com.yxx.common.utils.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

import static com.yxx.common.constant.MySqlConstats.TopGroupQuery;

/**
 * MyBatisPlus二次增强Mapper接口
 *
 * @param <T> 数据库实体类
 */
public interface BaseMapperPlus<T extends BaseColumnEntity> extends BaseMapper<T> {

    /**
     * 分组取每组TOP 1记录的SQL模板
     *
     * @param wrapper   原有的查询条件
     * @param selectCol 查询的字段
     * @param tableName 表名
     * @param groupCol  分组字段
     * @param orderCol  排序字段
     * @return 查询结果列表
     * @example 查询每个部门薪资最高的员工
     */
    @Select(TopGroupQuery.TOP_GROUP_FUNC)
    List<T> topGroupFunc(@Param(Constants.WRAPPER) Wrapper<T> wrapper,
                         @Param(TopGroupQuery.SELECT_COL) String selectCol,
                         @Param(TopGroupQuery.TABLE_NAME) String tableName,
                         @Param(TopGroupQuery.GROUP_COL) String groupCol,
                         @Param(TopGroupQuery.ORDER_COL) String orderCol);

    /**
     * 依据基本实体类属性构建基本的查询条件
     *
     * @param wrapper 原有查询条件
     * @param entity  基本实体类条件
     * @return 构建后的基本实体类查询条件
     */
    default LambdaQueryWrapper<T> buildBaseQuery(LambdaQueryWrapper<T> wrapper, T entity) {
        if (wrapper == null || entity == null) {
            return null;
        }
        wrapper.eq(StringUtils.isNotNull(entity.getCreateById()), T::getCreateById, entity.getCreateById());
        wrapper.like(StringUtils.isNotEmpty(entity.getCreateByName()), T::getCreateByName, entity.getCreateByName());
        wrapper.eq(StringUtils.isNotNull(entity.getCreateTime()), T::getCreateTime, entity.getCreateTime());
        wrapper.eq(StringUtils.isNotNull(entity.getUpdateById()), T::getUpdateById, entity.getUpdateById());
        wrapper.like(StringUtils.isNotEmpty(entity.getUpdateByName()), T::getUpdateByName, entity.getUpdateByName());
        wrapper.eq(StringUtils.isNotNull(entity.getUpdateTime()), T::getUpdateTime, entity.getUpdateTime());
        wrapper.like(StringUtils.isNotEmpty(entity.getRemark()), T::getRemark, entity.getRemark());
        return wrapper;
    }

    /**
     * 依据查询条件wrapper和基本实体类查询条件entity查询唯一一条结果
     *
     * @param wrapper 原有查询条件
     * @param entity  基本实体类条件
     * @return 查询唯一的一条结果
     */
    default T selectOneByT(LambdaQueryWrapper<T> wrapper, T entity) {
        return selectOne(buildBaseQuery(wrapper, entity));
    }

    /**
     * 依据基本实体类查询条件entity查询唯一一条结果
     *
     * @param entity 基本实体类条件
     * @return 查询唯一的一条结果
     */
    default T selectOneByT(T entity) {
        return selectOne(buildBaseQuery(new LambdaQueryWrapper<T>(), entity));
    }

    /**
     * 依据查询条件wrapper和基本实体类查询条件entity查询列表结果
     *
     * @param wrapper 原有查询条件
     * @param entity  基本实体类条件
     * @return 查询唯一的一条结果
     */
    default List<T> selectListByT(LambdaQueryWrapper<T> wrapper, T entity) {
        return selectList(buildBaseQuery(wrapper, entity));
    }

    /**
     * 依据基本实体类查询条件entity查询列表结果
     *
     * @param entity 基本实体类条件
     * @return 查询唯一的一条结果
     */
    default List<T> selectListByT(T entity) {
        return selectList(buildBaseQuery(new LambdaQueryWrapper<T>(), entity));
    }
}
