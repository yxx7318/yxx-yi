package com.yxx.quartz.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import com.yxx.common.utils.ExceptionUtil;
import com.yxx.common.utils.StringUtils;
import com.yxx.common.utils.spring.SpringUtils;
import com.yxx.quartz.domain.SysJob;
import org.quartz.JobExecutionException;

/**
 * 任务执行工具
 */
public class JobInvokeUtil
{
    /**
     * 执行方法
     *
     * @param sysJob 系统任务
     */
    public static String invokeMethod(SysJob sysJob) throws Exception
    {
        String invokeTarget = sysJob.getInvokeTarget();
        String beanName = getBeanName(invokeTarget);
        String methodName = getMethodName(invokeTarget);
        List<Object[]> methodParams = getMethodParams(invokeTarget);
        Object bean ;
        if (!isValidClassName(beanName))
        {
            bean = SpringUtils.getBean(beanName);
        }
        else
        {
            bean = Class.forName(beanName).getDeclaredConstructor().newInstance();
        }
        String result;
        try {
            result = invokeMethod(bean, methodName, methodParams);
        } catch (InvocationTargetException e) {
            // 获取原始的异常
            Throwable targetException = e.getTargetException();
            // 如果是自定义的任务异常，则进行异常二次抛出方便日志的写入
            if (targetException instanceof JobExecutionException) {
                throw new JobExecutionException(ExceptionUtil.getRootErrorMessage(e));
            }
            throw new Exception(e);
        }
        return result;
    }

    /**
     * 调用任务方法
     *
     * @param bean 目标对象
     * @param methodName 方法名称
     * @param methodParams 方法参数
     * @return 方法返回结果
     */
    private static String invokeMethod(Object bean, String methodName, List<Object[]> methodParams)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException
    {
        Object result;
        if (StringUtils.isNotNull(methodParams) && methodParams.size() > 0)
        {
            Method method = bean.getClass().getMethod(methodName, getMethodParamsType(methodParams));
            result = method.invoke(bean, getMethodParamsValue(methodParams));
        }
        else
        {
            Method method = bean.getClass().getMethod(methodName);
            result = method.invoke(bean);
        }
        return StringUtils.convertObjToString(result);
    }

    /**
     * 校验是否为为class包名
     * 
     * @param invokeTarget 名称
     * @return true是 false否
     */
    public static boolean isValidClassName(String invokeTarget)
    {
        return StringUtils.countMatches(invokeTarget, ".") > 1;
    }

    /**
     * 获取bean名称
     * 
     * @param invokeTarget 目标字符串
     * @return bean名称
     */
    public static String getBeanName(String invokeTarget)
    {
        String beanName = StringUtils.substringBefore(invokeTarget, "(");
        return StringUtils.substringBeforeLast(beanName, ".");
    }

    /**
     * 获取bean方法
     * 
     * @param invokeTarget 目标字符串
     * @return method方法
     */
    public static String getMethodName(String invokeTarget)
    {
        String methodName = StringUtils.substringBefore(invokeTarget, "(");
        return StringUtils.substringAfterLast(methodName, ".");
    }

    /**
     * 获取method方法参数相关列表
     * 
     * @param invokeTarget 目标字符串
     * @return method方法相关参数列表
     */
    public static List<Object[]> getMethodParams(String invokeTarget)
    {
        String methodStr = StringUtils.substringBetweenLast(invokeTarget, "(", ")");
        if (StringUtils.isEmpty(methodStr))
        {
            return null;
        }
        String[] methodParams = methodStr.split(",(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)");
        List<Object[]> classs = new LinkedList<>();
        for (int i = 0; i < methodParams.length; i++)
        {
            String str = StringUtils.trimToEmpty(methodParams[i]);
            // String字符串类型，以'或"开头
            if (StringUtils.startsWithAny(str, "'", "\""))
            {
                classs.add(new Object[] { StringUtils.substring(str, 1, str.length() - 1), String.class });
            }
            // boolean布尔类型，等于true或者false
            else if ("true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str))
            {
                classs.add(new Object[] { Boolean.valueOf(str), Boolean.class });
            }
            // long长整形，以L结尾
            else if (StringUtils.endsWith(str, "L"))
            {
                classs.add(new Object[] { Long.valueOf(StringUtils.substring(str, 0, str.length() - 1)), Long.class });
            }
            // double浮点类型，以D结尾
            else if (StringUtils.endsWith(str, "D"))
            {
                classs.add(new Object[] { Double.valueOf(StringUtils.substring(str, 0, str.length() - 1)), Double.class });
            }
            // 其他类型归类为整形
            else
            {
                classs.add(new Object[] { Integer.valueOf(str), Integer.class });
            }
        }
        return classs;
    }

    /**
     * 获取参数类型
     * 
     * @param methodParams 参数相关列表
     * @return 参数类型列表
     */
    public static Class<?>[] getMethodParamsType(List<Object[]> methodParams)
    {
        Class<?>[] classs = new Class<?>[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams)
        {
            classs[index] = (Class<?>) os[1];
            index++;
        }
        return classs;
    }

    /**
     * 获取参数值
     * 
     * @param methodParams 参数相关列表
     * @return 参数值列表
     */
    public static Object[] getMethodParamsValue(List<Object[]> methodParams)
    {
        Object[] classs = new Object[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams)
        {
            classs[index] = (Object) os[0];
            index++;
        }
        return classs;
    }
}
