package com.yxx.common.yxx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxx.common.core.domain.BaseEntity;
import com.yxx.common.core.domain.model.LoginUser;
import com.yxx.common.utils.SecurityUtils;

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
}
