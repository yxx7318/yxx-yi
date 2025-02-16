package com.yxx.framework.web.domain;

import com.yxx.common.utils.Arith;

import java.io.Serializable;

public class ProcessInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 进程 ID，唯一标识一个进程
     */
    private int pid;

    /**
     * 进程名称，通常是可执行文件的名称或服务名称
     */
    private String name;

    /**
     * 进程的命令行参数，包含启动该进程时使用的完整命令及其参数
     */
    private String commandLine;

    /**
     * 进程的内存使用情况，通常表示为当前进程占用的物理内存大小
     */
    private String memoryUsage;

    /**
     * 进程的 CPU 使用率，表示该进程占用了多少 CPU 资源
     */
    private double cpuUsagePercent;

    /**
     * 进程的启动时间，表示该进程开始运行的时间"yyyy-MM-dd HH:mm:ss"
     */
    private String startTime;

    /**
     * 运行该进程的用户名称
     */
    private String user;

    /**
     * 进程的状态，描述当前进程的运行状态
     * 常见的状态包括：
     * - "Running"：正在运行
     * - "Sleeping"：休眠
     * - "Stopped"：已停止
     * - "Zombie"：僵尸进程
     */
    private String state;

    /**
     * 进程监听信息
     */
    private String listening;

    @Override
    public String toString() {
        return String.format(
                "PID: %d\nName: %s\nCommand Line: %s\nMemory: %s\nCPU Usage: %.2f%%\nStart Time: %s\nUser: %s\nState: %s\nListening: %s\n",
                getPid(), getName(), getCommandLine(), getMemoryUsage(), getCpuUsagePercent(), getStartTime(), getUser(), getState(), getListening());
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommandLine() {
        return commandLine;
    }

    public void setCommandLine(String commandLine) {
        this.commandLine = commandLine;
    }

    public String getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(String memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public double getCpuUsagePercent() {
        return Arith.round(Arith.mul(cpuUsagePercent, 100), 2);
    }

    public void setCpuUsagePercent(double cpuUsagePercent) {
        this.cpuUsagePercent = cpuUsagePercent;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getListening() {
        return listening;
    }

    public void setListening(String listening) {
        this.listening = listening;
    }
}
