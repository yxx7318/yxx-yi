package com.yxx.common.utils;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxx.common.core.domain.PageResult;
import com.yxx.common.core.page.PageDomain;
import com.yxx.common.core.page.TableSupport;
import com.yxx.common.utils.sql.SqlUtil;

import java.util.List;

/**
 * 分页工具类
 */
public class PageUtils extends PageHelper
{
    /**
     * 设置请求分页数据
     */
    public static void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        // 如果查询所有则不分页
        if (pageDomain.getAllData()) {
            return;
        }
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        PageHelper.startPage(pageNum, pageSize, orderBy);
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage()
    {
        PageHelper.clearPage();
    }

    /**
     * 获取到MyBatis基本分页结果，不包含行结果
     */
    public static <T, VO> PageResult<VO> getMyBatisBasePageResult(List<T> list)
    {
        PageResult<VO> result = new PageResult<>();
        // 获取分页插件的分页结果信息
        PageInfo<T> pageInfo = new PageInfo<>(list);
        BeanUtil.copyProperties(pageInfo, result);
        return result;
    }
}
