package com.yxx.business.mapper;

import java.util.List;

import com.yxx.business.entity.TbTestUserSub;
import com.yxx.business.entity.TbTestUser;
import com.yxx.common.core.mapper.BaseMapperPlus;
import org.springframework.stereotype.Repository;

/**
 * 测试主表生成Mapper接口
 *
 * @author yxx
 * @date 2025-11-24
 */
@Repository
public interface TbTestUserSubMapper extends BaseMapperPlus<TbTestUserSub> {

    /**
     * 查询测试主表生成列表
     *
     * @param tbTestUserSub 测试主表生成查询实体
     * @return 测试主表生成集合
     */
    public List<TbTestUserSub> selectTbTestUserSubList(TbTestUserSub tbTestUserSub);

    /**
     * 查询测试主表生成
     *
     * @param subId 测试主表生成主键
     * @return 测试主表生成单个
     */
    public TbTestUserSub selectTbTestUserSubBySubId(Long subId);

    /**
     * 新增测试主表生成
     *
     * @param tbTestUserSub 测试主表生成数据库实体
     * @return 结果
     */
    public int insertTbTestUserSub(TbTestUserSub tbTestUserSub);

    /**
     * 修改测试主表生成
     *
     * @param tbTestUserSub 测试主表生成数据库实体
     * @return 结果
     */
    public int updateTbTestUserSub(TbTestUserSub tbTestUserSub);

    /**
     * 删除测试主表生成
     *
     * @param subId 测试主表生成主键
     * @return 结果
     */
    public int deleteTbTestUserSubBySubId(Long subId);

    /**
     * 批量删除测试主表生成
     *
     * @param subIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbTestUserSubBySubIds(List<Long> subIds);

    /**
     * 批量删除测试单表生成
     *
     * @param subIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbTestUserByParentIds(List<Long> subIds);

    /**
     * 通过测试主表生成主键删除测试单表生成信息
     *
     * @param subId 测试主表生成ID
     * @return 结果
     */
    public int deleteTbTestUserByParentId(Long subId);

    /**
     * 查询对应的ids
     *
     * @param subId 关联id
     * @return 结果
     */
    public List<Long> selectUserIdsByParentId(Long subId);

    /**
     * 新增测试单表生成
     *
     * @param tbTestUser 测试单表生成
     * @return 结果
     */
    public int insertTbTestUser(TbTestUser tbTestUser);

    /**
     * 更新测试单表生成
     *
     * @param tbTestUser 测试单表生成
     * @return 结果
     */
    public int updateTbTestUser(TbTestUser tbTestUser);

    /**
     * 删除测试单表生成
     *
     * @param userIds 测试单表生成
     * @return 结果
     */
    public int deleteTbTestUser(List<Long> userIds);
}
