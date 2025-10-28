package com.yxx.business.service.impl;

import java.util.List;
import com.yxx.common.core.domain.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import com.yxx.common.core.service.impl.ServiceImplPlus;
import com.yxx.business.mapper.TbTestUserMapper;
import com.yxx.business.entity.TbTestUserDO;
import com.yxx.business.entity.TbTestUserVO;
import com.yxx.business.entity.TbTestUserQueryDTO;
import com.yxx.business.entity.TbTestUserEditDTO;
import com.yxx.business.service.ITbTestUserService;

/**
 * 测试单表生成Service业务层处理
 *
 * @author yxx
 * @date 2025-10-13
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class TbTestUserServiceImpl extends ServiceImplPlus<TbTestUserMapper, TbTestUserDO> implements ITbTestUserService {

    private final ITbTestUserService self;

    private final TbTestUserMapper tbTestUserMapper;

    /**
     * 查询测试单表生成分页结果
     *
     * @param tbTestUserQueryDTO 测试单表生成查询实体
     * @return 测试单表生成分页
     */
    @Override
    public PageResult<TbTestUserVO> selectTbTestUserVOPage(TbTestUserQueryDTO tbTestUserQueryDTO) {
        startPage();
        PageResult<TbTestUserVO> page
                = super.getMyBatisPageResult(self.selectTbTestUserDOList(tbTestUserQueryDTO), TbTestUserVO.class);
        clearPage();
        return page;
    }

    /**
     * 查询测试单表生成VO列表
     *
     * @param tbTestUserQueryDTO 测试单表生成查询实体
     * @return 测试单表生成集合
     */
    @Override
    public List<TbTestUserVO> selectTbTestUserVOList(TbTestUserQueryDTO tbTestUserQueryDTO) {
        return super.convertList(tbTestUserMapper.selectTbTestUserList(tbTestUserQueryDTO), TbTestUserVO.class);
    }

    /**
     * 查询测试单表生成DO列表
     *
     * @param tbTestUserQueryDTO 测试单表生成查询实体
     * @return 测试单表生成集合
     */
    @Override
    public List<TbTestUserDO> selectTbTestUserDOList(TbTestUserQueryDTO tbTestUserQueryDTO) {
        getEntityClass();
        Class<TbTestUserMapper> mapperClass = this.getMapperClass();
        return tbTestUserMapper.selectTbTestUserList(tbTestUserQueryDTO);
    }

    /**
     * 查询单个测试单表生成
     *
     * @param userId 测试单表生成主键
     * @return 测试单表生成单个
     */
    @Override
    public TbTestUserVO selectTbTestUserVOByUserId(Long userId) {
        return super.convertBean(tbTestUserMapper.selectTbTestUserByUserId(userId), TbTestUserVO.class);
    }

    /**
     * 新增测试单表生成
     *
     * @param tbTestUserEditDTO 测试单表生成编辑实体
     * @return 结果
     */
    @Override
    public int insertTbTestUser(TbTestUserEditDTO tbTestUserEditDTO) {
        tbTestUserEditDTO.fieldFillInsert();
        return tbTestUserMapper.insertTbTestUser(super.convertT(tbTestUserEditDTO));
    }

    /**
     * 修改测试单表生成
     *
     * @param userId 测试单表生成主键
     * @param tbTestUserEditDTO 测试单表生成编辑实体
     * @return 结果
     */
    @Override
    public int updateTbTestUser(Long userId, TbTestUserEditDTO tbTestUserEditDTO) {
        tbTestUserEditDTO.fieldFillUpdate();
        return tbTestUserMapper.updateTbTestUser(super.convertT(tbTestUserEditDTO).setUserId(userId));
    }

    /**
     * 批量删除测试单表生成
     *
     * @param userIds 需要删除的测试单表生成主键集合
     * @return 结果
     */
    @Override
    public int deleteTbTestUserByUserIds(List<Long> userIds) {
        return tbTestUserMapper.deleteTbTestUserByUserIds(userIds);
    }

    /**
     * 删除单个测试单表生成信息
     *
     * @param userId 测试单表生成主键
     * @return 结果
     */
    @Override
    public int deleteTbTestUserByUserId(Long userId) {
        return tbTestUserMapper.deleteTbTestUserByUserId(userId);
    }
}
