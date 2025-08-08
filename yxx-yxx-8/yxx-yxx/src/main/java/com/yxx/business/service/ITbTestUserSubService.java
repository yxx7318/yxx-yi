package com.yxx.business.service;

import java.util.List;

import com.yxx.common.core.domain.PageResult;
import com.yxx.business.entity.TbTestUserSub;
import com.yxx.common.core.service.IServicePlus;

/**
 * 测试主表生成Service接口
 *
 * @author yxx
 * @date 2025-08-08
 */
public interface ITbTestUserSubService extends IServicePlus<TbTestUserSub> {

    /**
     * 查询测试主表生成分页结果
     *
     * @param tbTestUserSub 测试主表生成查询实体
     * @return 测试主表生成分页
     */
    public PageResult<TbTestUserSub> selectTbTestUserSubPage(TbTestUserSub tbTestUserSub);

    /**
     * 查询测试主表生成Do列表
     *
     * @param tbTestUserSub 测试主表生成查询实体
     * @return 测试主表生成集合
     */
    public List<TbTestUserSub> selectTbTestUserSubList(TbTestUserSub tbTestUserSub);

    /**
     * 查询单个测试主表生成
     *
     * @param subId 测试主表生成主键
     * @return 测试主表生成单个
     */
    public TbTestUserSub selectTbTestUserSubBySubId(Long subId);

    /**
     * 新增测试主表生成
     *
     * @param tbTestUserSub 测试主表生成编辑实体
     * @return 结果
     */
    public int insertTbTestUserSub(TbTestUserSub tbTestUserSub);

    /**
     * 修改测试主表生成
     *
     * @param subId 主键
     * @param tbTestUserSub 测试主表生成编辑实体
     * @return 结果
     */
    public int updateTbTestUserSub(Long subId, TbTestUserSub tbTestUserSub);

    /**
     * 批量删除测试主表生成
     *
     * @param subIds 测试主表生成主键集合
     * @return 结果
     */
    public int deleteTbTestUserSubBySubIds(List<Long> subIds);

    /**
     * 删除单个测试主表生成信息
     *
     * @param subId 测试主表生成主键
     * @return 结果
     */
    public int deleteTbTestUserSubBySubId(Long subId);
}
