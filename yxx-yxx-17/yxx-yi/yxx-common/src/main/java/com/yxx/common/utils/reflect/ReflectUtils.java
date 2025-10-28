package com.yxx.common.utils.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.yxx.common.utils.LocalDateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yxx.common.core.text.Convert;
import com.yxx.common.utils.DateUtils;

/**
 * 反射工具类. 提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class, 被AOP过的真实类等工具函数.
 */
@SuppressWarnings("rawtypes")
public class ReflectUtils
{
    private static final String SETTER_PREFIX = "set";

    private static final String GETTER_PREFIX = "get";

    private static final String IS_PREFIX = "is";

    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    private static Logger logger = LoggerFactory.getLogger(ReflectUtils.class);

    /**
     * 获取Java类的所有属性Field数组（不包含父类）
     * 只返回当前类声明的字段，包括private/protected/default字段
     */
    public static Field[] getDeclaredFields(final Class<?> clazz, final boolean accessible)
    {
        if (clazz == null)
        {
            return new Field[0];
        }

        Field[] fields = clazz.getDeclaredFields();
        if (accessible)
        {
            // 设置所有字段为可访问
            for (Field field : fields)
            {
                makeAccessible(field);
            }
        }
        return fields;
    }

    /**
     * 获取Java类的所有属性Field数组（不包含父类）
     * 只返回当前类声明的字段，包括private/protected/default字段
     */
    public static Field[] getDeclaredFields(final Class<?> clazz)
    {
        return getDeclaredFields(clazz, false);
    }

    /**
     * 获取Java类的所有属性Field数组（不包含父类）
     * 基于对象实例获取字段
     */
    public static Field[] getDeclaredFields(final Object obj, final boolean accessible)
    {
        if (obj == null)
        {
            return new Field[0];
        }
        return getDeclaredFields(obj.getClass(), accessible);
    }

    /**
     * 获取Java类的所有属性Field数组（不包含父类）
     * 基于对象实例获取字段
     */
    public static Field[] getDeclaredFields(final Object obj)
    {
        return getDeclaredFields(obj.getClass(), false);
    }

    /**
     * 获取Java类的所有属性Field数组（包含父类）
     * 返回当前类及其所有父类的字段（直到Object类）
     */
    public static Field[] getFields(final Class<?> clazz, final boolean accessible)
    {
        if (clazz == null)
        {
            return new Field[0];
        }

        List<Field> fieldList = new ArrayList<>();
        Class<?> currentClass = clazz;

        // 循环向上转型，收集所有字段
        while (currentClass != null && currentClass != Object.class)
        {
            Field[] declaredFields = currentClass.getDeclaredFields();
            for (Field field : declaredFields)
            {
                if (accessible)
                {
                    makeAccessible(field);
                }
                fieldList.add(field);
            }
            currentClass = currentClass.getSuperclass();
        }

        return fieldList.toArray(new Field[0]);
    }

    /**
     * 获取Java类的所有属性Field数组（包含父类）
     * 返回当前类及其所有父类的字段（直到Object类）
     */
    public static Field[] getFields(final Class<?> clazz)
    {
        return getFields(clazz, false);
    }

    /**
     * 获取Java类的所有属性Field数组（包含父类）
     * 基于对象实例获取字段
     */
    public static Field[] getFields(final Object obj)
    {
        if (obj == null)
        {
            return new Field[0];
        }
        return getFields(obj.getClass());
    }

