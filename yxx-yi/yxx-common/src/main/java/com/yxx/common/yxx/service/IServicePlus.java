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
     *
     * @param list 分页插件分页数据
     * @param voClass class对象字节码
     * @return PageResult<VO>分页结果
     * @param <VO> VO对象
     */
    public <VO> PageResult<VO> getPageResult(List<T> list, Class<VO> voClass);

    /**
     *
     * @param list 分页插件分页数据
     * @param convertor 自定义转换方法
     * @return PageResult<VO>分页结果
     * @param <VO> VO对象
     */
    public <VO> PageResult<VO> getPageResult(List<T> list, Function<T, VO> convertor);
}
