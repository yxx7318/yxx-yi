package com.yxx.framework.utils;

import com.yxx.common.utils.DateUtils;
import com.yxx.framework.web.domain.ProcessInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;
import oshi.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static com.yxx.framework.web.utils.ServerUtils.OSHI_WAIT_SECOND;


public class ProcessMonitor {

    private final String osName = System.getProperty("os.name").toLowerCase();

    private final Map<Integer, OSProcess> prevProcessesMap;

    private final Map<Integer, OSProcess> currentProcessesMap;

    private final Set<Integer> childPidSet;

    // 初始化方法
    public ProcessMonitor() {
        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();

        // 创建进程PID到初始快照对象的映射
        prevProcessesMap = new HashMap<>();
        for (OSProcess p : new ArrayList<>(os.getProcesses())) {
            prevProcessesMap.put(p.getProcessID(), p);
        }
        Util.sleep(OSHI_WAIT_SECOND);
        // 创建进程PID到新快照对象的映射
        currentProcessesMap = new HashMap<>();
        for (OSProcess p : new ArrayList<>(os.getProcesses())) {
            currentProcessesMap.put(p.getProcessID(), p);
        }

        childPidSet = new HashSet<>();
    }

    /**
     * 根据集合类一次获取所有的信息
     */
    public List<ProcessInfo> findProcessByAppNames(Collection<String> collection) {
        List<ProcessInfo> result = new ArrayList<>();
        collection.forEach((item) -> result.addAll(findProcess(item)));
        return result;
    }

    /**
     * 根据集合类一次获取所有的信息
     */
    public List<ProcessInfo> findProcessByPorts(Collection<Integer> collection) {
        List<ProcessInfo> result = new ArrayList<>();
        collection.forEach((item) -> {
            ProcessInfo info = findProcess(item);
            if (!ObjectUtils.isEmpty(info)) result.add(info);
        });
        return result.stream().filter((item) -> !childPidSet.contains(item.getPid())).collect(Collectors.toList());
    }

    /**
     * 根据应用名称查找进程
     */
    public List<ProcessInfo> findProcess(String appName) {
        List<ProcessInfo> result = new ArrayList<>();
        for (OSProcess process : prevProcessesMap.values()) {
            String name = process.getName();
            String commandLine = process.getCommandLine();
            if ((name != null && name.contains(appName)) ||
                    (commandLine != null && commandLine.contains(appName))) {
                ProcessInfo info = createProcessInfo(process);
                if (!ObjectUtils.isEmpty(info)) result.add(info);
            }
        }
        return result.stream().filter((item) -> !childPidSet.contains(item.getPid())).collect(Collectors.toList());
    }

    /**
     * 根据端口号查找进程
     */
    public ProcessInfo findProcess(int port) {
        int pid = findPidByPort(port);
        if (pid == -1) return null;
        return createProcessInfo(prevProcessesMap.get(pid));
    }

    /**
     * 依据进程对象获取构建对象结果
     */
    private ProcessInfo createProcessInfo(OSProcess process) {
        // 如果是已经计算汇总过的子进程，则不进行处理
        if (childPidSet.contains(process.getProcessID())) {
            return null;
        }

        // 获取所有子进程的PID（递归）
        List<Integer> childPids = getAllChildPids(process.getProcessID());
        // 加入子进程的排除列中
        childPidSet.addAll(childPids);

        ProcessInfo info = new ProcessInfo();
        info.setPid(process.getProcessID());
        info.setName(process.getName());
        info.setCommandLine(process.getCommandLine());

        info.setMemoryUsage(FormatUtil.formatBytes(getTotalMemoryUsage(process, childPids)));
        // 现在的和前面的进行对比
        info.setCpuUsagePercent(getCpuUsagePercent(process, childPids));

        info.setStartTime(DateFormatUtils.format(process.getStartTime(), DateUtils.YYYY_MM_DD_HH_MM_SS));
        info.setUser(process.getUser());
        info.setState(process.getState().name());
        info.setListening(getListeningByPid(info.getPid()));
        return info;
    }

