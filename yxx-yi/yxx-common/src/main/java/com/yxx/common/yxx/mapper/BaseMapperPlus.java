package com.yxx.common.yxx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxx.common.core.domain.BaseEntity;

/**
 * MyBatisPlus二次增强Mapper接口
 *
 * @param <T> 数据库实体类
 */
public interface BaseMapperPlus<T extends BaseEntity> extends BaseMapper<T> {
}
