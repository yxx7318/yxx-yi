package com.yxx.common.core.utils;

import com.yxx.common.core.domain.BaseColumnEntity;
import com.yxx.common.utils.LocalDateUtils;
import com.yxx.common.utils.SecurityUtils;

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
        t.setCreateById(SecurityUtils.getUserIdOrNotLogged());
        t.setCreateByName(SecurityUtils.getUsernameOrNotLogged());
        t.setCreateTime(LocalDateUtils.getNowLocalDateTime());
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
        t.setUpdateById(SecurityUtils.getUserIdOrNotLogged());
        t.setUpdateByName(SecurityUtils.getUsernameOrNotLogged());
        t.setUpdateTime(LocalDateUtils.getNowLocalDateTime());
        return t;
    }
}
