package com.yxx.framework.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.yxx.common.utils.DateUtils;
import com.yxx.common.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * 自定义元数据对象处理器
 */
@Component
public class MPMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入操作，自动填充
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasSetter("createTime")) {
            this.strictInsertFill(metaObject, "createTime", DateUtils::getNowDate, Date.class);
        }
        if (metaObject.hasSetter("updateTime")) {
            this.strictInsertFill(metaObject, "updateTime", DateUtils::getNowDate, Date.class);
        }
        if (metaObject.hasSetter("createById")) {
            this.strictInsertFill(metaObject, "createById", SecurityUtils::getUsernameOrNotLogged, String.class);
        }
        if (metaObject.hasSetter("updateById")) {
            this.strictInsertFill(metaObject, "updateById", SecurityUtils::getUsernameOrNotLogged, String.class);
        }
        if (metaObject.hasSetter("createByName")) {
            this.strictInsertFill(metaObject, "createByName", SecurityUtils::getUserIdOrNotLogged, Long.class);
        }
        if (metaObject.hasSetter("updateByName")) {
            this.strictInsertFill(metaObject, "updateByName", SecurityUtils::getUserIdOrNotLogged, Long.class);
        }
    }

    /**
     * 更新操作，自动填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter("updateTime")) {
            this.strictUpdateFill(metaObject, "updateTime", DateUtils::getNowDate, Date.class);
        }
        if (metaObject.hasSetter("updateById")) {
            this.strictUpdateFill(metaObject, "updateById", SecurityUtils::getUsernameOrNotLogged, String.class);
        }
        if (metaObject.hasSetter("updateByName")) {
            this.strictUpdateFill(metaObject, "updateByName", SecurityUtils::getUserIdOrNotLogged, Long.class);
        }
    }
}