package com.yxx.common.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.yxx.common.core.domain.BaseEntity;
import com.yxx.common.core.service.IServicePlus;
import com.yxx.common.core.domain.PageResult;
import com.yxx.common.core.mapper.BaseMapperPlus;
import com.yxx.common.core.utils.MpPageUtils;
import com.yxx.common.core.utils.ObjectUtils;
import com.yxx.common.core.utils.SingletonFactory;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * MyBatisPlus二次增强实现类
 *
 * @param <M> Mapper接口
 * @param <T> 数据库实体类
 * @author yxx
 */
public class ServiceImplPlus<M extends BaseMapperPlus<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements IServicePlus<T> {

    /**
     * 获取自注入spring管理的bean
     */
    @Override
    public IServicePlus<T> getSelfBean() {
        return SingletonFactory.getSingleton(this.getClass());
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
     * 获取MP通用分页结果
     */
    @Override
    public Page<T> getMpPage(Integer pageNum, Integer pageSize) {
        return getMpPage(pageNum, pageSize, null);
    }

    /**
     * 获取MP通用分页结果
     */
    @Override
    public Page<T> getMpPage(Integer pageNum, Integer pageSize, Wrapper<T> wrapper) {
        return MpPageUtils.getSelectPage(pageNum, pageSize, this.baseMapper, wrapper);
    }

    /**
     * 获取MP通用分页结果
     */
    @Override
    public Page<T> getMpPage(T t) {
        return this.getMpPage(t, null);
    }

    /**
     * 获取MP分页结果
     */
    @Override
    public Page<T> getMpPage(T t, Wrapper<T> wrapper) {
        return MpPageUtils.getSelectPage(t, this.baseMapper, wrapper);
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
     * 获取到MyBatis基本分页结果，不包含行结果
     */
    @Override
    public <VO> PageResult<VO> getMyBatisBasePageResult(List<T> list) {
        PageResult<VO> result = new PageResult<>();
        // 获取分页插件的分页结果信息
        PageInfo<T> pageInfo = new PageInfo<>(list);
        BeanUtil.copyProperties(pageInfo, result);
        return result;
    }

    /**
     * 获取MyBatis分页结果
     */
    @Override
    public PageResult<T> getMyBatisPageResult(List<T> list) {
        PageResult<T> result = getMyBatisBasePageResult(list);
        result.setRows(list);
        return result;
    }

    /**
     * 获取到MyBatis分页结果并转化为Vo对象分页结果
     */
    @Override
    public <VO> PageResult<VO> getMyBatisPageResult(List<T> list, Class<VO> voClass) {
        PageResult<VO> result = getMyBatisBasePageResult(list);
        // 复制到VO中
        result.setRows(getVoList(list, voClass));
        return result;
    }

    /**
     * 获取到MyBatis分页结果并转化为Vo对象分页结果
     */
    @Override
    public <VO> PageResult<VO> getMyBatisPageResult(List<T> list, Function<T, VO> convertor) {
        PageResult<VO> result = getMyBatisBasePageResult(list);
        // 使用转换器自定义Vo的处理
        result.setRows(getVoList(list, convertor));
        return result;
    }

    /**
     * 根据分页参数获取MP分页结果
     */
    @Override
    public <VO> PageResult<VO> getMpPageResult(Integer pageNum, Integer pageSize) {
        return MpPageUtils.dealWith(this.getMpPage(pageNum, pageSize));
    }

    /**
     * 根据分页参数获取MP分页结果和目标VO类获取分页结果
     */
    @Override
    public <VO> PageResult<VO> getMpPageResult(Integer pageNum, Integer pageSize, Class<VO> voClass) {
        return getMpPageResult(pageNum, pageSize, null, voClass);
    }

    /**
     * 根据分页参数获取MP分页结果和目标VO类获取分页结果
     */
    @Override
    public <VO> PageResult<VO> getMpPageResult(Integer pageNum, Integer pageSize, Wrapper<T> wrapper, Class<VO> voClass) {
        Page<T> page = this.getMpPage(pageNum, pageSize, wrapper);
        return MpPageUtils.of(page, voClass);
    }

    /**
     * 根据分页参数获取MP分页结果和自定义转换方法获取分页结果
     */
    @Override
    public <VO> PageResult<VO> getMpPageResult(Integer pageNum, Integer pageSize, Function<T, VO> convertor) {
        return getMpPageResult(pageNum, pageSize, null, convertor);
    }

    /**
     * 根据分页参数获取MP分页结果和自定义转换方法获取分页结果
     */
    @Override
    public <VO> PageResult<VO> getMpPageResult(Integer pageNum, Integer pageSize, Wrapper<T> wrapper, Function<T, VO> convertor) {
        Page<T> page = this.getMpPage(pageNum, pageSize, wrapper);
        return MpPageUtils.of(page, convertor);
    }

    /**
     * 根据对象获取分页结果
     */
    @Override
    public <VO> PageResult<VO> getMpPageResult(T t) {
        return MpPageUtils.dealWith(this.getMpPage(t));
    }

    /**
     * 根据对象获取分页结果并转化目标VO类获取分页结果
     */
    @Override
    public <VO> PageResult<VO> getMpPageResult(T t, Class<VO> voClass) {
        return getMpPageResult(t, null, voClass);
    }

    /**
     * 根据对象获取分页结果并转化目标VO类获取分页结果
     */
    @Override
    public <VO> PageResult<VO> getMpPageResult(T t, Function<T, VO> convertor) {
        return getMpPageResult(t, null, convertor);
    }

    /**
     * 根据对象和条件获取分页结果并转化目标VO类获取分页结果
     */
    @Override
    public <VO> PageResult<VO> getMpPageResult(T t, Wrapper<T> wrapper, Class<VO> voClass) {
        Page<T> page = this.getMpPage(t, wrapper);
        return MpPageUtils.of(page, voClass);
    }

    /**
     * 根据对象和条件获取分页结果并转化目标VO类获取分页结果
     */
    @Override
    public <VO> PageResult<VO> getMpPageResult(T t, Wrapper<T> wrapper, Function<T, VO> convertor) {
        Page<T> page = this.getMpPage(t, wrapper);
        return MpPageUtils.of(page, convertor);
    }

}
