package com.yxx.framework.yxx.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.yxx.common.utils.DateUtils;
import com.yxx.common.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 自定义元数据对象处理器
 */
@Component
public class MPMetaObjectHandler implements MetaObjectHandler {

    private static final String NO_LOGON = "noLogin";

    /**
     * 插入操作，自动填充
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", DateUtils.getNowDate());
        metaObject.setValue("updateTime", DateUtils.getNowDate());
        if (isUserLoggedIn()) {
            metaObject.setValue("createBy", SecurityUtils.getUsername());
            metaObject.setValue("updateBy", SecurityUtils.getUsername());
        } else {
            metaObject.setValue("createBy", NO_LOGON);
            metaObject.setValue("updateBy", NO_LOGON);
        }
    }

    /**
     * 更新操作，自动填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", DateUtils.getNowDate());
        if (isUserLoggedIn()) {
            metaObject.setValue("updateBy", SecurityUtils.getUsername());
        } else {
            metaObject.setValue("updateBy", NO_LOGON);
        }
    }

    /**
     * 判断用户是否已经登录了
     *
     * @return 是否已经登录
     */
    private boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 检查Authentication是否为null或者是否为AnonymousAuthenticationToken的实例，用户未登录或为匿名用户
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }
}