package com.yxx.business.service;

import java.util.List;

import com.yxx.common.core.domain.PageResult;
import com.yxx.business.entity.TbTestUserDO;
import com.yxx.business.entity.TbTestUserVO;
import com.yxx.business.entity.TbTestUserQueryDTO;
import com.yxx.business.entity.TbTestUserEditDTO;
import com.yxx.common.core.service.IServicePlus;

/**
 * 测试单表生成Service接口
 *
 * @author yxx
 * @date 2025-11-24
 */
public interface ITbTestUserService extends IServicePlus<TbTestUserDO> {

    /**
     * 查询测试单表生成分页结果
     *
     * @param tbTestUserQueryDTO 测试单表生成查询实体
     * @return 测试单表生成分页
     */
    public PageResult<TbTestUserVO> selectTbTestUserVOPage(TbTestUserQueryDTO tbTestUserQueryDTO);

    /**
     * 查询测试单表生成VO列表
     *
     * @param tbTestUserQueryDTO 测试单表生成查询实体
     * @return 测试单表生成集合
     */
    public List<TbTestUserVO> selectTbTestUserVOList(TbTestUserQueryDTO tbTestUserQueryDTO);

    /**
     * 查询测试单表生成DO列表
     *
     * @param tbTestUserQueryDTO 测试单表生成查询实体
     * @return 测试单表生成集合
     */
    public List<TbTestUserDO> selectTbTestUserDOList(TbTestUserQueryDTO tbTestUserQueryDTO);

    /**
     * 查询单个测试单表生成
     *
     * @param userId 测试单表生成主键
     * @return 测试单表生成单个
     */
    public TbTestUserVO selectTbTestUserVOByUserId(Long userId);

    /**
     * 新增测试单表生成
     *
     * @param tbTestUserEditDTO 测试单表生成编辑实体
     * @return 结果
     */
    public int insertTbTestUser(TbTestUserEditDTO tbTestUserEditDTO);

    /**
     * 修改测试单表生成
     *
     * @param userId 主键
     * @param tbTestUserEditDTO 测试单表生成编辑实体
     * @return 结果
     */
    public int updateTbTestUser(Long userId, TbTestUserEditDTO tbTestUserEditDTO);

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