    /**
     * 递归获取所有子进程PID
     */
    private List<Integer> getAllChildPids(int parentPid) {
        List<Integer> childPids = new ArrayList<>();
        for (OSProcess proc : prevProcessesMap.values()) {
            if (proc.getParentProcessID() == parentPid) {
                int childPid = proc.getProcessID();
                childPids.add(childPid);
                childPids.addAll(getAllChildPids(childPid));
            }
        }
        return childPids;
    }

    /**
     * 获取对应进程的内存占用
     */
    private long getTotalMemoryUsage(OSProcess process, List<Integer> childPids) {
        // 计算总内存占用
        long totalMemory = process.getResidentSetSize(); // 主进程内存
        for (int pid : childPids) {
            OSProcess p = prevProcessesMap.get(pid);
            if (p != null) {
                totalMemory += p.getResidentSetSize();
            }
        }
        return totalMemory;
    }

    /**
     * 获取对应进程的CPU占用
     */
    private double getCpuUsagePercent(OSProcess process, List<Integer> childPids) {
        // 计算总CPU占用
        double totalCpuUsage = process.getProcessCpuLoadBetweenTicks(currentProcessesMap.get(process.getProcessID())); // 主进程占用
        for (int pid : childPids) {
            OSProcess p = prevProcessesMap.get(pid);
            if (p != null) {
                totalCpuUsage += p.getProcessCpuLoadBetweenTicks(currentProcessesMap.get(p.getProcessID()));
            }
        }
        return totalCpuUsage;
    }

    /**
     * 通过端口号获取PID
     *
     * @param port 端口
     * @return PID
     */
    private int findPidByPort(int port) {
        if (osName.contains("win")) {
            return findPidByPortWindows(port);
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("mac")) {
            return findPidByPortLinux(port);
        }
        throw new UnsupportedOperationException("Unsupported OS");
    }

    /**
     * 获取port获取程序pid(Windows)
     */
    private int findPidByPortWindows(int port) {
        String command = String.format("netstat -ano | findstr \"LISTENING\" | findstr :%d | findstr /V \":%d[0-9]\"", port, port);
        Process process;
        try {
            process = Runtime.getRuntime().exec(new String[]{"cmd", "/c", command});
        } catch (IOException e) {
            return -1;
        }
        try (
                InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "GBK");
                BufferedReader reader = new BufferedReader(inputStreamReader);
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.trim().split("\\s+");
                if (tokens.length >= 5 && tokens[1].contains(":" + port)) {
                    return Integer.parseInt(tokens[4]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    /**
     * 获取port获取程序pid(Linux)
     */
    private int findPidByPortLinux(int port) {
        // 构造命令字符串
        String command = "netstat -tulnp | grep \":%d\\b\" | awk '{print $7}' | cut -d'/' -f1";
        String[] cmd = {"/bin/sh", "-c", String.format(command, port)};

        // 执行命令
        Process process;
        try {
            process = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            return -1;
        }
        try (
                InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
                BufferedReader reader = new BufferedReader(inputStreamReader);
        ) {
            String line = reader.readLine();
            return line != null ? Integer.parseInt(line.trim()) : -1;
        } catch (NumberFormatException | IOException e) {
            return -1;
        }
    }

    /**
     * 依据pid获取程序监听信息
     */
    private String getListeningByPid(int pid) {
        try {
            String[] cmd = osName.contains("win")
                    ? new String[]{"cmd", "/c", String.format("netstat -ano | findstr /R /C:\"LISTENING[ \\t]*%d *$\"", pid)}
                    : new String[]{"/bin/sh", "-c", String.format("netstat -tulnp | grep %d", pid)};
            Process process = Runtime.getRuntime().exec(cmd);

            StringBuilder ports = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), osName.contains("win") ? "GBK" : "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    ports.append(line).append("\n");
                }
            }
            return ports.toString();
        } catch (IOException e) {
            return "N/A";
        }
    }
}