package com.yxx.business.service.impl;

import java.util.List;
import com.yxx.common.utils.DateUtils;
import com.yxx.common.core.domain.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import com.yxx.common.core.service.impl.ServiceImplPlus;
import com.yxx.business.mapper.TbTestUserMapper;
import com.yxx.business.entity.TbTestUserDo;
import com.yxx.business.entity.TbTestUserVo;
import com.yxx.business.entity.TbTestUserQueryDto;
import com.yxx.business.entity.TbTestUserEditDto;
import com.yxx.business.service.ITbTestUserService;

/**
 * 测试单生成Service业务层处理
 *
 * @author yxx
 * @date 2025-08-08
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class TbTestUserServiceImpl extends ServiceImplPlus<TbTestUserMapper, TbTestUserDo> implements ITbTestUserService {

    private final ITbTestUserService self;

    private final TbTestUserMapper tbTestUserMapper;

    /**
     * 查询测试单生成分页结果
     *
     * @param tbTestUserQueryDto 测试单生成查询实体
     * @return 测试单生成分页
     */
    @Override
    public PageResult<TbTestUserVo> selectTbTestUserVoPage(TbTestUserQueryDto tbTestUserQueryDto) {
        startPage();
        PageResult<TbTestUserVo> page
                = super.getMyBatisPageResult(self.selectTbTestUserDoList(tbTestUserQueryDto), TbTestUserVo.class);
        clearPage();
        return page;
    }

    /**
     * 查询测试单生成Vo列表
     *
     * @param tbTestUserQueryDto 测试单生成查询实体
     * @return 测试单生成集合
     */
    @Override
    public List<TbTestUserVo> selectTbTestUserVoList(TbTestUserQueryDto tbTestUserQueryDto) {
        return super.convertList(tbTestUserMapper.selectTbTestUserList(tbTestUserQueryDto), TbTestUserVo.class);
    }

    /**
     * 查询测试单生成Do列表
     *
     * @param tbTestUserQueryDto 测试单生成查询实体
     * @return 测试单生成集合
     */
    @Override
    public List<TbTestUserDo> selectTbTestUserDoList(TbTestUserQueryDto tbTestUserQueryDto) {
        return tbTestUserMapper.selectTbTestUserList(tbTestUserQueryDto);
    }

    /**
     * 查询单个测试单生成
     *
     * @param userId 测试单生成主键
     * @return 测试单生成单个
     */
    @Override
    public TbTestUserVo selectTbTestUserVoByUserId(Long userId) {
        return super.convertBean(tbTestUserMapper.selectTbTestUserByUserId(userId), TbTestUserVo.class);
    }

    /**
     * 新增测试单生成
     *
     * @param tbTestUserQueryDto 测试单生成编辑实体
     * @return 结果
     */
    @Override
    public int insertTbTestUser(TbTestUserEditDto tbTestUserQueryDto) {
        tbTestUserQueryDto.setCreateById(getUserIdOrNotLogged());
        tbTestUserQueryDto.setCreateByName(getUserNameOrNotLogged());
        tbTestUserQueryDto.setCreateTime(DateUtils.getNowDate());
        return tbTestUserMapper.insertTbTestUser(super.convertT(tbTestUserQueryDto));
    }

    /**
     * 修改测试单生成
     *
     * @param userId 测试单生成主键
     * @param tbTestUserQueryDto 测试单生成编辑实体
     * @return 结果
     */
    @Override
    public int updateTbTestUser(Long userId, TbTestUserEditDto tbTestUserQueryDto) {
        tbTestUserQueryDto.setUpdateById(getUserIdOrNotLogged());
        tbTestUserQueryDto.setUpdateByName(getUserNameOrNotLogged());
        tbTestUserQueryDto.setUpdateTime(DateUtils.getNowDate());
        return tbTestUserMapper.updateTbTestUser(super.convertT(tbTestUserQueryDto).setUserId(userId));
    }

    /**
     * 批量删除测试单生成
     *
     * @param userIds 需要删除的测试单生成主键集合
     * @return 结果
     */
    @Override
    public int deleteTbTestUserByUserIds(List<Long> userIds) {
        return tbTestUserMapper.deleteTbTestUserByUserIds(userIds);
    }

    /**
     * 删除单个测试单生成信息
     *
     * @param userId 测试单生成主键
     * @return 结果
     */
    @Override
    public int deleteTbTestUserByUserId(Long userId) {
        return tbTestUserMapper.deleteTbTestUserByUserId(userId);
    }
}
