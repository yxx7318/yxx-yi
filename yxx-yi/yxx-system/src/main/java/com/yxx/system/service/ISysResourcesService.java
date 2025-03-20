package com.yxx.system.service;

import java.util.List;
import com.yxx.system.domain.SysResources;
import com.yxx.common.yxx.service.IServicePlus;

/**
 * 数据源Service接口
 * 
 * @author yxx
 * @date 2025-03-15
 */
public interface ISysResourcesService extends IServicePlus<SysResources> {
    /**
     * 查询数据源
     * 
     * @param resourceId 数据源主键
     * @return 数据源
     */
    public SysResources selectSysResourcesByResourceId(Long resourceId);

    /**
     * 根据id查一组数据
     * @param resourceIds ids
     * @return
     */
    public List<SysResources> selectSysResourcesByResourceIds(List<Long> resourceIds);

    /**
     * 查询数据源列表
     * 
     * @param sysResources 数据源
     * @return 数据源集合
     */
    public List<SysResources> selectSysResourcesList(SysResources sysResources);

    /**
     * 新增数据源
     * 
     * @param sysResources 数据源
     * @return 结果
     */
    public int insertSysResources(SysResources sysResources);

    /**
     * 修改数据源
     * 
     * @param sysResources 数据源
     * @return 结果
     */
    public int updateSysResources(SysResources sysResources);

    /**
     * 批量删除数据源
     * 
     * @param resourceIds 需要删除的数据源主键集合
     * @return 结果
     */
    public int deleteSysResourcesByResourceIds(List<Long> resourceIds);

    /**
     * 删除数据源信息
     * 
     * @param resourceId 数据源主键
     * @return 结果
     */
    public int deleteSysResourcesByResourceId(Long resourceId);

    /**
     * 根据key获取数据源
     * @param dbKey
     * @return
     */
    SysResources getDynamicDbSourceByCode(String dbKey);
}
