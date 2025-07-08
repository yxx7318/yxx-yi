package com.yxx.business.example.service;

import java.util.List;

import com.yxx.common.core.domain.PageResult;
import com.yxx.business.example.domain.TbTestUser;
import com.yxx.business.example.domain.TbTestUserQueryDto;
import com.yxx.business.example.domain.TbTestUserEditDto;
import com.yxx.business.example.domain.TbTestUserVo;
import com.yxx.common.core.service.IServicePlus;

/**
 * 测试用户Service接口
 *
 * @author yxx
 * @date 2025-06-03
 */
public interface ITbTestUserService extends IServicePlus<TbTestUser> {
    /**
     * 查询测试用户分页结果
     *
     * @param tbTestUserQueryDto 测试用户
     * @return 测试用户分页结果
     */
    public PageResult<TbTestUserVo> selectTbTestUserVoPage(TbTestUserQueryDto tbTestUserQueryDto);

    /**
     * 查询测试用户Vo列表
     *
     * @param tbTestUserQueryDto 测试用户
     * @return 测试用户集合
     */
    public List<TbTestUserVo> selectTbTestUserVoList(TbTestUserQueryDto tbTestUserQueryDto);

    /**
     * 查询测试用户列表
     *
     * @param tbTestUserQueryDto 测试用户
     * @return 测试用户集合
     */
    public List<TbTestUser> selectTbTestUserList(TbTestUserQueryDto tbTestUserQueryDto);

    /**
     * 查询测试用户
     *
     * @param userId 测试用户主键
     * @return 测试用户
     */
    public TbTestUserVo selectTbTestUserByUserId(Long userId);

    /**
     * 新增测试用户
     *
     * @param tbTestUserEditDto 测试用户
     * @return 结果
     */
    public int insertTbTestUser(TbTestUserEditDto tbTestUserEditDto);

    /**
     * 修改测试用户
     *
     * @param tbTestUserEditDto 测试用户
     * @return 结果
     */
    public int updateTbTestUser(Long userId, TbTestUserEditDto tbTestUserEditDto);

    /**
     * 批量删除测试用户
     *
     * @param userIds 需要删除的测试用户主键集合
     * @return 结果
     */
    public int deleteTbTestUserByUserIds(List<Long> userIds);

    /**
     * 删除测试用户信息
     *
     * @param userId 测试用户主键
     * @return 结果
     */
    public int deleteTbTestUserByUserId(Long userId);
}
