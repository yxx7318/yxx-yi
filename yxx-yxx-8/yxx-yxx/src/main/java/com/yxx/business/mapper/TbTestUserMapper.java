package com.yxx.business.mapper;

import java.util.List;

import com.yxx.business.entity.TbTestUserDO;
import com.yxx.business.entity.TbTestUserQueryDTO;
import com.yxx.common.core.mapper.BaseMapperPlus;
import org.springframework.stereotype.Repository;

/**
 * 测试单表生成Mapper接口
 *
 * @author yxx
 * @date 2025-10-13
 */
@Repository
public interface TbTestUserMapper extends BaseMapperPlus<TbTestUserDO> {

    /**
     * 查询测试单表生成列表
     *
     * @param tbTestUserQueryDTO 测试单表生成查询实体
     * @return 测试单表生成集合
     */
    public List<TbTestUserDO> selectTbTestUserList(TbTestUserQueryDTO tbTestUserQueryDTO);

    /**
     * 查询测试单表生成
     *
     * @param userId 测试单表生成主键
     * @return 测试单表生成单个
     */
    public TbTestUserDO selectTbTestUserByUserId(Long userId);

    /**
     * 新增测试单表生成
     *
     * @param tbTestUserDO 测试单表生成数据库实体
     * @return 结果
     */
    public int insertTbTestUser(TbTestUserDO tbTestUserDO);

    /**
     * 修改测试单表生成
     *
     * @param tbTestUserDO 测试单表生成数据库实体
     * @return 结果
     */
    public int updateTbTestUser(TbTestUserDO tbTestUserDO);

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
