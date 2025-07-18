package com.yxx.business.example.service.impl;

import java.util.List;

import com.yxx.common.utils.DateUtils;
import com.yxx.common.core.domain.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.yxx.common.core.service.impl.ServiceImplPlus;
import com.yxx.business.example.mapper.TbTestUserMapper;
import com.yxx.business.example.domain.TbTestUserDo;
import com.yxx.business.example.domain.TbTestUserVo;
import com.yxx.business.example.domain.TbTestUserQueryDto;
import com.yxx.business.example.domain.TbTestUserEditDto;
import com.yxx.business.example.service.ITbTestUserService;

/**
 * 测试用户Service业务层处理
 *
 * @author yxx
 * @date 2025-07-17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TbTestUserServiceImpl extends ServiceImplPlus<TbTestUserMapper, TbTestUserDo> implements ITbTestUserService {

    private final TbTestUserMapper tbTestUserMapper;

    /**
     * 查询测试用户分页结果
     *
     * @param tbTestUserQueryDto 测试用户查询实体
     * @return 测试用户分页
     */
    @Override
    public PageResult<TbTestUserVo> selectTbTestUserPage(TbTestUserQueryDto tbTestUserQueryDto) {
        startPage();
        PageResult<TbTestUserVo> page
                = super.getMyBatisPageResult(selectTbTestUserDoList(tbTestUserQueryDto), TbTestUserVo.class);
        clearPage();
        return page;
    }

    /**
     * 查询测试用户Vo列表
     *
     * @param tbTestUserQueryDto 测试用户查询实体
     * @return 测试用户集合
     */
    @Override
    public List<TbTestUserVo> selectTbTestUserVoList(TbTestUserQueryDto tbTestUserQueryDto) {
        return super.convertVoList(tbTestUserMapper.selectTbTestUserList(tbTestUserQueryDto), TbTestUserVo.class);
    }

    /**
     * 查询测试用户Do列表
     *
     * @param tbTestUserQueryDto 测试用户查询实体
     * @return 测试用户集合
     */
    @Override
    public List<TbTestUserDo> selectTbTestUserDoList(TbTestUserQueryDto tbTestUserQueryDto) {
        return tbTestUserMapper.selectTbTestUserList(tbTestUserQueryDto);
    }

    /**
     * 查询单个测试用户
     *
     * @param userId 测试用户主键
     * @return 测试用户单个
     */
    @Override
    public TbTestUserVo selectTbTestUserByUserId(Long userId) {
        return super.convertBean(tbTestUserMapper.selectTbTestUserByUserId(userId), TbTestUserVo.class);
    }

    /**
     * 新增测试用户
     *
     * @param tbTestUserEditDto 测试用户编辑实体
     * @return 结果
     */
    @Override
    public int insertTbTestUser(TbTestUserEditDto tbTestUserEditDto) {
        tbTestUserEditDto.setCreateBy(getUserNameOrNotLogged());
        tbTestUserEditDto.setCreateTime(DateUtils.getNowDate());
        return tbTestUserMapper.insertTbTestUser(super.convertT(tbTestUserEditDto));
    }

    /**
     * 修改测试用户
     *
     * @param userId 测试用户主键
     * @param tbTestUserEditDto 测试用户编辑实体
     * @return 结果
     */
    @Override
    public int updateTbTestUser(Long userId, TbTestUserEditDto tbTestUserEditDto) {
        tbTestUserEditDto.setUpdateBy(getUserNameOrNotLogged());
        tbTestUserEditDto.setUpdateTime(DateUtils.getNowDate());
        return tbTestUserMapper.updateTbTestUser(super.convertT(tbTestUserEditDto).setUserId(userId));
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
     * 删除单个测试用户信息
     *
     * @param userId 测试用户主键
     * @return 结果
     */
    @Override
    public int deleteTbTestUserByUserId(Long userId) {
        return tbTestUserMapper.deleteTbTestUserByUserId(userId);
    }
}
