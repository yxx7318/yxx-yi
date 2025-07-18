package com.yxx.business.example.service;

import java.util.List;

import com.yxx.common.core.domain.PageResult;
import com.yxx.business.example.domain.TbTestUserDo;
import com.yxx.business.example.domain.TbTestUserVo;
import com.yxx.business.example.domain.TbTestUserQueryDto;
import com.yxx.business.example.domain.TbTestUserEditDto;
import com.yxx.common.core.service.IServicePlus;

/**
 * 测试用户Service接口
 *
 * @author yxx
 * @date 2025-07-17
 */
public interface ITbTestUserService extends IServicePlus<TbTestUserDo> {

    /**
     * 查询测试用户分页结果
     *
     * @param tbTestUserQueryDto 测试用户查询实体
     * @return 测试用户分页
     */
    public PageResult<TbTestUserVo> selectTbTestUserPage(TbTestUserQueryDto tbTestUserQueryDto);

    /**
     * 查询测试用户Vo列表
     *
     * @param tbTestUserQueryDto 测试用户查询实体
     * @return 测试用户集合
     */
    public List<TbTestUserVo> selectTbTestUserVoList(TbTestUserQueryDto tbTestUserQueryDto);

    /**
     * 查询测试用户Do列表
     *
     * @param tbTestUserQueryDto 测试用户查询实体
     * @return 测试用户集合
     */
    public List<TbTestUserDo> selectTbTestUserDoList(TbTestUserQueryDto tbTestUserQueryDto);

    /**
     * 查询单个测试用户
     *
     * @param userId 测试用户主键
     * @return 测试用户单个
     */
    public TbTestUserVo selectTbTestUserByUserId(Long userId);

    /**
     * 新增测试用户
     *
     * @param tbTestUserEditDto 测试用户编辑实体
     * @return 结果
     */
    public int insertTbTestUser(TbTestUserEditDto tbTestUserEditDto);

    /**
     * 修改测试用户
     *
     * @param userId 主键
     * @param tbTestUserEditDto 测试用户编辑实体
     * @return 结果
     */
    public int updateTbTestUser(Long userId, TbTestUserEditDto tbTestUserEditDto);

    /**
     * 批量删除测试用户
     *
     * @param userIds 测试用户主键集合
     * @return 结果
     */
    public int deleteTbTestUserByUserIds(List<Long> userIds);

    /**
     * 删除单个测试用户信息
     *
     * @param userId 测试用户主键
     * @return 结果
     */
    public int deleteTbTestUserByUserId(Long userId);
}
