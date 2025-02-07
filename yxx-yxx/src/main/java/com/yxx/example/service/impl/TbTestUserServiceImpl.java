package com.yxx.example.service.impl;

import java.util.List;
import com.yxx.common.utils.DateUtils;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.yxx.common.yxx.service.impl.ServiceImplPlus;
import com.yxx.example.mapper.TbTestUserMapper;
import com.yxx.example.domain.TbTestUser;
import com.yxx.example.service.ITbTestUserService;

/**
 * 代码生成测试Service业务层处理
 * 
 * @author yxx
 * @date 2025-02-07
 */
@Service
@RequiredArgsConstructor
public class TbTestUserServiceImpl extends ServiceImplPlus<TbTestUserMapper, TbTestUser> implements ITbTestUserService
{
    private final TbTestUserMapper tbTestUserMapper;

    /**
     * 查询代码生成测试
     * 
     * @param userId 代码生成测试主键
     * @return 代码生成测试
     */
    @Override
    public TbTestUser selectTbTestUserByUserId(Long userId)
    {
        return tbTestUserMapper.selectTbTestUserByUserId(userId);
    }

    /**
     * 查询代码生成测试列表
     * 
     * @param tbTestUser 代码生成测试
     * @return 代码生成测试
     */
    @Override
    public List<TbTestUser> selectTbTestUserList(TbTestUser tbTestUser)
    {
        return tbTestUserMapper.selectTbTestUserList(tbTestUser);
    }

    /**
     * 新增代码生成测试
     * 
     * @param tbTestUser 代码生成测试
     * @return 结果
     */
    @Override
    public int insertTbTestUser(TbTestUser tbTestUser)
    {
        tbTestUser.setCreateTime(DateUtils.getNowDate());
        tbTestUser.setCreateBy(getUserNameOrNotLogged());
        return tbTestUserMapper.insertTbTestUser(tbTestUser);
    }

    /**
     * 修改代码生成测试
     * 
     * @param tbTestUser 代码生成测试
     * @return 结果
     */
    @Override
    public int updateTbTestUser(TbTestUser tbTestUser)
    {
        tbTestUser.setUpdateTime(DateUtils.getNowDate());
        tbTestUser.setUpdateBy(getUserNameOrNotLogged());
        return tbTestUserMapper.updateTbTestUser(tbTestUser);
    }

    /**
     * 批量删除代码生成测试
     * 
     * @param userIds 需要删除的代码生成测试主键
     * @return 结果
     */
    @Override
    public int deleteTbTestUserByUserIds(Long[] userIds)
    {
        return tbTestUserMapper.deleteTbTestUserByUserIds(userIds);
    }

    /**
     * 删除代码生成测试信息
     * 
     * @param userId 代码生成测试主键
     * @return 结果
     */
    @Override
    public int deleteTbTestUserByUserId(Long userId)
    {
        return tbTestUserMapper.deleteTbTestUserByUserId(userId);
    }
}
