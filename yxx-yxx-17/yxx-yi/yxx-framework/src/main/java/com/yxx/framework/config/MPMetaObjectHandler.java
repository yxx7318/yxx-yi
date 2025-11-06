package com.yxx.framework.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.yxx.common.utils.LocalDateUtils;
import com.yxx.common.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义元数据对象处理器(影响MyBatis)
 * <p>
 * metaObject.setValue() 固定填充
 * strictInsertFill()或 strictUpdateFill()方法可以根据注解 FieldFill.xxx、字段名、属性值和字段类型来区分填充逻辑
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
        this.strictInsertFill(metaObject, "createTime", LocalDateUtils::getNowLocalDateTime, LocalDateTime.class);
        this.strictInsertFill(metaObject, "createById", SecurityUtils::getUserIdOrNotLogged, Long.class);
        this.strictInsertFill(metaObject, "createByName", SecurityUtils::getUsernameOrNotLogged, String.class);
    }

    /**
     * 更新操作，自动填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateUtils::getNowLocalDateTime, LocalDateTime.class);
        this.strictUpdateFill(metaObject, "updateById", SecurityUtils::getUserIdOrNotLogged, Long.class);
        this.strictUpdateFill(metaObject, "updateByName", SecurityUtils::getUsernameOrNotLogged, String.class);
    }
}