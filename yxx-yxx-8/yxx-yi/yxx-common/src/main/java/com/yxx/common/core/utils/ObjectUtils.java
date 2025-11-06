package com.yxx.common.core.utils;

import cn.hutool.core.util.ObjectUtil;

import java.util.Arrays;
import java.util.function.Function;

/**
 * 对象工具类
 */
public class ObjectUtils extends ObjectUtil {

    private ObjectUtils() {
    }

    /**
     * 如果对象不为空，则获取对象中的某个字段 ObjectUtils.notNullGetter(user, User::getName);
     *
     * @param obj  对象
     * @param func 获取方法
     * @return 对象字段
     */
    public static <T, E> E notNullGetter(T obj, Function<T, E> func) {
        if (isNotNull(obj) && isNotNull(func)) {
            return func.apply(obj);
        }
        return null;
    }

    /**
     * 如果对象不为空，则获取对象中的某个字段，否则返回默认值
     *
     * @param obj          对象
     * @param func         获取方法
     * @param defaultValue 默认值
     * @return 对象字段
     */
    public static <T, E> E notNullGetter(T obj, Function<T, E> func, E defaultValue) {
        if (isNotNull(obj) && isNotNull(func)) {
            return func.apply(obj);
        }
        return defaultValue;
    }

    /**
     * 如果值不为空，则返回值，否则返回默认值
     *
     * @param obj          对象
     * @param defaultValue 默认值
     * @return 对象字段
     */
    public static <T> T notNull(T obj, T defaultValue) {
        if (isNotNull(obj)) {
            return obj;
        }
        return defaultValue;
    }

    /**
     * 判断指定对象是否与任意一个给定参数相等
     *
     * @param <T>   对象类型参数
     * @param obj   要检查的目标对象 (可为 null)
     * @param array 用于比较的对象数组 (可为 null 或空数组)
     * @return 如果目标对象与数组中任意元素相等返回 true，否则返回 false
     */
    public static <T> boolean equalsAny(T obj, T... array) {
        if (array == null) {
            return false;
        }
        return Arrays.asList(array).contains(obj);
    }

}
