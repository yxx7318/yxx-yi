package com.yxx.example.mapper;

import java.util.List;
import com.yxx.example.domain.TbTestUser;
import com.yxx.common.yxx.mapper.BaseMapperPlus;

/**
 * 代码生成测试Mapper接口
 * 
 * @author yxx
 * @date 2025-02-07
 */
public interface TbTestUserMapper extends BaseMapperPlus<TbTestUser>
{
    /**
     * 查询代码生成测试
     * 
     * @param userId 代码生成测试主键
     * @return 代码生成测试
     */
    public TbTestUser selectTbTestUserByUserId(Long userId);

    /**
     * 查询代码生成测试列表
     * 
     * @param tbTestUser 代码生成测试
     * @return 代码生成测试集合
     */
    public List<TbTestUser> selectTbTestUserList(TbTestUser tbTestUser);

    /**
     * 新增代码生成测试
     * 
     * @param tbTestUser 代码生成测试
     * @return 结果
     */
    public int insertTbTestUser(TbTestUser tbTestUser);

    /**
     * 修改代码生成测试
     * 
     * @param tbTestUser 代码生成测试
     * @return 结果
     */
    public int updateTbTestUser(TbTestUser tbTestUser);

    /**
     * 删除代码生成测试
     * 
     * @param userId 代码生成测试主键
     * @return 结果
     */
    public int deleteTbTestUserByUserId(Long userId);

    /**
     * 批量删除代码生成测试
     * 
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbTestUserByUserIds(Long[] userIds);
}
