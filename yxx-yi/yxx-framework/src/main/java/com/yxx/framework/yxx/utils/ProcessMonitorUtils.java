package com.yxx.framework.yxx.utils;

import com.yxx.framework.web.domain.ProcessInfo;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;
import static com.yxx.framework.yxx.utils.ServerUtils.OSHI_WAIT_SECOND;
import oshi.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ProcessMonitorUtils {

    /**
     * 根据应用名称查找进程
     */
    public static List<ProcessInfo> findProcesses(String appName) {
        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();
        List<ProcessInfo> result = new ArrayList<>();

        for (OSProcess process : os.getProcesses()) {
            String name = process.getName();
            String commandLine = process.getCommandLine();

            if ((name != null && name.contains(appName)) ||
                    (commandLine != null && commandLine.contains(appName))) {
                ProcessInfo info = createProcessInfo(process);
                result.add(info);
            }
        }
        return result;
    }

    /**
     * 根据端口号查找进程
     */
    public static ProcessInfo findProcess(int port) {
        int pid = findPidByPort(port);
        if (pid == -1) return null;

        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();
        OSProcess process = os.getProcess(pid);

        return createProcessInfo(process);
    }

    private static ProcessInfo createProcessInfo(OSProcess process) {
        ProcessInfo info = new ProcessInfo();
        info.setPid(process.getProcessID());
        info.setName(process.getName());
        info.setCommandLine(process.getCommandLine());
        info.setMemoryUsage(FormatUtil.formatBytes(process.getResidentSetSize()));
        Util.sleep(OSHI_WAIT_SECOND);
        info.setCpuUsagePercent(process.getProcessCpuLoadBetweenTicks(process));
        info.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((new Date(process.getStartTime()))));
        info.setUser(process.getUser());
        info.setState(process.getState().name());
        info.setListening(getListeningByPid(info.getPid()));
        return info;
    }

    /**
     * 通过端口号获取PID
     *
     * @param port 端口
     * @return PID
     */
    private static int findPidByPort(int port) {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return findPidByPortWindows(port);
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            return findPidByPortLinux(port);
        }
        throw new UnsupportedOperationException("Unsupported OS");
    }

    private static int findPidByPortWindows(int port) {
        String command = String.format("netstat -ano | findstr :%d | findstr /V \":%d[0-9]\"", port, port);
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

    private static int findPidByPortLinux(int port) {
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

    private static String getListeningByPid(int pid) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String[] cmd = os.contains("win")
                    ? new String[]{"cmd", "/c", String.format("netstat -ano | findstr %d", pid)}
                    : new String[]{"/bin/sh", "-c", String.format("netstat -tulnp | grep %d", pid)};
            Process process = Runtime.getRuntime().exec(cmd);

            StringBuilder ports = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), os.contains("win") ? "GBK" : "UTF-8"))) {
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