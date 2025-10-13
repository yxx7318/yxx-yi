package com.yxx.business.service.impl;

import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Map;
import com.yxx.common.core.utils.StreamUtils;
import com.yxx.common.core.domain.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.yxx.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.yxx.business.entity.TbTestUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import com.yxx.common.core.service.impl.ServiceImplPlus;
import com.yxx.business.mapper.TbTestUserSubMapper;
import com.yxx.business.entity.TbTestUserSub;
import com.yxx.business.service.ITbTestUserSubService;

/**
 * 测试主表生成Service业务层处理
 *
 * @author yxx
 * @date 2025-10-13
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy, @Autowired})
public class TbTestUserSubServiceImpl extends ServiceImplPlus<TbTestUserSubMapper, TbTestUserSub> implements ITbTestUserSubService {

    private final ITbTestUserSubService self;

    private final TbTestUserSubMapper tbTestUserSubMapper;

    /**
     * 查询测试主表生成分页结果
     *
     * @param tbTestUserSub 测试主表生成查询实体
     * @return 测试主表生成分页
     */
    @Override
    public PageResult<TbTestUserSub> selectTbTestUserSubPage(TbTestUserSub tbTestUserSub) {
        startPage();
        PageResult<TbTestUserSub> page
                = super.getMyBatisPageResult(self.selectTbTestUserSubList(tbTestUserSub), TbTestUserSub.class);
        clearPage();
        return page;
    }

    /**
     * 查询测试主表生成DO列表
     *
     * @param tbTestUserSub 测试主表生成查询实体
     * @return 测试主表生成集合
     */
    @Override
    public List<TbTestUserSub> selectTbTestUserSubList(TbTestUserSub tbTestUserSub) {
        return tbTestUserSubMapper.selectTbTestUserSubList(tbTestUserSub);
    }

    /**
     * 查询单个测试主表生成
     *
     * @param subId 测试主表生成主键
     * @return 测试主表生成单个
     */
    @Override
    public TbTestUserSub selectTbTestUserSubBySubId(Long subId) {
        return super.convertBean(tbTestUserSubMapper.selectTbTestUserSubBySubId(subId), TbTestUserSub.class);
    }

    /**
     * 新增测试主表生成
     *
     * @param tbTestUserSub 测试主表生成编辑实体
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertTbTestUserSub(TbTestUserSub tbTestUserSub) {
    tbTestUserSub.fieldFillInsert();
        int rows = tbTestUserSubMapper.insertTbTestUserSub(tbTestUserSub);
        insertTbTestUser(tbTestUserSub.getSubId(), tbTestUserSub.getTbTestUserList());
        return rows;
    }

    /**
     * 修改测试主表生成
     *
     * @param subId 测试主表生成主键
     * @param tbTestUserSub 测试主表生成编辑实体
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateTbTestUserSub(Long subId, TbTestUserSub tbTestUserSub) {
        tbTestUserSub.fieldFillUpdate();
        HashSet<Long> ids = new HashSet<>(tbTestUserSubMapper.selectUserIdsByParentId(subId));
        tbTestUserSub.getTbTestUserList().forEach(item -> ids.remove(item.getUserId()));
        // 需要删除的子表ids
        tbTestUserSubMapper.deleteTbTestUser(new ArrayList<>(ids));
        // 构建更新和插入的list
        Map<Boolean, List<TbTestUser>> insertUpdateMap =
            StreamUtils.partitioningBy(tbTestUserSub.getTbTestUserList(), item -> item.getUserId() == null);
        updateTbTestUser(insertUpdateMap.get(false));
        insertTbTestUser(tbTestUserSub.getSubId(), insertUpdateMap.get(true));
        return tbTestUserSubMapper.updateTbTestUserSub(super.convertT(tbTestUserSub).setSubId(subId));
    }

    /**
     * 批量删除测试主表生成
     *
     * @param subIds 需要删除的测试主表生成主键集合
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteTbTestUserSubBySubIds(List<Long> subIds) {
        tbTestUserSubMapper.deleteTbTestUserByParentIds(subIds);
        return tbTestUserSubMapper.deleteTbTestUserSubBySubIds(subIds);
    }

    /**
     * 删除单个测试主表生成信息
     *
     * @param subId 测试主表生成主键
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteTbTestUserSubBySubId(Long subId) {
        tbTestUserSubMapper.deleteTbTestUserByParentId(subId);
        return tbTestUserSubMapper.deleteTbTestUserSubBySubId(subId);
    }

    /**
     * 新增测试单表生成信息
     *
     * @param subId 测试主表生成主键
     * @param insertList 插入列
     */
    public void insertTbTestUser(Long subId, List<TbTestUser> insertList) {
        if (StringUtils.isNotNull(insertList))
        {
            List<TbTestUser> list = new ArrayList<>();
            for (TbTestUser tbTestUser : insertList)
            {
                tbTestUser.setParentId(subId);
                tbTestUser.setCreateById(getUserIdOrNotLogged());
                tbTestUser.setCreateByName(getUserNameOrNotLogged());
                tbTestUser.setCreateTime(getNowLocalDateTime());
                list.add(tbTestUser);
            }
            if (list.size() > 0)
            {
                list.forEach(tbTestUserSubMapper::insertTbTestUser);
            }
        }
    }

    /**
     * 更新测试单表生成信息
     *
     * @param updateList 更新列
     */
    public void updateTbTestUser(List<TbTestUser> updateList) {
        if (StringUtils.isNotNull(updateList))
        {
            List<TbTestUser> list = new ArrayList<>();
            for (TbTestUser tbTestUser : updateList)
            {
                tbTestUser.setUpdateById(getUserIdOrNotLogged());
                tbTestUser.setUpdateByName(getUserNameOrNotLogged());
                tbTestUser.setUpdateTime(getNowLocalDateTime());
                list.add(tbTestUser);
            }
            if (list.size() > 0)
            {
                list.forEach(tbTestUserSubMapper::updateTbTestUser);
            }
        }
    }
}