    /**
     * 获取指定名称的字段（包含父类查找）
     * 这是对现有getAccessibleField方法的补充，返回Field对象而不是值
     */
    public static Field getField(final Class<?> clazz, final String fieldName, final boolean accessible)
    {
        if (clazz == null || StringUtils.isBlank(fieldName))
        {
            return null;
        }

        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class)
        {
            try
            {
                Field field = currentClass.getDeclaredField(fieldName);
                if (accessible)
                {
                    makeAccessible(field);
                }
                return field;
            }
            catch (NoSuchFieldException e)
            {
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 获取指定名称的字段（包含父类查找）
     * 这是对现有getAccessibleField方法的补充，返回Field对象而不是值
     */
    public static Field getField(final Class<?> clazz, final String fieldName)
    {
        return getField(clazz, fieldName, false);
    }

    /**
     * 获取指定名称的字段（包含父类查找）
     * 基于对象实例查找字段
     */
    public static Field getField(final Object obj, final String fieldName)
    {
        if (obj == null)
        {
            return null;
        }
        return getField(obj.getClass(), fieldName);
    }

    /**
     * 过滤字段数组，排除静态字段
     */
    public static Field[] excludeStaticFields(Field[] fields)
    {
        if (fields == null || fields.length == 0)
        {
            return new Field[0];
        }

        List<Field> result = new ArrayList<>();
        for (Field field : fields)
        {
            if (!Modifier.isStatic(field.getModifiers()))
            {
                result.add(field);
            }
        }
        return result.toArray(new Field[0]);
    }

    /**
     * 过滤字段数组，只包含静态字段
     */
    public static Field[] getStaticFields(Field[] fields)
    {
        if (fields == null || fields.length == 0)
        {
            return new Field[0];
        }

        List<Field> result = new ArrayList<>();
        for (Field field : fields)
        {
            if (Modifier.isStatic(field.getModifiers()))
            {
                result.add(field);
            }
        }
        return result.toArray(new Field[0]);
    }

    /**
     * 获取字段上使用的所有注解对象数组
     *
     * @param field 字段对象
     * @return 注解对象数组，如果字段为null则返回空数组
     */
    public static Annotation[] getFieldAnnotations(Field field)
    {
        if (field == null)
        {
            return new Annotation[0];
        }

        // 确保字段可访问
        makeAccessible(field);
        return field.getAnnotations();
    }

    /**
     * 获取字段上指定的注解对象
     *
     * @param field 字段对象
     * @param annotationClass 注解类型
     * @return 注解对象，如果不存在则返回null
     */
    public static <A extends Annotation> A getFieldAnnotation(Field field, Class<A> annotationClass)
    {
        if (field == null || annotationClass == null)
        {
            return null;
        }

        // 确保字段可访问
        makeAccessible(field);
        return field.getAnnotation(annotationClass);
    }

    /**
     * 判断字段是否被指定注解标记
     *
     * @param field 字段对象
     * @param annotationClass 注解类型
     * @return 如果字段被该注解标记则返回true
     */
    public static boolean isFieldAnnotatedWith(Field field, Class<? extends Annotation> annotationClass)
    {
        if (field == null || annotationClass == null)
        {
            return false;
        }

        // 确保字段可访问
        makeAccessible(field);
        return field.isAnnotationPresent(annotationClass);
    }

    /**
     * 获取字段上所有指定类型的注解（包括重复注解）
     *
     * @param field 字段对象
     * @param annotationClass 注解类型
     * @return 注解对象数组
     */
    public static <A extends Annotation> A[] getFieldAnnotationsByType(Field field, Class<A> annotationClass)
    {
        if (field == null || annotationClass == null) {
            return null;
        }

        // 确保字段可访问
        makeAccessible(field);
        return field.getAnnotationsByType(annotationClass);
    }

    /**
     * 获取类的指定字段的所有注解
     *
     * @param clazz 类对象
     * @param fieldName 字段名
     * @return 注解对象数组，如果字段不存在则返回空数组
     */
    public static Annotation[] getFieldAnnotations(Class<?> clazz, String fieldName)
    {
        Field field = getAccessibleField(clazz, fieldName);
        return getFieldAnnotations(field);
    }

    /**
     * 获取对象的指定字段的所有注解
     *
     * @param obj 对象实例
     * @param fieldName 字段名
     * @return 注解对象数组，如果字段不存在则返回空数组
     */
    public static Annotation[] getFieldAnnotations(Object obj, String fieldName)
    {
        Field field = getAccessibleField(obj, fieldName);
        return getFieldAnnotations(field);
    }

    /**
     * 获取类的指定字段的特定注解
     *
     * @param clazz 类对象
     * @param fieldName 字段名
     * @param annotationClass 注解类型
     * @return 注解对象，如果不存在则返回null
     */
    public static <A extends Annotation> A getFieldAnnotation(Class<?> clazz, String fieldName, Class<A> annotationClass)
    {
        Field field = getAccessibleField(clazz, fieldName);
        return getFieldAnnotation(field, annotationClass);
    }

    /**
     * 获取对象的指定字段的特定注解
     *
     * @param obj 对象实例
     * @param fieldName 字段名
     * @param annotationClass 注解类型
     * @return 注解对象，如果不存在则返回null
     */
    public static <A extends Annotation> A getFieldAnnotation(Object obj, String fieldName, Class<A> annotationClass)
    {
        Field field = getAccessibleField(obj, fieldName);
        return getFieldAnnotation(field, annotationClass);
    }

    /**
     * 获取类中所有被指定注解标记的字段
     *
     * @param clazz 类对象
     * @param annotationClass 注解类型
     * @return 被标记的字段列表
     */
    public static List<Field> getFieldsWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass)
    {
        List<Field> annotatedFields = new ArrayList<>();

        if (clazz == null || annotationClass == null)
        {
            return annotatedFields;
        }

        Field[] fields = getFields(clazz);
        for (Field field : fields)
        {
            if (isFieldAnnotatedWith(field, annotationClass))
            {
                annotatedFields.add(field);
            }
        }

        return annotatedFields;
    }

    /**
     * 获取类中所有被指定注解标记的字段（不包含父类）
     *
     * @param clazz 类对象
     * @param annotationClass 注解类型
     * @return 被标记的字段列表
     */
    public static List<Field> getDeclaredFieldsWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass)
    {
        List<Field> annotatedFields = new ArrayList<>();

        if (clazz == null || annotationClass == null)
        {
            return annotatedFields;
        }
        // 确保字段可访问
        Field[] fields = getDeclaredFields(clazz, true);
        for (Field field : fields)
        {
            if (isFieldAnnotatedWith(field, annotationClass))
            {
                annotatedFields.add(field);
            }
        }

        return annotatedFields;
    }

