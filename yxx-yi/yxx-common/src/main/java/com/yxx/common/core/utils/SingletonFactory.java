package com.yxx.common.core.utils;

import com.yxx.common.utils.spring.SpringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 线程安全的单例对象工厂，支持两种创建方式：
 * 1. 从Spring容器获取Bean
 * 2. 使用自定义的创建逻辑（非Spring管理的对象）
 */
public class SingletonFactory {

    // 保存已创建的单例实例，Key为类对象
    private static final Map<Class<?>, Object> INSTANCES = new ConcurrentHashMap<>();

    // 为每个类提供独立的锁对象，保证细粒度锁控制
    private static final Map<Class<?>, Object> LOCKS = new ConcurrentHashMap<>();

    /**
     * 获取Spring管理的单例Bean（要求目标类必须已被Spring管理）
     *
     * @param clazz 目标类类型
     * @return 单例实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getSingleton(Class<T> clazz) {
        T instance = clazz.cast(INSTANCES.get(clazz));
        if (instance == null) {
            // 类级别的锁对象，避免不同类之间的锁竞争
            final Object lock = LOCKS.computeIfAbsent(clazz, k -> new Object());
            synchronized (lock) {
                instance = clazz.cast(INSTANCES.get(clazz));
                if (instance == null) {
                    // 从Spring容器获取Bean（要求必须存在对应Bean）
                    instance = SpringUtils.getBean(clazz);
                    INSTANCES.put(clazz, instance);
                }
            }
        }
        return instance;
    }

    /**
     * 获取自定义创建的单例对象（适用于非Spring管理的对象）
     *
     * @param clazz   目标类类型
     * @param creator 自定义对象创建器
     * @return 单例实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getSingleton(Class<T> clazz, Supplier<T> creator) {
        T instance = clazz.cast(INSTANCES.get(clazz));
        if (instance == null) {
            final Object lock = LOCKS.computeIfAbsent(clazz, k -> new Object());
            synchronized (lock) {
                instance = clazz.cast(INSTANCES.get(clazz));
                if (instance == null) {
                    // 使用自定义逻辑创建对象实例
                    instance = creator.get();
                    INSTANCES.put(clazz, instance);
                }
            }
        }
        return instance;
    }
}