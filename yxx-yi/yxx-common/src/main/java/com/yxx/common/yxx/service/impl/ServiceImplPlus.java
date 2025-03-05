package com.yxx.common.yxx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.yxx.common.core.domain.BaseEntity;
import com.yxx.common.yxx.domain.PageResult;
import com.yxx.common.yxx.mapper.BaseMapperPlus;
import com.yxx.common.yxx.service.IServicePlus;
import com.yxx.common.yxx.utils.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * MyBatisPlus二次增强实现类
 *
 * @param <M> Mapper接口
 * @param <T> 数据库实体类
 */
public class ServiceImplPlus<M extends BaseMapperPlus<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements IServicePlus<T> {

    /**
     * 获取到基本的Vo对象分页结果，不包含行结果
     */
    private <VO> PageResult<VO> getBasePageResult(List<T> list) {
        PageResult<VO> result = new PageResult<>();
        // 获取分页插件的分页结果信息
        PageInfo<T> pageInfo = new PageInfo<>(list);
        BeanUtil.copyProperties(pageInfo, result);
        return result;
    }

    /**
     * 获取转化后的Vo结果
     */
    @Override
    public <VO> VO getVo(T t, Class<VO> voClass) {
        if (ObjectUtils.isEmpty(t)) {
            return null;
        }
        return BeanUtil.copyProperties(t, voClass);
    }

    /**
     * 获取转化后的Vo结果
     */
    @Override
    public <VO> VO getVo(T t, Function<T, VO> convertor) {
        if (ObjectUtils.isEmpty(t)) {
            return null;
        }
        return convertor.apply(t);
    }

    /**
     * 获取转化后的VoList结果
     */
    @Override
    public <VO> List<VO> getVoList(List<T> list, Class<VO> voClass) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return BeanUtil.copyToList(list, voClass);
    }

    /**
     * 获取转化后的VoList结果
     */
    @Override
    public <VO> List<VO> getVoList(List<T> list, Function<T, VO> convertor) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(convertor).collect(Collectors.toList());
    }

    /**
     * 获取到Vo对象分页结果
     */
    @Override
    public <VO> PageResult<VO> getPageResult(List<T> list, Class<VO> voClass) {
        PageResult<VO> result = getBasePageResult(list);
        // 复制到VO中
        result.setRows(getVoList(list, voClass));
        return result;
    }

    /**
     * 获取到Vo对象分页结果
     */
    @Override
    public <VO> PageResult<VO> getPageResult(List<T> list, Function<T, VO> convertor) {
        PageResult<VO> result = getBasePageResult(list);
        // 使用转换器自定义Vo的处理
        result.setRows(getVoList(list, convertor));
        return result;
    }
}