    /**
     * 获取Java类的所有方法Method数组（不包含父类）
     * 只返回当前类声明的方法，包括private/protected/default方法
     */
    public static Method[] getDeclaredMethods(final Class<?> clazz, final boolean accessible)
    {
        if (clazz == null)
        {
            return new Method[0];
        }

        Method[] methods = clazz.getDeclaredMethods();
        if (accessible)
        {
            // 设置所有方法为可访问
            for (Method method : methods)
            {
                makeAccessible(method);
            }
        }
        return methods;
    }

    /**
     * 获取Java类的所有方法Method数组（不包含父类）
     * 只返回当前类声明的方法，包括private/protected/default方法
     */
    public static Method[] getDeclaredMethods(final Class<?> clazz)
    {
        return getDeclaredMethods(clazz, false);
    }

    /**
     * 获取Java类的所有方法Method数组（不包含父类）
     * 基于对象实例获取方法
     */
    public static Method[] getDeclaredMethods(final Object obj, final boolean accessible)
    {
        if (obj == null)
        {
            return new Method[0];
        }
        return getDeclaredMethods(obj.getClass(), accessible);
    }

    /**
     * 获取Java类的所有方法Method数组（不包含父类）
     * 基于对象实例获取方法
     */
    public static Method[] getDeclaredMethods(final Object obj)
    {
        return getDeclaredMethods(obj, false);
    }

    /**
     * 获取Java类的所有方法Method数组（包含父类）
     * 返回当前类及其所有父类的方法（直到Object类），排除Object类方法
     */
    public static Method[] getMethods(final Class<?> clazz, final boolean accessible)
    {
        if (clazz == null)
        {
            return new Method[0];
        }

        List<Method> methodList = new ArrayList<>();
        Class<?> currentClass = clazz;

        // 循环向上转型，收集所有方法
        while (currentClass != null && currentClass != Object.class)
        {
            Method[] declaredMethods = currentClass.getDeclaredMethods();
            for (Method method : declaredMethods)
            {
                // 跳过Object类方法
                if (method.getDeclaringClass() == Object.class)
                {
                    continue;
                }
                if (accessible)
                {
                    makeAccessible(method);
                }
                methodList.add(method);
            }
            currentClass = currentClass.getSuperclass();
        }

        return methodList.toArray(new Method[0]);
    }

