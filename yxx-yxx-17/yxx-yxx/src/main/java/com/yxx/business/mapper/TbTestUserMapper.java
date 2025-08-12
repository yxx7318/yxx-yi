package com.yxx.business.mapper;

import java.util.List;

import com.yxx.business.entity.TbTestUserDo;
import com.yxx.business.entity.TbTestUserQueryDto;
import com.yxx.common.core.mapper.BaseMapperPlus;

/**
 * 测试单表生成Mapper接口
 *
 * @author yxx
 * @date 2025-08-11
 */
public interface TbTestUserMapper extends BaseMapperPlus<TbTestUserDo> {

    /**
     * 查询测试单表生成列表
     *
     * @param tbTestUserQueryDto 测试单表生成查询实体
     * @return 测试单表生成集合
     */
    public List<TbTestUserDo> selectTbTestUserList(TbTestUserQueryDto tbTestUserQueryDto);

    /**
     * 查询测试单表生成
     *
     * @param userId 测试单表生成主键
     * @return 测试单表生成单个
     */
    public TbTestUserDo selectTbTestUserByUserId(Long userId);

    /**
     * 新增测试单表生成
     *
     * @param tbTestUserDo 测试单表生成数据库实体
     * @return 结果
     */
    public int insertTbTestUser(TbTestUserDo tbTestUserDo);

    /**
     * 修改测试单表生成
     *
     * @param tbTestUserDo 测试单表生成数据库实体
     * @return 结果
     */
    public int updateTbTestUser(TbTestUserDo tbTestUserDo);

    /**
     * 删除测试单表生成
     *
     * @param userId 测试单表生成主键
     * @return 结果
     */
    public int deleteTbTestUserByUserId(Long userId);

    /**
     * 批量删除测试单表生成
     *
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbTestUserByUserIds(List<Long> userIds);
}
