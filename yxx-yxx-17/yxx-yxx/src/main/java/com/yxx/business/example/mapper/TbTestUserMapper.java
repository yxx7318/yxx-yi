package com.yxx.business.example.mapper;

import java.util.List;

import com.yxx.business.example.domain.TbTestUserDo;
import com.yxx.business.example.domain.TbTestUserQueryDto;
import com.yxx.common.core.mapper.BaseMapperPlus;

/**
 * 测试用户Mapper接口
 *
 * @author yxx
 * @date 2025-07-17
 */
public interface TbTestUserMapper extends BaseMapperPlus<TbTestUserDo> {

    /**
     * 查询测试用户列表
     *
     * @param tbTestUserQueryDto 测试用户查询实体
     * @return 测试用户集合
     */
    public List<TbTestUserDo> selectTbTestUserList(TbTestUserQueryDto tbTestUserQueryDto);

    /**
     * 查询测试用户
     *
     * @param userId 测试用户主键
     * @return 测试用户单个
     */
    public TbTestUserDo selectTbTestUserByUserId(Long userId);

    /**
     * 新增测试用户
     *
     * @param tbTestUserDo 测试用户数据库实体
     * @return 结果
     */
    public int insertTbTestUser(TbTestUserDo tbTestUserDo);

    /**
     * 修改测试用户
     *
     * @param tbTestUserDo 测试用户数据库实体
     * @return 结果
     */
    public int updateTbTestUser(TbTestUserDo tbTestUserDo);

    /**
     * 删除测试用户
     *
     * @param userId 测试用户主键
     * @return 结果
     */
    public int deleteTbTestUserByUserId(Long userId);

    /**
     * 批量删除测试用户
     *
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbTestUserByUserIds(List<Long> userIds);
}
