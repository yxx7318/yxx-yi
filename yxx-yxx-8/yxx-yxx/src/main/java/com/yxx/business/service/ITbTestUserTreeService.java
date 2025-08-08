package com.yxx.business.service;

import java.util.List;

import com.yxx.common.core.domain.PageResult;
import com.yxx.business.entity.TbTestUserTree;
import com.yxx.common.core.service.IServicePlus;

/**
 * 测试树表生成Service接口
 *
 * @author yxx
 * @date 2025-08-08
 */
public interface ITbTestUserTreeService extends IServicePlus<TbTestUserTree> {

    /**
     * 查询测试树表生成分页结果
     *
     * @param tbTestUserTree 测试树表生成查询实体
     * @return 测试树表生成分页
     */
    public PageResult<TbTestUserTree> selectTbTestUserTreePage(TbTestUserTree tbTestUserTree);

    /**
     * 查询测试树表生成Do列表
     *
     * @param tbTestUserTree 测试树表生成查询实体
     * @return 测试树表生成集合
     */
    public List<TbTestUserTree> selectTbTestUserTreeList(TbTestUserTree tbTestUserTree);

    /**
     * 查询单个测试树表生成
     *
     * @param userId 测试树表生成主键
     * @return 测试树表生成单个
     */
    public TbTestUserTree selectTbTestUserTreeByUserId(Long userId);

    /**
     * 新增测试树表生成
     *
     * @param tbTestUserTree 测试树表生成编辑实体
     * @return 结果
     */
    public int insertTbTestUserTree(TbTestUserTree tbTestUserTree);

    /**
     * 修改测试树表生成
     *
     * @param userId 主键
     * @param tbTestUserTree 测试树表生成编辑实体
     * @return 结果
     */
    public int updateTbTestUserTree(Long userId, TbTestUserTree tbTestUserTree);

    /**
     * 批量删除测试树表生成
     *
     * @param userIds 测试树表生成主键集合
     * @return 结果
     */
    public int deleteTbTestUserTreeByUserIds(List<Long> userIds);

    /**
     * 删除单个测试树表生成信息
     *
     * @param userId 测试树表生成主键
     * @return 结果
     */
    public int deleteTbTestUserTreeByUserId(Long userId);
}
