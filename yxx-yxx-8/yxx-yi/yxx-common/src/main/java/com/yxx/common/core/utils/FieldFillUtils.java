package com.yxx.common.core.utils;

import com.yxx.common.core.domain.BaseColumnEntity;
import com.yxx.common.core.domain.model.LoginUser;
import com.yxx.common.utils.LocalDateUtils;
import com.yxx.common.utils.SecurityUtils;
import com.yxx.common.utils.StringUtils;
import org.springframework.util.Assert;

import java.util.Objects;


/**
 * 填充公共字段工具类
 */
public class FieldFillUtils {

    /**
     * 填充插入时的公共字段
     *
     * @param t   继承BaseColumnEntity的对象
     * @param <T> 继承BaseColumnEntity的实体类
     * @return t
     */
    public static <T extends BaseColumnEntity> T fieldFillInsert(T t) {
        if (Objects.isNull(t)) {
            return null;
        }
        if (StringUtils.isNull(t.getCreateById())) {
            t.setCreateById(SecurityUtils.getUserIdOrNotLogged());
        }
        if (StringUtils.isNull(t.getCreateByName())) {
            t.setCreateByName(SecurityUtils.getUsernameOrNotLogged());
        }
        if (StringUtils.isNull(t.getCreateTime())) {
            t.setCreateTime(LocalDateUtils.getNowLocalDateTime());
        }
        return t;
    }

    /**
     * 填充插入时的公共字段
     *
     * @param t   继承BaseColumnEntity的对象
     * @param <T> 继承BaseColumnEntity的实体类
     * @return t
     */
    public static <T extends BaseColumnEntity> T fieldFillUpdate(T t) {
        if (Objects.isNull(t)) {
            return null;
        }
        if (StringUtils.isNull(t.getUpdateById())) {
            t.setUpdateById(SecurityUtils.getUserIdOrNotLogged());
        }
        if (StringUtils.isNull(t.getUpdateByName())) {
            t.setUpdateByName(SecurityUtils.getUsernameOrNotLogged());
        }
        if (StringUtils.isNull(t.getUpdateTime())) {
            t.setUpdateTime(LocalDateUtils.getNowLocalDateTime());
        }
        return t;
    }

    /**
     * 填充插入时的公共字段通过登录数据
     *
     * @param t         继承BaseColumnEntity的对象
     * @param <T>       继承BaseColumnEntity的实体类
     * @param loginUser 登录用户
     * @return t
     */
    public static <T extends BaseColumnEntity> T fieldFillInsertByLoginUser(T t, LoginUser loginUser) {
        Assert.notNull(loginUser, "loginUser can not be null");
        if (Objects.isNull(t)) {
            return null;
        }
        if (StringUtils.isNull(t.getCreateById()) && StringUtils.isNotNull(loginUser.getUserId())) {
            t.setCreateById(loginUser.getUserId());
        } else {
            t.setCreateById(SecurityUtils.getUserIdOrNotLogged());
        }
        if (StringUtils.isNull(t.getCreateByName()) && StringUtils.isNotNull(loginUser.getUsername())) {
            t.setCreateByName(loginUser.getUsername());
        } else {
            t.setCreateByName(SecurityUtils.getUsernameOrNotLogged());
        }
        if (StringUtils.isNull(t.getCreateTime())) {
            t.setCreateTime(LocalDateUtils.getNowLocalDateTime());
        }
        return t;
    }

    /**
     * 填充插入时的公共字段
     *
     * @param t         继承BaseColumnEntity的对象
     * @param <T>       继承BaseColumnEntity的实体类
     * @param loginUser 登录用户
     * @return t
     */
    public static <T extends BaseColumnEntity> T fieldFillUpdateByLoginUser(T t, LoginUser loginUser) {
        Assert.notNull(loginUser, "loginUser can not be null");
        if (Objects.isNull(t)) {
            return null;
        }
        if (StringUtils.isNull(t.getUpdateById()) && StringUtils.isNotNull(loginUser.getUserId())) {
            t.setUpdateById(loginUser.getUserId());
        } else {
            t.setUpdateById(SecurityUtils.getUserIdOrNotLogged());
        }
        if (StringUtils.isNull(t.getUpdateByName()) && StringUtils.isNotNull(loginUser.getUsername())) {
            t.setUpdateByName(loginUser.getUsername());
        } else {
            t.setUpdateByName(SecurityUtils.getUsernameOrNotLogged());
        }
        if (StringUtils.isNull(t.getUpdateTime())) {
            t.setUpdateTime(LocalDateUtils.getNowLocalDateTime());
        }
        return t;
    }
}
