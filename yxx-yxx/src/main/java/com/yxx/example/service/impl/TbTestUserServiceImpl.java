package com.yxx.example.service.impl;

import java.util.List;

import com.yxx.common.utils.DateUtils;
import com.yxx.common.core.domain.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.yxx.common.core.service.impl.ServiceImplPlus;
import com.yxx.example.mapper.TbTestUserMapper;
import com.yxx.example.domain.TbTestUser;
import com.yxx.example.service.ITbTestUserService;

/**
 * 测试用户Service业务层处理
 *
 * @author yxx
 * @date 2025-04-29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TbTestUserServiceImpl extends ServiceImplPlus<TbTestUserMapper, TbTestUser> implements ITbTestUserService {

    private final TbTestUserMapper tbTestUserMapper;

    /**
     * 查询测试用户分页结果
     *
     * @param tbTestUser 测试用户
     * @return 测试用户
     */
    @Override
    public PageResult<TbTestUser> selectTbTestUserPage(TbTestUser tbTestUser) {
        startPage();
        PageResult<TbTestUser> page = getMyBatisPageResult(tbTestUserMapper.selectTbTestUserList(tbTestUser));
        clearPage();
        return page;
    }

    /**
     * 查询测试用户列表
     *
     * @param tbTestUser 测试用户
     * @return 测试用户
     */
    @Override
    public List<TbTestUser> selectTbTestUserList(TbTestUser tbTestUser) {
        return tbTestUserMapper.selectTbTestUserList(tbTestUser);
    }

    /**
     * 查询测试用户
     *
     * @param userId 测试用户主键
     * @return 测试用户
     */
    @Override
    public TbTestUser selectTbTestUserByUserId(Long userId) {
        return tbTestUserMapper.selectTbTestUserByUserId(userId);
    }

    /**
     * 新增测试用户
     *
     * @param tbTestUser 测试用户
     * @return 结果
     */
    @Override
    public int insertTbTestUser(TbTestUser tbTestUser) {
        tbTestUser.setCreateBy(getUserNameOrNotLogged());
        tbTestUser.setCreateTime(DateUtils.getNowDate());
        return tbTestUserMapper.insertTbTestUser(tbTestUser);
    }

    /**
     * 修改测试用户
     *
     * @param tbTestUser 测试用户
     * @return 结果
     */
    @Override
    public int updateTbTestUser(TbTestUser tbTestUser) {
        tbTestUser.setUpdateBy(getUserNameOrNotLogged());
        tbTestUser.setUpdateTime(DateUtils.getNowDate());
        return tbTestUserMapper.updateTbTestUser(tbTestUser);
    }

    /**
     * 批量删除测试用户
     *
     * @param userIds 需要删除的测试用户主键集合
     * @return 结果
     */
    @Override
    public int deleteTbTestUserByUserIds(List<Long> userIds) {
        return tbTestUserMapper.deleteTbTestUserByUserIds(userIds);
    }

    /**
     * 删除测试用户信息
     *
     * @param userId 测试用户主键
     * @return 结果
     */
    @Override
    public int deleteTbTestUserByUserId(Long userId) {
        return tbTestUserMapper.deleteTbTestUserByUserId(userId);
    }
}
