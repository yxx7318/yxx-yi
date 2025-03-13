package com.yxx.common.yxx.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.google.common.collect.ImmutableMap;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

/**
 * 集合工具类
 */
public class CollectionUtils {

    public static <T> T getFirst(List<T> from) {
        return !CollectionUtil.isEmpty(from) ? from.get(0) : null;
    }

    public static <T> T findFirst(Collection<T> from, Predicate<T> predicate) {
        return findFirst(from, predicate, Function.identity());
    }

    public static <T, U> U findFirst(Collection<T> from, Predicate<T> predicate, Function<T, U> func) {
        if (CollUtil.isEmpty(from)) {
            return null;
        }
        return from.stream().filter(predicate).findFirst().map(func).orElse(null);
    }

    public static <T, V extends Comparable<? super V>> V getMaxValue(Collection<T> from, Function<T, V> valueFunc) {
        if (CollUtil.isEmpty(from)) {
            return null;
        }
        assert !from.isEmpty(); // 断言，避免告警
        T t = from.stream().max(Comparator.comparing(valueFunc)).get();
        return valueFunc.apply(t);
    }

    public static <T, V extends Comparable<? super V>> V getMinValue(List<T> from, Function<T, V> valueFunc) {
        if (CollUtil.isEmpty(from)) {
            return null;
        }
        assert from.size() > 0; // 断言，避免告警
        T t = from.stream().min(Comparator.comparing(valueFunc)).get();
        return valueFunc.apply(t);
    }

    public static <T, V extends Comparable<? super V>> T getMinObject(List<T> from, Function<T, V> valueFunc) {
        if (CollUtil.isEmpty(from)) {
            return null;
        }
        assert from.size() > 0; // 断言，避免告警
        return from.stream().min(Comparator.comparing(valueFunc)).get();
    }

    public static <T, V extends Comparable<? super V>> V getSumValue(Collection<T> from, Function<T, V> valueFunc,
                                                                     BinaryOperator<V> accumulator) {
        return getSumValue(from, valueFunc, accumulator, null);
    }

    public static <T, V extends Comparable<? super V>> V getSumValue(Collection<T> from, Function<T, V> valueFunc,
                                                                     BinaryOperator<V> accumulator, V defaultValue) {
        if (CollUtil.isEmpty(from)) {
            return defaultValue;
        }
        assert !from.isEmpty(); // 断言，避免告警
        return from.stream().map(valueFunc).filter(Objects::nonNull).reduce(accumulator).orElse(defaultValue);
    }

    public static <T> void addIfNotNull(Collection<T> coll, T item) {
        if (item == null) {
            return;
        }
        coll.add(item);
    }

    public static <T> Collection<T> singleton(T obj) {
        return obj == null ? Collections.emptyList() : Collections.singleton(obj);
    }

