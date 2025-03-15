package com.yxx.system.mapper;

import java.util.List;
import com.yxx.system.domain.SysResources;
import com.yxx.common.yxx.mapper.BaseMapperPlus;

/**
 * 数据源Mapper接口
 * 
 * @author yxx
 * @date 2025-03-15
 */
public interface SysResourcesMapper extends BaseMapperPlus<SysResources> {
    /**
     * 查询数据源
     * 
     * @param resourceId 数据源主键
     * @return 数据源
     */
    public SysResources selectSysResourcesByResourceId(Long resourceId);

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
     * 删除数据源
     * 
     * @param resourceId 数据源主键
     * @return 结果
     */
    public int deleteSysResourcesByResourceId(Long resourceId);

    /**
     * 批量删除数据源
     * 
     * @param resourceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysResourcesByResourceIds(List<Long> resourceIds);
}
