package com.yxx.common.yxx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxx.common.core.domain.BaseEntity;
import com.yxx.common.core.domain.model.LoginUser;
import com.yxx.common.utils.SecurityUtils;
import com.yxx.common.yxx.domain.PageResult;

import java.util.List;
import java.util.function.Function;

/**
 * MyBatisPlus二次增强接口
 *
 * @param <T> 数据库实体类
 */
public interface IServicePlus<T extends BaseEntity> extends IService<T> {

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
     * 获取转化后的Vo结果
     */
    public <VO> VO getVo(T t, Class<VO> voClass);

    /**
     * 获取转化后的Vo结果
     */
    public <VO> VO getVo(T t, Function<T, VO> convertor);

    /**
     * 获取转化后的VoList结果
     *
     * @param list    数据库查询数据
     * @param voClass voClass对象字节码
     * @param <VO>    VO对象
     * @return VoList
     */
    public <VO> List<VO> getVoList(List<T> list, Class<VO> voClass);

    /**
     * 获取转化后的VoList结果
     *
     * @param list      数据库查询数据
     * @param convertor 自定义转换方法
     * @param <VO>      VO对象
     * @return VoList
     */
    public <VO> List<VO> getVoList(List<T> list, Function<T, VO> convertor);

    /**
     * 获取到Vo对象分页结果
     *
     * @param list    分页插件查询数据
     * @param voClass voClass对象字节码
     * @param <VO>    VO对象
     * @return PageResult<VO>分页结果
     */
    public <VO> PageResult<VO> getPageResult(List<T> list, Class<VO> voClass);

    /**
     * 获取到Vo对象分页结果
     *
     * @param list      分页插件查询数据
     * @param convertor 自定义转换方法
     * @param <VO>      VO对象
     * @return PageResult<VO>分页结果
     */
    public <VO> PageResult<VO> getPageResult(List<T> list, Function<T, VO> convertor);
}
