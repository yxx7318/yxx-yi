package com.yxx.business.example.mapper;

import java.util.List;

import com.yxx.business.example.domain.TbTestUser;
import com.yxx.common.core.mapper.BaseMapperPlus;

/**
 * 测试用户Mapper接口
 *
 * @author yxx
 * @date 2025-05-13
 */
public interface TbTestUserMapper extends BaseMapperPlus<TbTestUser> {
    /**
     * 查询测试用户列表
     *
     * @param tbTestUser 测试用户
     * @return 测试用户集合
     */
    public List<TbTestUser> selectTbTestUserList(TbTestUser tbTestUser);

    /**
     * 查询测试用户
     *
     * @param userId 测试用户主键
     * @return 测试用户
     */
    public TbTestUser selectTbTestUserByUserId(Long userId);

    /**
     * 新增测试用户
     *
     * @param tbTestUser 测试用户
     * @return 结果
     */
    public int insertTbTestUser(TbTestUser tbTestUser);

    /**
     * 修改测试用户
     *
     * @param tbTestUser 测试用户
     * @return 结果
     */
    public int updateTbTestUser(TbTestUser tbTestUser);

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
