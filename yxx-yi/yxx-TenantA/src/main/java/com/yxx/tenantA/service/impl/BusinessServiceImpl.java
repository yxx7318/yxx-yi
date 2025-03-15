package com.yxx.tenantA.service.impl;

import java.util.List;

import com.yxx.common.annotation.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.yxx.common.yxx.service.impl.ServiceImplPlus;
import com.yxx.tenantA.mapper.BusinessMapper;
import com.yxx.tenantA.domain.Business;
import com.yxx.tenantA.service.IBusinessService;

/**
 * xxx业务Service业务层处理
 * 
 * @author yxx
 * @date 2025-03-15
 */
@Service
@RequiredArgsConstructor
@DataSource("TenantA")
public class BusinessServiceImpl extends ServiceImplPlus<BusinessMapper, Business> implements IBusinessService {

    @Autowired
    private  BusinessMapper businessMapper;

    /**
     * 查询xxx业务
     * 
     * @param businessId xxx业务主键
     * @return xxx业务
     */
    @Override
    public Business selectBusinessByBusinessId(Long businessId)
    {
        return businessMapper.selectBusinessByBusinessId(businessId);
    }

    /**
     * 查询xxx业务列表
     * 
     * @param business xxx业务
     * @return xxx业务
     */
    @Override
    public List<Business> selectBusinessList(Business business)
    {
        return businessMapper.selectBusinessList(business);
    }

    /**
     * 新增xxx业务
     * 
     * @param business xxx业务
     * @return 结果
     */
    @Override
    public int insertBusiness(Business business)
    {
        return businessMapper.insertBusiness(business);
    }

    /**
     * 修改xxx业务
     * 
     * @param business xxx业务
     * @return 结果
     */
    @Override
    public int updateBusiness(Business business)
    {
        return businessMapper.updateBusiness(business);
    }

    /**
     * 批量删除xxx业务
     * 
     * @param businessIds 需要删除的xxx业务主键集合
     * @return 结果
     */
    @Override
    public int deleteBusinessByBusinessIds(List<Long> businessIds)
    {
        return businessMapper.deleteBusinessByBusinessIds(businessIds);
    }

    /**
     * 删除xxx业务信息
     * 
     * @param businessId xxx业务主键
     * @return 结果
     */
    @Override
    public int deleteBusinessByBusinessId(Long businessId)
    {
        return businessMapper.deleteBusinessByBusinessId(businessId);
    }
}
