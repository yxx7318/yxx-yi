package com.yxx.common.yxx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxx.common.core.domain.BaseEntity;
import com.yxx.common.yxx.mapper.BaseMapperPlus;
import com.yxx.common.yxx.service.IServicePlus;

/**
 * MyBatisPlus二次增强实现类
 *
 * @param <M> Mapper接口
 * @param <T> 数据库实体类
 */
public class ServiceImplPlus<M extends BaseMapperPlus<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements IServicePlus<T> {
}
