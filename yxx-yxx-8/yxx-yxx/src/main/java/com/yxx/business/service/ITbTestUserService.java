package com.yxx.business.service;

import java.util.List;

import com.yxx.common.core.domain.PageResult;
import com.yxx.business.entity.TbTestUserDo;
import com.yxx.business.entity.TbTestUserVo;
import com.yxx.business.entity.TbTestUserQueryDto;
import com.yxx.business.entity.TbTestUserEditDto;
import com.yxx.common.core.service.IServicePlus;

/**
 * 测试单表生成Service接口
 *
 * @author yxx
 * @date 2025-08-11
 */
public interface ITbTestUserService extends IServicePlus<TbTestUserDo> {

    /**
     * 查询测试单表生成分页结果
     *
     * @param tbTestUserQueryDto 测试单表生成查询实体
     * @return 测试单表生成分页
     */
    public PageResult<TbTestUserVo> selectTbTestUserVoPage(TbTestUserQueryDto tbTestUserQueryDto);

    /**
     * 查询测试单表生成Vo列表
     *
     * @param tbTestUserQueryDto 测试单表生成查询实体
     * @return 测试单表生成集合
     */
    public List<TbTestUserVo> selectTbTestUserVoList(TbTestUserQueryDto tbTestUserQueryDto);

    /**
     * 查询测试单表生成Do列表
     *
     * @param tbTestUserQueryDto 测试单表生成查询实体
     * @return 测试单表生成集合
     */
    public List<TbTestUserDo> selectTbTestUserDoList(TbTestUserQueryDto tbTestUserQueryDto);

    /**
     * 查询单个测试单表生成
     *
     * @param userId 测试单表生成主键
     * @return 测试单表生成单个
     */
    public TbTestUserVo selectTbTestUserVoByUserId(Long userId);

    /**
     * 新增测试单表生成
     *
     * @param tbTestUserEditDto 测试单表生成编辑实体
     * @return 结果
     */
    public int insertTbTestUser(TbTestUserEditDto tbTestUserEditDto);

    /**
     * 修改测试单表生成
     *
     * @param userId 主键
     * @param tbTestUserEditDto 测试单表生成编辑实体
     * @return 结果
     */
    public int updateTbTestUser(Long userId, TbTestUserEditDto tbTestUserEditDto);

    /**
     * 批量删除测试单表生成
     *
     * @param userIds 测试单表生成主键集合
     * @return 结果
     */
    public int deleteTbTestUserByUserIds(List<Long> userIds);

    /**
     * 删除单个测试单表生成信息
     *
     * @param userId 测试单表生成主键
     * @return 结果
     */
    public int deleteTbTestUserByUserId(Long userId);
}
