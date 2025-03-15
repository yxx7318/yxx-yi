package com.yxx.system.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yxx.common.utils.DateUtils;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.yxx.common.yxx.service.impl.ServiceImplPlus;
import com.yxx.system.mapper.SysResourcesMapper;
import com.yxx.system.domain.SysResources;
import com.yxx.system.service.ISysResourcesService;

/**
 * 数据源Service业务层处理
 * 
 * @author yxx
 * @date 2025-03-15
 */
@Service
@RequiredArgsConstructor
public class SysResourcesServiceImpl extends ServiceImplPlus<SysResourcesMapper, SysResources> implements ISysResourcesService {

    private final SysResourcesMapper sysResourcesMapper;

    /**
     * 查询数据源
     * 
     * @param resourceId 数据源主键
     * @return 数据源
     */
    @Override
    public SysResources selectSysResourcesByResourceId(Long resourceId)
    {
        return sysResourcesMapper.selectSysResourcesByResourceId(resourceId);
    }

    /**
     * 查询数据源列表
     * 
     * @param sysResources 数据源
     * @return 数据源
     */
    @Override
    public List<SysResources> selectSysResourcesList(SysResources sysResources)
    {
        return sysResourcesMapper.selectSysResourcesList(sysResources);
    }

    /**
     * 新增数据源
     * 
     * @param sysResources 数据源
     * @return 结果
     */
    @Override
    public int insertSysResources(SysResources sysResources)
    {
        sysResources.setCreateBy(getUserNameOrNotLogged());
        sysResources.setCreateTime(DateUtils.getNowDate());
        return sysResourcesMapper.insertSysResources(sysResources);
    }

    /**
     * 修改数据源
     * 
     * @param sysResources 数据源
     * @return 结果
     */
    @Override
    public int updateSysResources(SysResources sysResources)
    {
        sysResources.setUpdateBy(getUserNameOrNotLogged());
        sysResources.setUpdateTime(DateUtils.getNowDate());
        return sysResourcesMapper.updateSysResources(sysResources);
    }

    /**
     * 批量删除数据源
     * 
     * @param resourceIds 需要删除的数据源主键集合
     * @return 结果
     */
    @Override
    public int deleteSysResourcesByResourceIds(List<Long> resourceIds)
    {
        return sysResourcesMapper.deleteSysResourcesByResourceIds(resourceIds);
    }

    /**
     * 删除数据源信息
     * 
     * @param resourceId 数据源主键
     * @return 结果
     */
    @Override
    public int deleteSysResourcesByResourceId(Long resourceId)
    {
        return sysResourcesMapper.deleteSysResourcesByResourceId(resourceId);
    }

    @Override
    public SysResources getDynamicDbSourceByCode(String dbKey) {
        QueryWrapper<SysResources> queryWrapper = new QueryWrapper<SysResources>();

        queryWrapper.eq("code", dbKey);
        queryWrapper.eq("status", '0');
        return sysResourcesMapper.selectOne(queryWrapper);
    }
}
