package com.yxx.common.core.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.yxx.common.core.domain.BaseColumnEntity;
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

    @Select(TopGroupQuery.TOP_GROUP_FUNC)
    List<T> topGroupFunc(@Param(Constants.WRAPPER) Wrapper<T> wrapper,
                         @Param(TopGroupQuery.SELECT_COL) String selectCol,
                         @Param(TopGroupQuery.TABLE_NAME) String tableName,
                         @Param(TopGroupQuery.GROUP_COL) String groupCol,
                         @Param(TopGroupQuery.ORDER_COL) String orderCol);
}
