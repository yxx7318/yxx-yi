package com.yxx.common.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxx.common.core.domain.BaseColumnEntity;
import com.yxx.common.core.domain.PageQueryEntity;
import com.yxx.common.core.service.IServicePlus;
import com.yxx.common.core.domain.PageResult;
import com.yxx.common.core.mapper.BaseMapperPlus;
import com.yxx.common.core.utils.MpPageUtils;
import com.yxx.common.core.utils.ObjectUtils;
import com.yxx.common.core.utils.SingletonFactory;
import com.yxx.common.utils.PageUtils;

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
public class ServiceImplPlus<M extends BaseMapperPlus<T>, T extends BaseColumnEntity> extends ServiceImpl<M, T> implements IServicePlus<T> {

    /**
     * 获取转化后的Po结果
     */
    @Override
    public <PO> PO convertBean(T t, Class<PO> voClass) {
        if (ObjectUtils.isEmpty(t)) {
            return null;
        }
        return BeanUtil.copyProperties(t, voClass);
    }

    /**
     * 获取转化后的Po结果
     */
    @Override
    public <PO> PO convertBean(T t, Function<T, PO> convertor) {
        if (ObjectUtils.isEmpty(t)) {
            return null;
        }
        return convertor.apply(t);
    }

    @Override
    public <PO> T convertT(PO po) {
        return BeanUtil.copyProperties(po, super.getEntityClass());
    }

    /**
     * 获取MP通用分页结果
     */
    public Page<T> getMpDoPage(Integer pageNum, Integer pageSize) {
        return getMpDoPage(pageNum, pageSize, null);
    }

    /**
     * 获取MP通用分页结果
     */
    public Page<T> getMpDoPage(Integer pageNum, Integer pageSize, Wrapper<T> wrapper) {
        return MpPageUtils.getSelectPage(pageNum, pageSize, this.baseMapper, wrapper);
    }

    /**
     * 获取MP通用分页结果
     */
    @Override
    public <DTO extends PageQueryEntity> Page<T> getMpDoPage(DTO dto) {
        return this.getMpDoPage(dto, null);
    }

    /**
     * 获取MP分页结果
     */
    @Override
    public <DTO extends PageQueryEntity> Page<T> getMpDoPage(DTO dto, Wrapper<T> wrapper) {
        return MpPageUtils.getSelectPage(dto, this.baseMapper, wrapper);
    }



    /**
     * 获取转化后的List结果
     */
    @Override
    public <PO, VO> List<VO> convertList(List<PO> list, Class<VO> voClass) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return BeanUtil.copyToList(list, voClass);
    }

    /**
     * 获取转化后的VoList结果
     */
    @Override
    public <PO, VO> List<VO> convertList(List<PO> list, Function<PO, VO> convertor) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(convertor).collect(Collectors.toList());
    }



    /**
     * 获取到MyBatis分页结果
     *
     * @param list    分页插件查询数据
     * @param <PO>    PO对象
     * @return PageResult<PO>分页结果
     */
    public <PO> PageResult<PO> getMyBatisPageResult(List<PO> list) {
        PageResult<PO> result = PageUtils.getMyBatisBasePageResult(list);
        return result.setRows(list);
    }

    /**
     * 获取到MyBatis分页结果并转化为Vo对象分页结果
     */
    @Override
    public <PO, VO> PageResult<VO> getMyBatisPageResult(List<PO> list, Class<VO> voClass) {
        PageResult<VO> result = PageUtils.getMyBatisBasePageResult(list);
        // 复制到VO中
        return result.setRows(convertList(list, voClass));
    }

    /**
     * 获取到MyBatis分页结果并转化为Vo对象分页结果
     */
    @Override
    public <PO, VO> PageResult<VO> getMyBatisPageResult(List<PO> list, Function<PO, VO> convertor) {
        PageResult<VO> result = PageUtils.getMyBatisBasePageResult(list);
        // 使用转换器自定义Vo的处理
        return result.setRows(convertList(list, convertor));
    }

    /**
     * 根据分页参数获取MP分页结果
     */
    @Override
    public PageResult<T> getMpDoPageResult(Integer pageNum, Integer pageSize) {
        return MpPageUtils.of(this.getMpDoPage(pageNum, pageSize));
    }

    /**
     * 根据分页参数获取MP分页结果和目标VO类获取分页结果
     */
    @Override
    public <VO> PageResult<VO> getMpVoPageResult(Integer pageNum, Integer pageSize, Class<VO> voClass) {
        return getMpVoPageResult(pageNum, pageSize, null, voClass);
    }

    /**
     * 根据分页参数获取MP分页结果和目标VO类获取分页结果
     */
    @Override
    public <VO> PageResult<VO> getMpVoPageResult(Integer pageNum, Integer pageSize, Wrapper<T> wrapper, Class<VO> voClass) {
        Page<T> page = this.getMpDoPage(pageNum, pageSize, wrapper);
        return MpPageUtils.of(page, voClass);
    }

    /**
     * 根据分页参数获取MP分页结果和自定义转换方法获取分页结果
     */
    @Override
    public <VO> PageResult<VO> getMpVoPageResult(Integer pageNum, Integer pageSize, Function<T, VO> convertor) {
        return getMpVoPageResult(pageNum, pageSize, null, convertor);
    }

    /**
     * 根据分页参数获取MP分页结果和自定义转换方法获取分页结果
     */
    @Override
    public <VO> PageResult<VO> getMpVoPageResult(Integer pageNum, Integer pageSize, Wrapper<T> wrapper, Function<T, VO> convertor) {
        Page<T> page = this.getMpDoPage(pageNum, pageSize, wrapper);
        return MpPageUtils.of(page, convertor);
    }

    /**
     * 根据对象获取分页结果
     */
    @Override
    public <DTO extends PageQueryEntity> PageResult<T> getMpDoPageResult(DTO dto) {
        return MpPageUtils.of(this.getMpDoPage(dto));
    }

    /**
     * 根据对象获取分页结果并转化目标VO类获取分页结果
     */
    @Override
    public <DTO extends PageQueryEntity, VO> PageResult<VO> getMpVoPageResult(DTO dto, Class<VO> voClass) {
        return getMpVoPageResult(dto, null, voClass);
    }

    /**
     * 根据对象获取分页结果并转化目标VO类获取分页结果
     */
    @Override
    public <DTO extends PageQueryEntity, VO> PageResult<VO> getMpVoPageResult(DTO dto, Function<T, VO> convertor) {
        return getMpVoPageResult(dto, null, convertor);
    }

    /**
     * 根据对象和条件获取分页结果并转化目标VO类获取分页结果
     */
    @Override
    public <DTO extends PageQueryEntity, VO> PageResult<VO> getMpVoPageResult(DTO dto, Wrapper<T> wrapper, Class<VO> voClass){
        Page<T> page = this.getMpDoPage(dto, wrapper);
        return MpPageUtils.of(page, voClass);
    }

    /**
     * 根据对象和条件获取分页结果并转化目标VO类获取分页结果
     */
    @Override
    public <DTO extends PageQueryEntity, VO> PageResult<VO> getMpVoPageResult(DTO dto, Wrapper<T> wrapper, Function<T, VO> convertor) {
        Page<T> page = this.getMpDoPage(dto, wrapper);
        return MpPageUtils.of(page, convertor);
    }
}
