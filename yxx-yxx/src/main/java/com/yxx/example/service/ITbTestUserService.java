package com.yxx.example.service;

import java.util.List;
import com.yxx.example.domain.TbTestUser;
import com.yxx.common.yxx.service.IServicePlus;

/**
 * 代码生成测试Service接口
 * 
 * @author yxx
 * @date 2025-02-07
 */
public interface ITbTestUserService extends IServicePlus<TbTestUser>
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
     * 批量删除代码生成测试
     * 
     * @param userIds 需要删除的代码生成测试主键集合
     * @return 结果
     */
    public int deleteTbTestUserByUserIds(Long[] userIds);

    /**
     * 删除代码生成测试信息
     * 
     * @param userId 代码生成测试主键
     * @return 结果
     */
    public int deleteTbTestUserByUserId(Long userId);
}
