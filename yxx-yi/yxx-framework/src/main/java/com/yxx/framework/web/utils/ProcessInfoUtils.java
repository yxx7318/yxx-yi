package com.yxx.framework.web.utils;

import com.yxx.common.utils.spring.SpringUtils;
import com.yxx.common.yxx.utils.StreamUtils;
import com.yxx.framework.web.domain.ServerProperties;
import com.yxx.framework.web.domain.ProcessInfo;

import java.util.*;

public class ProcessInfoUtils {

    private static final List<Integer> ports = SpringUtils.getBean(ServerProperties.class).getPorts();

    private static final List<String> appNames = SpringUtils.getBean(ServerProperties.class).getAppNames();

    /**
     * 去重
     */
    private static List<ProcessInfo> removeDuplicates(List<ProcessInfo> data) {
        Set<Integer> set = new HashSet<>();
        return StreamUtils.filter(data, (item) -> {
            if (!set.contains(item.getPid())) {
                set.add(item.getPid());
                return true;
            }
            return false;
        });
    }

    /**
     * 设置默认值
     */
    public static void setDefault(Collection<ProcessInfo> collection) {
        collection.addAll(getDefault());
    }

    /**
     * 获取默认值
     */
    public static List<ProcessInfo> getDefault() {
        List<ProcessInfo> result = new ArrayList<>();
        ProcessMonitor processMonitor = new ProcessMonitor();
        result.addAll(processMonitor.findProcessByPorts(ports));
        result.addAll(processMonitor.findProcessByAppNames(appNames));
        // 获取去重结果
        return removeDuplicates(result);
    }

    public static List<ProcessInfo> findProcessByAppNames(Collection<String> collection) {
        return new ProcessMonitor().findProcessByAppNames(collection);
    }

    public static List<ProcessInfo> findProcessByPorts(Collection<Integer> collection) {
        return new ProcessMonitor().findProcessByPorts(collection);
    }

    public static List<ProcessInfo> findProcess(String appName) {
        return new ProcessMonitor().findProcess(appName);
    }

    public static ProcessInfo findProcess(Integer port) {
        return new ProcessMonitor().findProcess(port);
    }

}
