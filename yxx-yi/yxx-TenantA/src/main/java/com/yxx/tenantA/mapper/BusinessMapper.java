package com.yxx.tenantA.mapper;

import java.util.List;
import com.yxx.tenantA.domain.Business;
import com.yxx.common.yxx.mapper.BaseMapperPlus;

/**
 * xxx业务Mapper接口
 * 
 * @author yxx
 * @date 2025-03-15
 */
public interface BusinessMapper extends BaseMapperPlus<Business> {
    /**
     * 查询xxx业务
     * 
     * @param businessId xxx业务主键
     * @return xxx业务
     */
    public Business selectBusinessByBusinessId(Long businessId);

    /**
     * 查询xxx业务列表
     * 
     * @param business xxx业务
     * @return xxx业务集合
     */
    public List<Business> selectBusinessList(Business business);

    /**
     * 新增xxx业务
     * 
     * @param business xxx业务
     * @return 结果
     */
    public int insertBusiness(Business business);

    /**
     * 修改xxx业务
     * 
     * @param business xxx业务
     * @return 结果
     */
    public int updateBusiness(Business business);

    /**
     * 删除xxx业务
     * 
     * @param businessId xxx业务主键
     * @return 结果
     */
    public int deleteBusinessByBusinessId(Long businessId);

    /**
     * 批量删除xxx业务
     * 
     * @param businessIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusinessByBusinessIds(List<Long> businessIds);
}