    public static <T> List<T> newArrayList(List<List<T>> list) {
        return list.stream().filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toList());
    }

    /**
     * 检查source是否存在于targets中。
     *
     * @param source  需要检查的对象
     * @param targets 可能包含source的目标对象数组
     * @return 如果source在targets中则返回true，否则返回false
     */
    public static boolean containsAny(Object source, Object... targets) {
        return asList(targets).contains(source);
    }

    /**
     * 检查source集合是否包含candidates集合中的任意一个元素。
     *
     * @param source     需要检查的目标集合，用于查找是否存在某些候选元素
     * @param candidates 候选元素集合，这些元素将被检查是否存在于目标集合中
     * @return 如果source集合包含candidates集合中的至少一个元素，则返回true；否则返回false
     */
    public static boolean containsAny(Collection<?> source, Collection<?> candidates) {
        // 使用Spring框架的CollectionUtils工具类来执行具体的操作
        return org.springframework.util.CollectionUtils.containsAny(source, candidates);
    }

    /**
     * 检查提供的集合列表中是否有任何一个为空集合。
     *
     * @param collections 一个可变长参数，可以传入多个集合
     * @return 如果任意一个集合为空或null，则返回true；否则返回false
     */
    public static boolean isAnyEmpty(Collection<?>... collections) {
        return Arrays.stream(collections).anyMatch(CollectionUtil::isEmpty);
    }

    /**
     * 判断from集合中是否存在满足predicate条件的元素。
     *
     * @param from      集合
     * @param predicate 用于测试集合中元素的谓词
     * @param <T>       泛型参数
     * @return 如果存在至少一个元素满足条件，则返回true；否则返回false
     */
    public static <T> boolean anyMatch(Collection<T> from, Predicate<T> predicate) {
        return from.stream().anyMatch(predicate);
    }

    /**
     * 过滤from集合中满足predicate条件的所有元素，并返回一个新列表。
     *
     * @param from      原始集合
     * @param predicate 用于过滤集合中元素的谓词
     * @param <T>       泛型参数
     * @return 包含所有满足条件的元素的新列表
     */
    public static <T> List<T> filterList(Collection<T> from, Predicate<T> predicate) {
        if (CollUtil.isEmpty(from)) {
            return new ArrayList<>();
        }
        return from.stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * 根据keyMapper函数为集合中的元素生成键值，去除重复元素后返回一个列表。
     * 如果有重复键值，使用cover函数决定保留哪个元素。
     *
     * @param from      原始集合
     * @param keyMapper 生成键值的函数
     * @param <T>       元素类型
     * @param <R>       键值类型
     * @return 去重后的列表
     */
    public static <T, R> List<T> distinct(Collection<T> from, Function<T, R> keyMapper) {
        if (CollUtil.isEmpty(from)) {
            return new ArrayList<>();
        }
        return distinct(from, keyMapper, (t1, t2) -> t1);
    }

    /**
     * 根据keyMapper函数为集合中的元素生成键值，去除重复元素后返回一个列表。
     * 如果有重复键值，使用cover函数决定保留哪个元素。
     * <p>
     * // 使用distinct方法去重，对于名字相同的Person，保留ID较大的那个
     * distinct(peopleList,Person::getName, (p1, p2) -> p1.getId() > p2.getId() ? p1 : p2);
     *
     * @param from      原始集合
     * @param keyMapper 生成键值的函数
     * @param cover     当遇到相同键值时，决定保留哪个元素的函数
     * @param <T>       元素类型
     * @param <R>       键值类型
     * @return 去重后的列表
     */
    public static <T, R> List<T> distinct(Collection<T> from, Function<T, R> keyMapper, BinaryOperator<T> cover) {
        if (CollUtil.isEmpty(from)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(convertMap(from, keyMapper, Function.identity(), cover).values());
    }

    /**
     * 将数组from中的每个元素通过func转换成新的类型U，并返回一个新的列表。
     *
     * @param from 原始数组
     * @param func 转换函数
     * @param <T>  原始类型
     * @param <U>  目标类型
     * @return 转换后的列表
     */
    public static <T, U> List<U> convertList(T[] from, Function<T, U> func) {
        if (ArrayUtil.isEmpty(from)) {
            return new ArrayList<>();
        }
        return convertList(Arrays.asList(from), func);
    }

    /**
     * 将集合中的每个元素通过func转换成新的类型U，并返回一个新的列表。
     * 该方法会过滤掉所有转换结果为null的元素。
     *
     * @param from 原始集合
     * @param func 转换函数，用于将T类型的元素转换为U类型
     * @param <T>  原始元素类型
     * @param <U>  目标元素类型
     * @return 转换后的列表，不包含任何null值
     */
    public static <T, U> List<U> convertList(Collection<T> from, Function<T, U> func) {
        if (CollUtil.isEmpty(from)) {
            return new ArrayList<>();
        }
        return from.stream().map(func).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 将集合中的每个元素首先通过filter过滤，然后通过func转换成新的类型U，并返回一个新的列表。
     * 该方法会过滤掉所有转换结果为null的元素以及不符合filter条件的原始元素。
     *
     * @param from   原始集合
     * @param func   转换函数，用于将T类型的元素转换为U类型
     * @param filter 过滤器，用于决定哪些元素应该被转换
     * @param <T>    原始元素类型
     * @param <U>    目标元素类型
     * @return 转换后的列表，不包含任何null值
     */
    public static <T, U> List<U> convertList(Collection<T> from, Function<T, U> func, Predicate<T> filter) {
        if (CollUtil.isEmpty(from)) {
            return new ArrayList<>();
        }
        return from.stream().filter(filter).map(func).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 将集合中的每个元素通过func转换成流，然后将这些流合并成一个列表。
     * 该方法适合于需要将每个元素映射到多个目标元素的情况。它会过滤掉所有的null值。
     * <p>
     * // 使用convertListByFlatMap方法从订单中映射的getProductIds合并后提取出所有商品ID
     * convertListByFlatMap(orders, Order::getProductIds, productIds -> productIds.stream());
     *
     * @param from 原始集合
     * @param func 转换函数，用于将T类型的元素转换为Stream<? extends U>
     * @param <T>  原始元素类型
     * @param <U>  目标元素类型
     * @return 合并后的列表，不包含任何null值
     */
    public static <T, U> List<U> convertListByFlatMap(Collection<T> from,
                                                      Function<T, ? extends Stream<? extends U>> func) {
        if (CollUtil.isEmpty(from)) {
            return new ArrayList<>();
        }
        return from.stream().filter(Objects::nonNull).flatMap(func).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 首先使用mapper对集合中的每个元素进行一次映射，然后通过func将其转换成流，
     * 最后将这些流合并成一个列表。该方法适用于复杂的转换场景。
     * 它同样会过滤掉所有的null值。
     *
     * @param from   原始集合
     * @param mapper 第一次映射使用的函数
     * @param func   第二次映射使用的函数，用于生成流
     * @param <T>    原始元素类型
     * @param <U>    中间元素类型
     * @param <R>    目标元素类型
     * @return 合并后的列表，不包含任何null值
     */
    public static <T, U, R> List<R> convertListByFlatMap(Collection<T> from,
                                                         Function<? super T, ? extends U> mapper,
                                                         Function<U, ? extends Stream<? extends R>> func) {
        if (CollUtil.isEmpty(from)) {
            return new ArrayList<>();
        }
        return from.stream().map(mapper).filter(Objects::nonNull).flatMap(func).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 将给定Map<K, List<V>>的所有值（即List<V>）合并到一个单一的列表中。
     * 这个方法特别适用于需要将一个键值对集合的所有值组合起来处理的场景。
     *
     * @param map 包含键值对的映射，其中值是列表
     * @param <K> 键的类型
     * @param <V> 值的类型
     * @return 合并后的列表
     */
    public static <K, V> List<V> mergeValuesFromMap(Map<K, List<V>> map) {
        return map.values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * 将集合中的元素转换为一个新的Set，使用默认的转换函数（即不做转换）。
     * 该方法会过滤掉所有转换结果为null的元素，并确保返回集合中没有重复元素。
     *
     * @param from 原始集合
     * @param <T>  集合元素类型
     * @return 转换后的Set集合，不包含任何null值
     */
    public static <T> Set<T> convertSet(Collection<T> from) {
        return convertSet(from, v -> v);
    }

    /**
     * 将集合中的每个元素通过func转换成新的类型U，并返回一个新的Set集合。
     * 该方法会过滤掉所有转换结果为null的元素，并确保返回集合中没有重复元素。
     *
     * @param from 原始集合
     * @param func 转换函数，用于将T类型的元素转换为U类型
     * @param <T>  原始元素类型
     * @param <U>  目标元素类型
     * @return 转换后的Set集合，不包含任何null值
     */
    public static <T, U> Set<U> convertSet(Collection<T> from, Function<T, U> func) {
        if (CollUtil.isEmpty(from)) {
            return new HashSet<>();
        }
        return from.stream().map(func).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    /**
     * 将集合中的每个元素首先通过filter过滤，然后通过func转换成新的类型U，并返回一个新的Set集合。
     * 该方法会过滤掉所有转换结果为null的元素以及不符合filter条件的原始元素，并确保返回集合中没有重复元素。
     *
     * @param from   原始集合
     * @param func   转换函数，用于将T类型的元素转换为U类型
     * @param filter 过滤器，用于决定哪些元素应该被转换
     * @param <T>    原始元素类型
     * @param <U>    目标元素类型
     * @return 转换后的Set集合，不包含任何null值
     */
    public static <T, U> Set<U> convertSet(Collection<T> from, Function<T, U> func, Predicate<T> filter) {
        if (CollUtil.isEmpty(from)) {
            return new HashSet<>();
        }
        return from.stream().filter(filter).map(func).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    /**
     * 将集合中的元素通过filter过滤后，使用keyFunc生成键值对，并返回一个Map。
     * 该方法会将满足条件的每个元素映射为其键值，值为元素本身。
     *
     * @param from    原始集合
     * @param filter  过滤器，用于筛选符合条件的元素
     * @param keyFunc 键生成函数，用于从T类型元素生成K类型的键
     * @param <T>     集合元素类型
     * @param <K>     键的类型
     * @return 转换后的Map
     */
    public static <T, K> Map<K, T> convertMapByFilter(Collection<T> from, Predicate<T> filter, Function<T, K> keyFunc) {
        if (CollUtil.isEmpty(from)) {
            return new HashMap<>();
        }
        return from.stream().filter(filter).collect(Collectors.toMap(keyFunc, v -> v));
    }

    /**
     * 将集合中的每个元素通过func转换成流，然后将这些流合并成一个Set集合。
     * 适用于需要将每个元素映射到多个目标元素的情况。它会过滤掉所有的null值。
     *
     * @param from 原始集合
     * @param func 转换函数，用于将T类型的元素转换为Stream<? extends U>
     * @param <T>  原始元素类型
     * @param <U>  目标元素类型
     * @return 合并后的Set集合，不包含任何null值
     */
    public static <T, U> Set<U> convertSetByFlatMap(Collection<T> from,
                                                    Function<T, ? extends Stream<? extends U>> func) {
        if (CollUtil.isEmpty(from)) {
            return new HashSet<>();
        }
        return from.stream().filter(Objects::nonNull).flatMap(func).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    /**
     * 首先使用mapper对集合中的每个元素进行一次映射，然后通过func将其转换成流，
     * 最后将这些流合并成一个Set集合。适用于复杂的转换场景。
     * 它同样会过滤掉所有的null值。
     *
     * @param from   原始集合
     * @param mapper 第一次映射使用的函数
     * @param func   第二次映射使用的函数，用于生成流
     * @param <T>    原始元素类型
     * @param <U>    中间元素类型
     * @param <R>    目标元素类型
     * @return 合并后的Set集合，不包含任何null值
     */
    public static <T, U, R> Set<R> convertSetByFlatMap(Collection<T> from,
                                                       Function<? super T, ? extends U> mapper,
                                                       Function<U, ? extends Stream<? extends R>> func) {
        if (CollUtil.isEmpty(from)) {
            return new HashSet<>();
        }
        return from.stream().map(mapper).filter(Objects::nonNull).flatMap(func).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    /**
     * 将集合中的每个元素通过keyFunc生成键值对，并返回一个Map。
     * 该方法会将每个元素映射为其键值，值为元素本身。
     *
     * @param from    原始集合
     * @param keyFunc 键生成函数，用于从T类型元素生成K类型的键
     * @param <T>     集合元素类型
     * @param <K>     键的类型
     * @return 转换后的Map
     */
    public static <T, K> Map<K, T> convertMap(Collection<T> from, Function<T, K> keyFunc) {
        if (CollUtil.isEmpty(from)) {
            return new HashMap<>();
        }
        return convertMap(from, keyFunc, Function.identity());
    }

    /**
     * 将集合中的每个元素通过keyFunc生成键值对，并返回一个Map。
     * 该方法会将每个元素映射为其键值，值为元素本身。允许用户自定义转换逻辑和冲突处理方式。
     *
     * @param from      原始集合
     * @param keyFunc   键生成函数，用于从T类型元素生成K类型的键
     * @param valueFunc 值生成函数，用于从T类型元素生成V类型的值
     * @param <T>       集合元素类型
     * @param <K>       键的类型
     * @param <V>       值的类型
     * @return 转换后的Map，默认情况下使用(v1, v2) -> v1作为冲突处理策略
     */
    public static <T, K, V> Map<K, V> convertMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        if (CollUtil.isEmpty(from)) {
            return new HashMap<>();
        }
        return convertMap(from, keyFunc, valueFunc, (v1, v2) -> v1);
    }

    /**
     * 将集合中的每个元素通过keyFunc生成键值，并使用valueFunc生成对应的值，然后返回一个Map。
     * 可以指定Map的具体实现类通过supplier提供，默认策略为保留第一个出现的值（v1），而忽略后续出现的值（v2）。
     *
     * @param from      原始集合
     * @param keyFunc   键生成函数，用于从T类型元素生成K类型的键
     * @param valueFunc 值生成函数，用于从T类型元素生成V类型的值
     * @param supplier  提供Map的具体实现类的工厂
     * @param <T>       集合元素类型
     * @param <K>       键的类型
     * @param <V>       值的类型
     * @return 转换后的Map
     */
    public static <T, K, V> Map<K, V> convertMapDiscard(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc, Supplier<? extends Map<K, V>> supplier) {
        if (CollUtil.isEmpty(from)) {
            return supplier.get();
        }
        return convertMap(from, keyFunc, valueFunc, (v1, v2) -> v1, supplier);
    }

    /**
     * 将集合中的每个元素通过keyFunc生成键值，并使用valueFunc生成对应的值，然后返回一个Map。
     * 默认返回HashMap，默认合并策略为合并为列表。
     *
     * @param from      原始集合
     * @param keyFunc   键生成函数，用于从T类型元素生成K类型的键
     * @param valueFunc 值生成函数，用于从T类型元素生成V类型的值
     * @param <T>       集合元素类型
     * @param <K>       键的类型
     * @param <V>       值的类型
     * @return 转换后的Map
     */

    public static <T, K, V> Map<K, Collection<V>> convertMapMerge(Collection<T> from, Function<T, K> keyFunc, Function<T, Collection<V>> valueFunc) {
        return convertMap(from, keyFunc, valueFunc, CollUtil::union, HashMap::new);
    }

    /**
     * 将集合中的每个元素通过keyFunc生成键值，并使用valueFunc生成对应的值，然后返回一个Map。
     * 可以指定Map的具体实现类通过supplier提供，默认合并策略为合并为列表。
     * <p>
     * // 定义键生成函数：使用年龄作为键
     * Function<Person, Integer> keyFunc = Person::getAge;
     * <p>
     * // 定义值生成函数：使用名字作为初始值
     * Function<Person, List<String>> valueFunc = person -> new ArrayList<>(Collections.singletonList(person.getName()));
     * <p>
     * // 定义Map的具体实现类供应商（这里使用HashMap）
     * Supplier<HashMap<Integer, List<String>>> supplier = HashMap::new;
     * <p>
     * // 调用convertMap方法
     * Map<Integer, List<String>> ageToNamesMap = convertMap(people, keyFunc, valueFunc, supplier);
     *
     * @param from      原始集合
     * @param keyFunc   键生成函数，用于从T类型元素生成K类型的键
     * @param valueFunc 值生成函数，用于从T类型元素生成V类型的值
     * @param supplier  提供Map的具体实现类的工厂
     * @param <T>       集合元素类型
     * @param <K>       键的类型
     * @param <V>       值的类型
     * @return 转换后的Map
     */
    public static <T, K, V> Map<K, Collection<V>> convertMapMerge(Collection<T> from, Function<T, K> keyFunc, Function<T, Collection<V>> valueFunc, Supplier<? extends Map<K, Collection<V>>> supplier) {
        if (CollUtil.isEmpty(from)) {
            return supplier.get();
        }
        return convertMap(from, keyFunc, valueFunc, CollUtil::union, supplier);
    }

    /**
     * 将集合中的每个元素通过keyFunc生成键值，并使用valueFunc生成对应的值，
     * 当出现重复键时，使用mergeFunction来决定如何合并值，最后返回一个Map。
     *
     * @param from          原始集合
     * @param keyFunc       键生成函数，用于从T类型元素生成K类型的键
     * @param valueFunc     值生成函数，用于从T类型元素生成V类型的值
     * @param mergeFunction 键冲突解决函数，当遇到重复键时决定如何合并对应的值
     * @param <T>           集合元素类型
     * @param <K>           键的类型
     * @param <V>           值的类型
     * @return 转换后的Map
     */
    public static <T, K, V> Map<K, V> convertMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc, BinaryOperator<V> mergeFunction) {
        if (CollUtil.isEmpty(from)) {
            return new HashMap<>();
        }
        return convertMap(from, keyFunc, valueFunc, mergeFunction, HashMap::new);
    }

    /**
     * 将集合中的每个元素通过keyFunc生成键值，并使用valueFunc生成对应的值，
     * 使用mergeFunction处理键冲突，同时可以指定Map的具体实现类通过supplier提供。
     * <p>
     * // 定义键生成函数：使用年龄作为键
     * Function<Person, Integer> keyFunc = Person::getAge;
     * <p>
     * // 定义值生成函数：使用名字作为初始值
     * Function<Person, String> valueFunc = Person::getName;
     * <p>
     * // 定义键冲突解决函数：保留后续的，丢弃前面的
     * BinaryOperator<String> mergeFunction = (v1, v2) -> v2;
     * <p>
     * // 定义Map的具体实现类供应商（这里使用HashMap）
     * Supplier<HashMap<Integer, String>> supplier = HashMap::new;
     * <p>
     * // 调用convertMap方法
     * Map<Integer, String> ageToNamesMap = convertMap(people, keyFunc, valueFunc, mergeFunction, supplier);
     *
     * @param from          原始集合
     * @param keyFunc       键生成函数，用于从T类型元素生成K类型的键
     * @param valueFunc     值生成函数，用于从T类型元素生成V类型的值
     * @param mergeFunction 键冲突解决函数，当遇到重复键时决定如何合并对应的值
     * @param supplier      提供Map的具体实现类的工厂
     * @param <T>           集合元素类型
     * @param <K>           键的类型
     * @param <V>           值的类型
     * @return 转换后的Map
     */
    public static <T, K, V> Map<K, V> convertMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc, BinaryOperator<V> mergeFunction, Supplier<? extends Map<K, V>> supplier) {
        if (CollUtil.isEmpty(from)) {
            return supplier.get();
        }
        return from.stream().collect(Collectors.toMap(keyFunc, valueFunc, mergeFunction, supplier));
    }

    /**
     * 将集合中的每个元素通过keyFunc生成键，并将这些元素分组到一个Map<K, List<T>>中。
     * 每个键对应一个列表，列表中包含所有映射到该键的原始元素。
     *
     * @param from    原始集合
     * @param keyFunc 键生成函数，用于从T类型元素生成K类型的键
     * @param <T>     集合元素类型
     * @param <K>     键的类型
     * @return 转换后的Map，其中每个键关联着一个由原始元素组成的列表
     */
    public static <T, K> Map<K, List<T>> convertMultiMap(Collection<T> from, Function<T, K> keyFunc) {
        if (CollUtil.isEmpty(from)) {
            return new HashMap<>();
        }
        return from.stream().collect(Collectors.groupingBy(keyFunc, Collectors.mapping(t -> t, Collectors.toList())));
    }

    /**
     * 将集合中的每个元素通过keyFunc生成键，并使用valueFunc生成对应的值，
     * 最终形成一个Map<K, List<V>>，即每个键关联着一个由特定值组成的列表。
     * 这种方法允许对原始集合中的元素进行转换后再分组。
     * <p>
     * // 定义键生成函数：使用年龄作为键
     * Function<Person, Integer> keyFunc = Person::getAge;
     * <p>
     * // 定义值生成函数：使用名字作为值
     * Function<Person, String> valueFunc = Person::getName;
     * <p>
     * // 调用convertMultiMap方法
     * Map<Integer, List<String>> ageToNamesMap = convertMultiMap(people, keyFunc, valueFunc);
     *
     * @param from      原始集合
     * @param keyFunc   键生成函数，用于从T类型元素生成K类型的键
     * @param valueFunc 值生成函数，用于从T类型元素生成V类型的值
     * @param <T>       集合元素类型
     * @param <K>       键的类型
     * @param <V>       值的类型
     * @return 转换后的Map，其中每个键关联着一个由转换后值组成的列表
     */
    public static <T, K, V> Map<K, List<V>> convertMultiMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        if (CollUtil.isEmpty(from)) {
            return new HashMap<>();
        }
        return from.stream()
                .collect(Collectors.groupingBy(keyFunc, Collectors.mapping(valueFunc, Collectors.toList())));
    }

    /**
     * 将集合中的每个元素通过keyFunc生成键，并使用valueFunc生成对应的值，
     * 最终形成一个Map<K, Set<V>>，即每个键关联着一个由特定值组成的集合（Set）。
     * 使用Set可以确保每个键对应的值集不会有重复。
     *
     * @param from      原始集合
     * @param keyFunc   键生成函数，用于从T类型元素生成K类型的键
     * @param valueFunc 值生成函数，用于从T类型元素生成V类型的值
     * @param <T>       集合元素类型
     * @param <K>       键的类型
     * @param <V>       值的类型
     * @return 转换后的Map，键映射到一个没有重复值的集合
     */
    public static <T, K, V> Map<K, Set<V>> convertMultiMapToSet(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        if (CollUtil.isEmpty(from)) {
            return new HashMap<>();
        }
        return from.stream().collect(Collectors.groupingBy(keyFunc, Collectors.mapping(valueFunc, Collectors.toSet())));
    }

    /**
     * 将集合中的每个元素通过keyFunc生成键值对，并返回一个不可变的Map<K, T>。
     * 该方法会将每个元素映射为其键值，值为元素本身。使用Google Guava库中的ImmutableMap来确保返回的Map是不可变的。
     * <p>
     * 注意：如果存在重复键的情况，后出现的值会覆盖之前出现的值。
     *
     * @param from    原始集合
     * @param keyFunc 键生成函数，用于从T类型元素生成K类型的键
     * @param <T>     集合元素类型
     * @param <K>     键的类型
     * @return 转换后的不可变Map，其中每个键关联着原始元素
     */
    public static <T, K> Map<K, T> convertImmutableMap(Collection<T> from, Function<T, K> keyFunc) {
        // 如果输入集合为空，则直接返回空的不可变Map
        if (CollUtil.isEmpty(from)) {
            return Collections.emptyMap();
        }
        // 使用Guava的ImmutableMap.Builder来构建不可变Map
        ImmutableMap.Builder<K, T> builder = ImmutableMap.builder();
        // 遍历输入集合中的每一个元素，应用键生成函数并将其添加到builder中
        from.forEach(item -> builder.put(keyFunc.apply(item), item));
        // 构建并返回不可变Map
        return builder.build();
    }

    /**
     * 对比老、新两个列表，找出新增、修改、删除的数据
     *
     * @param oldList  老列表
     * @param newList  新列表
     * @param sameFunc 对比函数，返回 true 表示相同，返回 false 表示不同
     *                 注意，same 是通过每个元素的“标识”，判断它们是不是同一个数据
     * @return [新增列表、修改列表、删除列表]
     */
    public static <T> List<List<T>> diffList(Collection<T> oldList, Collection<T> newList,
                                             BiFunction<T, T, Boolean> sameFunc) {
        List<T> createList = new LinkedList<>(newList); // 默认都认为是新增的，后续会进行移除
        List<T> updateList = new ArrayList<>();
        List<T> deleteList = new ArrayList<>();

        // 通过以 oldList 为主遍历，找出 updateList 和 deleteList
        for (T oldObj : oldList) {
            // 1. 寻找是否有匹配的
            T foundObj = null;
            for (Iterator<T> iterator = createList.iterator(); iterator.hasNext(); ) {
                T newObj = iterator.next();
                // 1.1 不匹配，则直接跳过
                if (!sameFunc.apply(oldObj, newObj)) {
                    continue;
                }
                // 1.2 匹配，则移除，并结束寻找
                iterator.remove();
                foundObj = newObj;
                break;
            }
            // 2. 匹配添加到 updateList；不匹配则添加到 deleteList 中
            if (foundObj != null) {
                updateList.add(foundObj);
            } else {
                deleteList.add(oldObj);
            }
        }
        return asList(createList, updateList, deleteList);
    }

    /**
     * 对于给定的列表和多个条件，统计每个条件对应的满足条件的对象数量。
     * <p>
     * // 定义多个条件
     * Predicate<Person> nameLongerThan4 = person -> person.getName().length() > 4;
     * Predicate<Person> ageGreaterThan30 = person -> person.getAge() > 30;
     * <p>
     * // 应用方法
     * List<Long> results = countForEachCondition(people, nameLongerThan4, ageGreaterThan30);
     *
     * @param list       要检查的列表
     * @param conditions 用于判断对象是否满足条件的谓词数组
     * @param <T>        列表元素类型
     * @return 每个条件对应的满足条件的对象数量组成的列表，不包含任何null值
     */
    @SafeVarargs
    public static <T> List<Long> countForEachCondition(List<T> list, Predicate<T>... conditions) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<Long> counts = new ArrayList<>();
        for (Predicate<T> condition : conditions) {
            long count = 0;
            if (!ObjectUtils.isEmpty(condition)) {
                count = list.stream().filter(Objects::nonNull).filter(condition).count();
            }
            counts.add(count);
        }
        return counts;
    }
}
