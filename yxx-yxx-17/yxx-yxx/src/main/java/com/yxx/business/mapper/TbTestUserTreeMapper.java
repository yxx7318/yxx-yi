package com.yxx.business.mapper;

import java.util.List;

import com.yxx.business.entity.TbTestUserTree;
import com.yxx.common.core.mapper.BaseMapperPlus;
import org.springframework.stereotype.Repository;

/**
 * 测试树表生成Mapper接口
 *
 * @author yxx
 * @date 2025-08-14
 */
@Repository
public interface TbTestUserTreeMapper extends BaseMapperPlus<TbTestUserTree> {

    /**
     * 查询测试树表生成列表
     *
     * @param tbTestUserTree 测试树表生成查询实体
     * @return 测试树表生成集合
     */
    public List<TbTestUserTree> selectTbTestUserTreeList(TbTestUserTree tbTestUserTree);

    /**
     * 查询测试树表生成
     *
     * @param userId 测试树表生成主键
     * @return 测试树表生成单个
     */
    public TbTestUserTree selectTbTestUserTreeByUserId(Long userId);

    /**
     * 新增测试树表生成
     *
     * @param tbTestUserTree 测试树表生成数据库实体
     * @return 结果
     */
    public int insertTbTestUserTree(TbTestUserTree tbTestUserTree);

    /**
     * 修改测试树表生成
     *
     * @param tbTestUserTree 测试树表生成数据库实体
     * @return 结果
     */
    public int updateTbTestUserTree(TbTestUserTree tbTestUserTree);

    /**
     * 删除测试树表生成
     *
     * @param userId 测试树表生成主键
     * @return 结果
     */
    public int deleteTbTestUserTreeByUserId(Long userId);

    /**
     * 批量删除测试树表生成
     *
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbTestUserTreeByUserIds(List<Long> userIds);
}
