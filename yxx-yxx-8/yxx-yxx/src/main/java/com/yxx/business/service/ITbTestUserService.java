package com.yxx.business.service;

import java.util.List;

import com.yxx.common.core.domain.PageResult;
import com.yxx.business.entity.TbTestUserDo;
import com.yxx.business.entity.TbTestUserVo;
import com.yxx.business.entity.TbTestUserQueryDto;
import com.yxx.business.entity.TbTestUserEditDto;
import com.yxx.common.core.service.IServicePlus;

/**
 * 测试用户Service接口
 *
 * @author yxx
 * @date 2025-08-04
 */
public interface ITbTestUserService extends IServicePlus<TbTestUserDo> {

    /**
     * 查询测试用户分页结果
     *
     * @param tbTestUserQueryDto 测试用户查询实体
     * @return 测试用户分页
     */
    public PageResult<TbTestUserVo> selectTbTestUserVoPage(TbTestUserQueryDto tbTestUserQueryDto);

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
    public TbTestUserVo selectTbTestUserVoByUserId(Long userId);

    /**
     * 新增测试用户
     *
     * @param tbTestUserQueryDto 测试用户编辑实体
     * @return 结果
     */
    public int insertTbTestUser(TbTestUserEditDto tbTestUserQueryDto);

    /**
     * 修改测试用户
     *
     * @param userId 主键
     * @param tbTestUserQueryDto 测试用户编辑实体
     * @return 结果
     */
    public int updateTbTestUser(Long userId, TbTestUserEditDto tbTestUserQueryDto);

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
