package com.yxx.business.service.impl;

import java.util.List;
import com.yxx.common.core.domain.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import com.yxx.common.core.service.impl.ServiceImplPlus;
import com.yxx.business.mapper.TbTestUserTreeMapper;
import com.yxx.business.entity.TbTestUserTree;
import com.yxx.business.service.ITbTestUserTreeService;

/**
 * 测试树表生成Service业务层处理
 *
 * @author yxx
 * @date 2025-08-14
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class TbTestUserTreeServiceImpl extends ServiceImplPlus<TbTestUserTreeMapper, TbTestUserTree> implements ITbTestUserTreeService {

    private final ITbTestUserTreeService self;

    private final TbTestUserTreeMapper tbTestUserTreeMapper;

    /**
     * 查询测试树表生成分页结果
     *
     * @param tbTestUserTree 测试树表生成查询实体
     * @return 测试树表生成分页
     */
    @Override
    public PageResult<TbTestUserTree> selectTbTestUserTreePage(TbTestUserTree tbTestUserTree) {
        startPage();
        PageResult<TbTestUserTree> page
                = super.getMyBatisPageResult(self.selectTbTestUserTreeList(tbTestUserTree), TbTestUserTree.class);
        clearPage();
        return page;
    }

    /**
     * 查询测试树表生成Do列表
     *
     * @param tbTestUserTree 测试树表生成查询实体
     * @return 测试树表生成集合
     */
    @Override
    public List<TbTestUserTree> selectTbTestUserTreeList(TbTestUserTree tbTestUserTree) {
        return tbTestUserTreeMapper.selectTbTestUserTreeList(tbTestUserTree);
    }

    /**
     * 查询单个测试树表生成
     *
     * @param userId 测试树表生成主键
     * @return 测试树表生成单个
     */
    @Override
    public TbTestUserTree selectTbTestUserTreeByUserId(Long userId) {
        return super.convertBean(tbTestUserTreeMapper.selectTbTestUserTreeByUserId(userId), TbTestUserTree.class);
    }

    /**
     * 新增测试树表生成
     *
     * @param tbTestUserTree 测试树表生成编辑实体
     * @return 结果
     */
    @Override
    public int insertTbTestUserTree(TbTestUserTree tbTestUserTree) {
        tbTestUserTree.setCreateById(getUserIdOrNotLogged());
        tbTestUserTree.setCreateByName(getUserNameOrNotLogged());
        tbTestUserTree.setCreateTime(getNowLocalDateTime());
        return tbTestUserTreeMapper.insertTbTestUserTree(super.convertT(tbTestUserTree));
    }

    /**
     * 修改测试树表生成
     *
     * @param userId 测试树表生成主键
     * @param tbTestUserTree 测试树表生成编辑实体
     * @return 结果
     */
    @Override
    public int updateTbTestUserTree(Long userId, TbTestUserTree tbTestUserTree) {
        tbTestUserTree.setUpdateById(getUserIdOrNotLogged());
        tbTestUserTree.setUpdateByName(getUserNameOrNotLogged());
        tbTestUserTree.setUpdateTime(getNowLocalDateTime());
        return tbTestUserTreeMapper.updateTbTestUserTree(super.convertT(tbTestUserTree).setUserId(userId));
    }

    /**
     * 批量删除测试树表生成
     *
     * @param userIds 需要删除的测试树表生成主键集合
     * @return 结果
     */
    @Override
    public int deleteTbTestUserTreeByUserIds(List<Long> userIds) {
        return tbTestUserTreeMapper.deleteTbTestUserTreeByUserIds(userIds);
    }

    /**
     * 删除单个测试树表生成信息
     *
     * @param userId 测试树表生成主键
     * @return 结果
     */
    @Override
    public int deleteTbTestUserTreeByUserId(Long userId) {
        return tbTestUserTreeMapper.deleteTbTestUserTreeByUserId(userId);
    }
}
