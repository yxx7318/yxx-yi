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
        if (metaObject.hasSetter("createBy")) {
            this.strictInsertFill(metaObject, "createBy", SecurityUtils::getUsernameOrNotLogged, String.class);
        }
        if (metaObject.hasSetter("updateBy")) {
            this.strictInsertFill(metaObject, "updateBy", SecurityUtils::getUsernameOrNotLogged, String.class);
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
        if (metaObject.hasSetter("updateBy")) {
            this.strictUpdateFill(metaObject, "updateBy", SecurityUtils::getUsernameOrNotLogged, String.class);
        }
    }
}