    /**
     * 获取Java类的所有方法Method数组（包含父类）
     * 返回当前类及其所有父类的方法（直到Object类），排除Object类方法
     */
    public static Method[] getMethods(final Class<?> clazz)
    {
        return getMethods(clazz, false);
    }

    /**
     * 获取Java类的所有方法Method数组（包含父类）
     * 基于对象实例获取方法
     */
    public static Method[] getMethods(final Object obj, final boolean accessible)
    {
        if (obj == null)
        {
            return new Method[0];
        }
        return getMethods(obj.getClass(), accessible);
    }

    /**
     * 获取Java类的所有方法Method数组（包含父类）
     * 基于对象实例获取方法
     */
    public static Method[] getMethods(final Object obj)
    {
        return getMethods(obj, false);
    }

    /**
     * 根据方法名获取方法数组（包含父类查找）
     * 返回所有同名方法，包括重载方法
     */
    public static Method[] getMethodsByName(final Class<?> clazz, final String methodName, final boolean accessible)
    {
        if (clazz == null || StringUtils.isBlank(methodName))
        {
            return new Method[0];
        }

        List<Method> result = new ArrayList<>();
        Class<?> currentClass = clazz;

        while (currentClass != null && currentClass != Object.class)
        {
            Method[] methods = currentClass.getDeclaredMethods();
            for (Method method : methods)
            {
                if (method.getName().equals(methodName))
                {
                    if (accessible)
                    {
                        makeAccessible(method);
                    }
                    result.add(method);
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        return result.toArray(new Method[0]);
    }

    /**
     * 根据方法名获取方法数组（包含父类查找）
     * 返回所有同名方法，包括重载方法
     */
    public static Method[] getMethodsByName(final Class<?> clazz, final String methodName)
    {
        return getMethodsByName(clazz, methodName, false);
    }

    /**
     * 根据方法名获取方法数组（包含父类查找）
     * 基于对象实例查找方法
     */
    public static Method[] getMethodsByName(final Object obj, final String methodName)
    {
        if (obj == null)
        {
            return new Method[0];
        }
        return getMethodsByName(obj.getClass(), methodName);
    }

    /**
     * 根据方法名和参数类型获取指定方法（包含父类查找）
     */
    public static Method getMethod(final Class<?> clazz, final String methodName, final boolean accessible, final Class<?>... parameterTypes)
    {
        if (clazz == null || StringUtils.isBlank(methodName))
        {
            return null;
        }

        // 直接使用类查找方法，不需要创建实例
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class)
        {
            try
            {
                Method method = currentClass.getDeclaredMethod(methodName, parameterTypes);
                if (accessible)
                {
                    makeAccessible(method);
                }
                return method;
            }
            catch (NoSuchMethodException e)
            {
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 根据方法名和参数类型获取指定方法（包含父类查找）
     */
    public static Method getMethod(final Class<?> clazz, final String methodName, final Class<?>... parameterTypes)
    {
        return getMethod(clazz, methodName, false, parameterTypes);
    }

    /**
     * 根据方法名和参数类型获取指定方法（包含父类查找）
     * 基于对象实例查找方法
     */
    public static Method getMethod(final Object obj, final String methodName, final boolean accessible, final Class<?>... parameterTypes)
    {
        if (obj == null)
        {
            return null;
        }
        return getMethod(obj.getClass(), methodName, accessible, parameterTypes);
    }

    /**
     * 根据方法名和参数类型获取指定方法（包含父类查找）
     * 基于对象实例查找方法
     */
    public static Method getMethod(final Object obj, final String methodName, final Class<?>... parameterTypes)
    {
        return getMethod(obj.getClass(), methodName, false, parameterTypes);
    }

    /**
     * 过滤方法数组，排除静态方法
     */
    public static Method[] excludeStaticMethods(Method[] methods)
    {
        if (methods == null || methods.length == 0)
        {
            return new Method[0];
        }

        List<Method> result = new ArrayList<>();
        for (Method method : methods)
        {
            if (!Modifier.isStatic(method.getModifiers()))
            {
                result.add(method);
            }
        }
        return result.toArray(new Method[0]);
    }

    /**
     * 过滤方法数组，只包含静态方法
     */
    public static Method[] getStaticMethods(Method[] methods)
    {
        if (methods == null || methods.length == 0)
        {
            return new Method[0];
        }

        List<Method> result = new ArrayList<>();
        for (Method method : methods)
        {
            if (Modifier.isStatic(method.getModifiers()))
            {
                result.add(method);
            }
        }
        return result.toArray(new Method[0]);
    }

    /**
     * 根据返回类型过滤方法数组
     */
    public static Method[] filterMethodsByReturnType(Method[] methods, Class<?> returnType)
    {
        if (methods == null || methods.length == 0 || returnType == null)
        {
            return new Method[0];
        }

        List<Method> result = new ArrayList<>();
        for (Method method : methods)
        {
            if (returnType.equals(method.getReturnType()))
            {
                result.add(method);
            }
        }
        return result.toArray(new Method[0]);
    }

    /**
     * 根据参数个数过滤方法数组
     */
    public static Method[] filterMethodsByParameterCount(Method[] methods, int parameterCount)
    {
        if (methods == null || methods.length == 0)
        {
            return new Method[0];
        }

        List<Method> result = new ArrayList<>();
        for (Method method : methods)
        {
            if (method.getParameterTypes().length == parameterCount)
            {
                result.add(method);
            }
        }
        return result.toArray(new Method[0]);
    }

    /**
     * 获取Getter方法数组
     * 返回所有以"get"或"is"开头的方法（符合JavaBean规范）
     */
    public static Method[] getGetterMethods(final Class<?> clazz)
    {
        Method[] allMethods = getMethods(clazz);
        List<Method> getterMethods = new ArrayList<>();

        for (Method method : allMethods)
        {
            if (isGetterMethod(method))
            {
                getterMethods.add(method);
            }
        }

        return getterMethods.toArray(new Method[0]);
    }

    /**
     * 获取Setter方法数组
     * 返回所有以"set"开头的方法（符合JavaBean规范）
     */
    public static Method[] getSetterMethods(final Class<?> clazz)
    {
        Method[] allMethods = getMethods(clazz);
        List<Method> setterMethods = new ArrayList<>();

        for (Method method : allMethods)
        {
            if (isSetterMethod(method))
            {
                setterMethods.add(method);
            }
        }

        return setterMethods.toArray(new Method[0]);
    }

    /**
     * 判断是否为Getter方法
     */
    public static boolean isGetterMethod(Method method)
    {
        if (method.getParameterTypes().length > 0)
        {
            return false;
        }

        String methodName = method.getName();

        // 标准 getter 方法
        if (methodName.startsWith(GETTER_PREFIX) && methodName.length() > 3)
        {
            return true;
        }

        // 布尔值 getter 方法 (isXxx)
        if (methodName.startsWith(IS_PREFIX) && methodName.length() > 2)
        {
            Class<?> returnType = method.getReturnType();
            return returnType == boolean.class || returnType == Boolean.class;
        }

        return false;
    }

    /**
     * 判断是否为Setter方法
     */
    public static boolean isSetterMethod(Method method)
    {
        if (method.getParameterTypes().length != 1)
        {
            return false;
        }

        String methodName = method.getName();
        return methodName.startsWith(SETTER_PREFIX) && methodName.length() > 3;
    }

    /**
     * 通过无参构造方法创建bean
     */
    public static <T> T newInstance(Class<T> aclass)
    {
        if (aclass == null)
        {
            throw new RuntimeException("the incoming class object is empty");
        }
        try {
            // 获取无参构造函数
            Constructor<T> constructor = aclass.getDeclaredConstructor();

            // 确保构造函数可访问
            if (!constructor.isAccessible())
            {
                constructor.setAccessible(true);
            }

            return constructor.newInstance();
        }
        catch(InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            throw new RuntimeException("failed to create the object: " + aclass.getName() + ", reason: " + e.getMessage(), e);
        }
    }

    /**
     * 调用Getter方法.
     * 支持多级，如：对象名.对象名.方法
     * <p>
     * // 获取user对象的address属性的city属性
     * String city = ReflectUtils.invokeGetter(user, "address.city");
     * // 等价于：user.getAddress().getCity()
     */
    @SuppressWarnings("unchecked")
    public static <E> E invokeGetter(Object obj, String propertyName)
    {
        Object object = obj;
        for (String name : StringUtils.split(propertyName, "."))
        {
            String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
            object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
        }
        return (E) object;
    }

    /**
     * 调用Setter方法, 仅匹配方法名。
     * 支持多级，如：对象名.对象名.方法
     * <p>
     * // 设置user对象的address属性的city属性
     * ReflectUtils.invokeSetter(user, "address.city", "北京市");
     * // 等价于：user.getAddress().setCity("北京市")
     */
    public static <E> void invokeSetter(Object obj, String propertyName, E value)
    {
        Object object = obj;
        String[] names = StringUtils.split(propertyName, ".");
        for (int i = 0; i < names.length; i++)
        {
            if (i < names.length - 1)
            {
                String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(names[i]);
                object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
            }
            else
            {
                String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(names[i]);
                invokeMethodByName(object, setterMethodName, new Object[] { value });
            }
        }
    }

    /**
     * 调用Getter方法，单级
     */
    @SuppressWarnings("unchecked")
    public static <E> E invokeGetter(Object obj, Field field)
    {
        if (obj == null || field == null)
        {
            return null;
        }
        Method[] getterMethods = getGetterMethods(obj.getClass());
        for (Method method : getterMethods)
        {
            if (method.getName().equals(GETTER_PREFIX + StringUtils.capitalize(field.getName()))
                    || method.getName().equals(IS_PREFIX + StringUtils.capitalize(field.getName())))
            {
                return invokeMethod(obj, method);
            }
        }
        logger.error("在 [" + obj.getClass() + "] 中，没有找到 [" + field.getName() + "] 的 Getter方法 ");
        throw new IllegalArgumentException("在 [" + obj.getClass() + "] 中，没有找到 [" + field.getName() + "] 的 Getter方法 ");
    }

    /**
     * 调用Setter方法, 单级
     */
    public static <E> void invokeSetter(Object obj, Field field, E value)
    {
        if (obj == null || field == null)
        {
            return;
        }
        Method[] setterMethods = getSetterMethods(obj.getClass());
        for (Method method : setterMethods)
        {
            if (method.getName().equals(SETTER_PREFIX + StringUtils.capitalize(field.getName())))
            {
                invokeMethod(obj, method, value);
                return;
            }
        }
        logger.error("在 [" + obj.getClass() + "] 中，没有找到 [" + field.getName() + "] 的 Setter方法 ");
        throw new IllegalArgumentException("在 [" + obj.getClass() + "] 中，没有找到 [" + field.getName() + "] 的 Setter方法 ");
    }

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     */
    @SuppressWarnings("unchecked")
    public static <E> E getFieldValue(final Object obj, final String fieldName)
    {
        Field field = getAccessibleField(obj, fieldName);
        if (field == null)
        {
            logger.debug("在 [" + obj.getClass() + "] 中，没有找到 [" + fieldName + "] 字段 ");
            return null;
        }
        E result = null;
        try
        {
            result = (E) field.get(obj);
        }
        catch (IllegalAccessException e)
        {
            logger.error("不可能抛出的异常{}", e.getMessage());
        }
        return result;
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     */
    public static <E> void setFieldValue(final Object obj, final String fieldName, final E value)
    {
        Field field = getAccessibleField(obj, fieldName);
        if (field == null)
        {
            // throw new IllegalArgumentException("在 [" + obj.getClass() + "] 中，没有找到 [" + fieldName + "] 字段 ");
            logger.debug("在 [" + obj.getClass() + "] 中，没有找到 [" + fieldName + "] 字段 ");
            return;
        }
        try
        {
            field.set(obj, value);
        }
        catch (IllegalAccessException e)
        {
            logger.error("不可能抛出的异常: {}", e.getMessage());
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符.
     * 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用.
     * 同时匹配方法名+参数类型，
     */
    @SuppressWarnings("unchecked")
    public static <E> E invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
            final Object[] args)
    {
        if (obj == null || methodName == null)
        {
            return null;
        }
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null)
        {
            logger.debug("在 [" + obj.getClass() + "] 中，没有找到 [" + methodName + "] 方法 ");
            return null;
        }
        try
        {
            return (E) method.invoke(obj, args);
        }
        catch (Exception e)
        {
            String msg = "method: " + method + ", obj: " + obj + ", args: " + args + "";
            throw convertReflectionExceptionToUnchecked(msg, e);
        }
    }

    /**
     * 通用的方法调用封装
     * 根据方法对象直接调用方法
     */
    @SuppressWarnings("unchecked")
    public static <E> E invokeMethod(final Object obj, final Method method, final boolean accessible, final Object... args)
    {
        if (obj == null || method == null)
        {
            return null;
        }

        try
        {
            if (accessible)
            {
                makeAccessible(method);
            }
            // 执行方法调用
            return (E) method.invoke(obj, args);
        }
        catch (IllegalAccessException e)
        {
            logger.error("方法 [{}] 访问被拒绝: {}", method.getName(), e.getMessage());
            return null;
        }
        catch (InvocationTargetException e)
        {
            Throwable targetException = e.getTargetException();
            logger.error("方法 [{}] 执行异常: {}", method.getName(), targetException.getMessage());
            throw convertReflectionExceptionToUnchecked("方法执行失败: " + method.getName(), e);
        }
        catch (IllegalArgumentException e)
        {
            logger.error("方法 [{}] 参数不匹配: {}", method.getName(), e.getMessage());
            return null;
        }
    }

    /**
     * 通用的方法调用封装
     * 根据方法对象直接调用方法
     */
    @SuppressWarnings("unchecked")
    public static <E> E invokeMethod(final Object obj, final Method method, final Object... args)
    {
        return invokeMethod(obj, method, false, args);
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符，
     * 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用.
     * 只匹配函数名，如果有多个同名函数调用第一个。
     */
    @SuppressWarnings("unchecked")
    public static <E> E invokeMethodByName(final Object obj, final String methodName, final Object[] args)
    {
        Method method = getAccessibleMethodByName(obj, methodName, args.length);
        if (method == null)
        {
            // 如果为空不报错，直接返回空。
            logger.debug("在 [" + obj.getClass() + "] 中，没有找到 [" + methodName + "] 方法 ");
            return null;
        }
        try
        {
            // 类型转换（将参数数据类型转换为目标方法参数类型）
            Class<?>[] cs = method.getParameterTypes();
            for (int i = 0; i < cs.length; i++)
            {
                if (args[i] != null && !args[i].getClass().equals(cs[i]))
                {
                    if (cs[i] == String.class)
                    {
                        args[i] = Convert.toStr(args[i]);
                        if (StringUtils.endsWith((String) args[i], ".0"))
                        {
                            args[i] = StringUtils.substringBefore((String) args[i], ".0");
                        }
                    }
                    else if (cs[i] == Integer.class)
                    {
                        args[i] = Convert.toInt(args[i]);
                    }
                    else if (cs[i] == Long.class)
                    {
                        args[i] = Convert.toLong(args[i]);
                    }
                    else if (cs[i] == Double.class)
                    {
                        args[i] = Convert.toDouble(args[i]);
                    }
                    else if (cs[i] == Float.class)
                    {
                        args[i] = Convert.toFloat(args[i]);
                    }
                    else if (cs[i] == Date.class)
                    {
                        if (args[i] instanceof String)
                        {
                            args[i] = DateUtils.parseDate(args[i]);
                        }
                        else
                        {
                            args[i] = DateUtil.getJavaDate((Double) args[i]);
                        }
                    }
                    else if (cs[i] == LocalDate.class)
                    {
                        if (args[i] instanceof String)
                        {
                            args[i] = DateUtils.parseDate(args[i]);
                        }
                        else
                        {
                            args[i] = LocalDateUtils.toLocalDate(DateUtil.getJavaDate((Double) args[i]));
                        }
                    }
                    else if (cs[i] == LocalDateTime.class)
                    {
                        if (args[i] instanceof String)
                        {
                            args[i] = DateUtils.parseDate(args[i]);
                        }
                        else
                        {
                            args[i] = LocalDateUtils.toLocalDateTime(DateUtil.getJavaDate((Double) args[i]));
                        }
                    }
                    else if (cs[i] == boolean.class || cs[i] == Boolean.class)
                    {
                        args[i] = Convert.toBool(args[i]);
                    }
                }
            }
            return (E) method.invoke(obj, args);
        }
        catch (Exception e)
        {
            String msg = "method: " + method + ", obj: " + obj + ", args: " + args + "";
            throw convertReflectionExceptionToUnchecked(msg, e);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.
     */
    public static Field getAccessibleField(final Object obj, final String fieldName)
    {
        // 为空不报错。直接返回 null
        if (obj == null)
        {
            return null;
        }
        Validate.notBlank(fieldName, "fieldName can't be blank");
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass())
        {
            try
            {
                Field field = superClass.getDeclaredField(fieldName);
                makeAccessible(field);
                return field;
            }
            catch (NoSuchFieldException e)
            {
                continue;
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.
     * 匹配函数名+参数类型。
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     */
    public static Method getAccessibleMethod(final Object obj, final String methodName,
            final Class<?>... parameterTypes)
    {
        // 为空不报错。直接返回 null
        if (obj == null)
        {
            return null;
        }
        Validate.notBlank(methodName, "methodName can't be blank");
        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass())
        {
            try
            {
                Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
                makeAccessible(method);
                return method;
            }
            catch (NoSuchMethodException e)
            {
                continue;
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.
     * 只匹配函数名。
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     */
    public static Method getAccessibleMethodByName(final Object obj, final String methodName, int argsNum)
    {
        // 为空不报错。直接返回 null
        if (obj == null)
        {
            return null;
        }
        Validate.notBlank(methodName, "methodName can't be blank");
        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass())
        {
            Method[] methods = searchType.getDeclaredMethods();
            for (Method method : methods)
            {
                if (method.getName().equals(methodName) && method.getParameterTypes().length == argsNum)
                {
                    makeAccessible(method);
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Method method)
    {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
                && !method.isAccessible())
        {
            method.setAccessible(true);
        }
    }

    /**
     * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Field field)
    {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())
                || Modifier.isFinal(field.getModifiers())) && !field.isAccessible())
        {
            field.setAccessible(true);
        }
    }

    /**
     * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处
     * <p>
     * 例如UserDao extends BaseDao<User> getClassGenricType(UserDao.class) 返回 User
     * <p>
     * 如无法找到, 返回Object.class.
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassGenricType(final Class clazz)
    {
        return getClassGenricType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
     * 如无法找到, 返回Object.class.
     */
    public static Class getClassGenricType(final Class clazz, final int index)
    {
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType))
        {
            logger.debug(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0)
        {
            logger.debug("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class))
        {
            logger.debug(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }

        return (Class) params[index];
    }

    /**
     * 获取被AOP代理后的原始类
     * <p>
     * // 获取目标对象
     * Object target = joinPoint.getTarget();
     * // 如果使用了CGLIB代理，target.getClass() 可能是: UserService$$EnhancerBySpringCGLIB$$123456
     * Class<?> originalClass = ReflectUtils.getUserClass(target);
     * // originalClass 会是: UserService.class
     * System.out.println("原始类: " + originalClass.getSimpleName());
     * System.out.println("代理类: " + target.getClass().getSimpleName());
     */
    public static Class<?> getUserClass(Object instance)
    {
        if (instance == null)
        {
            throw new RuntimeException("Instance must not be null");
        }
        Class clazz = instance.getClass();
        if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR))
        {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass))
            {
                return superClass;
            }
        }

        return clazz;
    }

    /**
     * 将反射时的checked exception转换为unchecked exception.
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(String msg, Exception e)
    {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException)
        {
            return new IllegalArgumentException(msg, e);
        }
        else if (e instanceof InvocationTargetException)
        {
            return new RuntimeException(msg, ((InvocationTargetException) e).getTargetException());
        }
        return new RuntimeException(msg, e);
    }
}
