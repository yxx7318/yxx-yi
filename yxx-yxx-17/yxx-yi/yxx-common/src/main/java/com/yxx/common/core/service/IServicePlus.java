package com.yxx.common.core.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageHelper;
import com.yxx.common.core.domain.BaseColumnEntity;
import com.yxx.common.core.domain.PageQueryEntity;
import com.yxx.common.core.domain.model.LoginUser;
import com.yxx.common.core.page.PageDomain;
import com.yxx.common.core.page.TableSupport;
import com.yxx.common.utils.DateUtils;
import com.yxx.common.utils.LocalDateUtils;
import com.yxx.common.utils.PageUtils;
import com.yxx.common.utils.SecurityUtils;
import com.yxx.common.utils.StringUtils;
import com.yxx.common.utils.sql.SqlUtil;
import com.yxx.common.core.domain.PageResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * MyBatisPlus二次增强接口
 *
 * @param <T> 数据库实体类
 * @author yxx
 */
public interface IServicePlus<T extends BaseColumnEntity> extends IService<T> {

    /**
     * 设置请求分页数据
     */
    default void startPage() {
        PageUtils.startPage();
    }

    /**
     * 设置请求排序数据
     */
    default void startOrderBy() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        if (StringUtils.isNotEmpty(pageDomain.getOrderBy())) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
        }
    }

    /**
     * 清理分页的线程变量
     */
    default void clearPage() {
        PageUtils.clearPage();
    }

    /**
     * 获取用户缓存信息
     */
    default LoginUser getLoginUser() {
        return SecurityUtils.getLoginUser();
    }

    /**
     * 获取登录用户id
     */
    default Long getUserIdOrNotLogged() {
        return SecurityUtils.getUserIdOrNotLogged();
    }

    /**
     * 获取登录部门id
     */
    default Long getDeptIdOrNotLogged() {
        return SecurityUtils.getDeptIdOrNotLogged();
    }

    /**
     * 获取登录用户名
     */
    default String getUserNameOrNotLogged() {
        return SecurityUtils.getUsernameOrNotLogged();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     */
    default String getNowDate() {
        return DateUtils.getDate();
    }

    /**
     * 获取当前时间Date对象
     */
    default Date getNowDateTime() {
        return DateUtils.getNowDate();
    }

    /**
     * 获取当前日期LocalDate对象
     */
    default LocalDate getNowLocalDate() {
        return LocalDateUtils.getNowLocalDate();
    }

    /**
     * 获取当前时间LocalDateTime对象
     */
    default LocalDateTime getNowLocalDateTime() {
        return LocalDateUtils.getNowLocalDateTime();
    }

    /**
     * 获取转化后的Po结果
     */
    public <PO> PO convertBean(T t, Class<PO> voClass);

    /**
     * 获取转化后的Po结果
     */
    public <PO> PO convertBean(T t, Function<T, PO> convertor);

    /**
     * 获取转化后的T结果
     */
    public <PO> T convertT(PO po);

    /**
     * 获取转化后的List结果
     *
     * @param list    数据库查询结果
     * @param voClass voClass对象字节码
     * @param <PO>    PO对象
     * @param <VO>    VO对象
     * @return VoList
     */
    public <PO, VO> List<VO> convertList(List<PO> list, Class<VO> voClass);

    /**
     * 获取转化后的VoList结果
     *
     * @param list      数据库查询结果
     * @param convertor 自定义转换方法
     * @param <PO>    PO对象
     * @param <VO>    VO对象
     * @return VoList
     */
    public <PO, VO> List<VO> convertList(List<PO> list, Function<PO, VO> convertor);

    /**
     * 获取MP通用分页结果
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return 分页结果
     */
    public Page<T> getMpDoPage(Integer pageNum, Integer pageSize);

    /**
     * 获取MP通用分页结果
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @param wrapper  查询条件
     * @return 分页结果
     */
    public Page<T> getMpDoPage(Integer pageNum, Integer pageSize, Wrapper<T> wrapper);

    /**
     * 获取MP通用分页结果
     *
     * @param dto 条件对象
     * @return 分页结果
     */
    public <DTO extends PageQueryEntity> Page<T> getMpDoPage(DTO dto);

    /**
     * 获取MP通用分页结果
     *
     * @param dto     条件对象
     * @param wrapper 查询条件
     * @return 分页结果
     */
    public <DTO extends PageQueryEntity> Page<T> getMpDoPage(DTO dto, Wrapper<T> wrapper);

    /**
     * 获取到MyBatis分页结果
     *
     * @param list    分页插件查询数据
     * @param <PO>    PO对象
     * @return PageResult<PO>分页结果
     */
    public <PO> PageResult<PO> getMyBatisPageResult(List<PO> list);

    /**
     * 获取到MyBatis分页结果并转化为Vo对象分页结果
     *
     * @param list    分页插件查询数据
     * @param voClass voClass对象字节码
     * @param <VO>    VO对象
     * @return PageResult<VO>分页结果
     */
    public <PO, VO> PageResult<VO> getMyBatisPageResult(List<PO> list, Class<VO> voClass);

    /**
     * 获取到MyBatis分页结果并转化为Vo对象分页结果
     *
     * @param list      分页插件查询数据
     * @param convertor 自定义转换方法
     * @param <VO>      VO对象
     * @return PageResult<VO>分页结果
     */
    public <PO, VO> PageResult<VO> getMyBatisPageResult(List<PO> list, Function<PO, VO> convertor);

    /**
     * 根据分页参数获取MP分页结果
     *
     * @param pageNum  当前页码，如果为 null，则默认为第一页（1）
     * @param pageSize 每页大小，如果为 null，则默认为每页 10 条数据，为 0 则获取所有
     * @return 基本T对象的分页结果 PageResult<T>
     */
    public PageResult<T> getMpDoPageResult(Integer pageNum, Integer pageSize);

    /**
     * 根据分页参数获取MP分页结果和目标VO类获取分页结果
     *
     * @param pageNum  当前页码，如果为 null，则默认为第一页（1）
     * @param pageSize 每页大小，如果为 null，则默认为每页 10 条数据，为 0 则获取所有
     * @param voClass  目标VO类的Class对象，用于将查询结果转换为目标VO类型
     * @param <VO>     VO对象类型
     * @return 包含VO对象的分页结果 PageResult<VO>
     */
    public <VO> PageResult<VO> getMpVoPageResult(Integer pageNum, Integer pageSize, Class<VO> voClass);

    /**
     * 根据分页参数获取MP分页结果和目标VO类获取分页结果
     *
     * @param pageNum  当前页码，如果为 null，则默认为第一页（1）
     * @param pageSize 每页大小，如果为 null，则默认为每页 10 条数据
     * @param voClass  目标VO类的Class对象，用于将查询结果转换为目标VO类型
     * @param <VO>     VO对象类型
     * @return 包含VO对象的分页结果 PageResult<VO>
     */
    public <VO> PageResult<VO> getMpVoPageResult(Integer pageNum, Integer pageSize, Wrapper<T> wrapper, Class<VO> voClass);

    /**
     * 根据分页参数获取MP分页结果和自定义转换方法获取分页结果
     *
     * @param pageNum   当前页码，如果为 null，则默认为第一页（1）
     * @param pageSize  每页大小，如果为 null，则默认为每页 10 条数据
     * @param convertor 自定义转换方法，用于将查询结果转换为目标VO类型
     * @param <VO>      VO对象类型
     * @return 包含VO对象的分页结果 PageResult<VO>
     */
    public <VO> PageResult<VO> getMpVoPageResult(Integer pageNum, Integer pageSize, Function<T, VO> convertor);

    /**
     * 根据分页参数获取MP分页结果和自定义转换方法获取分页结果
     *
     * @param pageNum   当前页码，如果为 null，则默认为第一页（1）
     * @param pageSize  每页大小，如果为 null，则默认为每页 10 条数据
     * @param wrapper   查询条件
     * @param convertor 自定义转换方法，用于将查询结果转换为目标VO类型
     * @param <VO>      VO对象类型
     * @return 包含VO对象的分页结果 PageResult<VO>
     */
    public <VO> PageResult<VO> getMpVoPageResult(Integer pageNum, Integer pageSize, Wrapper<T> wrapper, Function<T, VO> convertor);

    /**
     * 根据对象获取分页结果
     *
     * @param dto  分页对象
     * @return 基本T对象的分页结果 PageResult<T>
     */
    public <DTO extends PageQueryEntity> PageResult<T> getMpDoPageResult(DTO dto);

    /**
     * 根据对象获取分页结果并转化目标VO类获取分页结果
     *
     * @param dto     分页对象
     * @param voClass 目标VO类的Class对象，用于将查询结果转换为目标VO类型
     * @param <VO>    VO对象类型
     * @return 包含VO对象的分页结果 PageResult<VO>
     */
    public <DTO extends PageQueryEntity, VO> PageResult<VO> getMpVoPageResult(DTO dto, Class<VO> voClass);

    /**
     * 根据对象获取分页结果并转化目标VO类获取分页结果
     *
     * @param dto       分页对象
     * @param convertor 自定义转换方法，用于将查询结果转换为目标VO类型
     * @param <VO>      VO对象类型
     * @return 包含VO对象的分页结果 PageResult<VO>
     */
    public <DTO extends PageQueryEntity, VO> PageResult<VO> getMpVoPageResult(DTO dto, Function<T, VO> convertor);

    /**
     * 根据对象和条件获取分页结果并转化目标VO类获取分页结果
     *
     * @param dto     分页对象
     * @param wrapper 查询条件
     * @param voClass 目标VO类的Class对象，用于将查询结果转换为目标VO类型
     * @param <VO>    VO对象类型
     * @return 包含VO对象的分页结果 PageResult<VO>
     */
    public <DTO extends PageQueryEntity, VO> PageResult<VO> getMpVoPageResult(DTO dto, Wrapper<T> wrapper, Class<VO> voClass);

    /**
     * 根据对象和条件获取分页结果并转化目标VO类获取分页结果
     *
     * @param dto       分页对象
     * @param wrapper   查询条件
     * @param convertor 自定义转换方法，用于将查询结果转换为目标VO类型
     * @param <VO>      VO对象类型
     * @return 包含VO对象的分页结果 PageResult<VO>
     */
    public <DTO extends PageQueryEntity, VO> PageResult<VO> getMpVoPageResult(DTO dto, Wrapper<T> wrapper, Function<T, VO> convertor);
}
