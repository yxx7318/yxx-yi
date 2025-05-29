package com.yxx.framework.utils;

import com.yxx.common.utils.Arith;
import com.yxx.common.utils.ip.IpUtils;
import com.yxx.framework.web.domain.Server;
import com.yxx.framework.web.domain.server.*;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;

public class ServerUtils {

    public static final int OSHI_WAIT_SECOND = 1000;

    public static void copyTo(Server server)
    {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();

        setCpuInfo(hal.getProcessor(), server.getCpu());

        setMemInfo(hal.getMemory(), server.getMem());

        setSysInfo(server.getSys());

        setJvmInfo(server.getJvm());

        setSysFilesInfo(si.getOperatingSystem(), server.getSysFiles());
    }

    /**
     * 设置CPU信息
     */
    private static void setCpuInfo(CentralProcessor processor, Cpu cpu)
    {
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.ordinal()] - prevTicks[CentralProcessor.TickType.NICE.ordinal()];
        long irq = ticks[CentralProcessor.TickType.IRQ.ordinal()] - prevTicks[CentralProcessor.TickType.IRQ.ordinal()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.ordinal()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.ordinal()];
        long steal = ticks[CentralProcessor.TickType.STEAL.ordinal()] - prevTicks[CentralProcessor.TickType.STEAL.ordinal()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.ordinal()] - prevTicks[CentralProcessor.TickType.SYSTEM.ordinal()];
        long user = ticks[CentralProcessor.TickType.USER.ordinal()] - prevTicks[CentralProcessor.TickType.USER.ordinal()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.ordinal()] - prevTicks[CentralProcessor.TickType.IOWAIT.ordinal()];
        long idle = ticks[CentralProcessor.TickType.IDLE.ordinal()] - prevTicks[CentralProcessor.TickType.IDLE.ordinal()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        cpu.setCpuNum(processor.getLogicalProcessorCount());
        cpu.setTotal(totalCpu);
        cpu.setSys(cSys);
        cpu.setUsed(user);
        cpu.setWait(iowait);
        cpu.setFree(idle);
    }

    /**
     * 设置内存信息
     */
    private static void setMemInfo(GlobalMemory memory, Mem mem)
    {
        mem.setTotal(memory.getTotal());
        mem.setUsed(memory.getTotal() - memory.getAvailable());
        mem.setFree(memory.getAvailable());
    }

    /**
     * 设置服务器信息
     */
    private static void setSysInfo(Sys sys)
    {
        Properties props = System.getProperties();
        sys.setComputerName(IpUtils.getHostName());
        sys.setComputerIp(IpUtils.getHostIp());
        sys.setOsName(props.getProperty("os.name"));
        sys.setOsArch(props.getProperty("os.arch"));
        sys.setUserDir(props.getProperty("user.dir"));
    }

    /**
     * 设置Java虚拟机
     */
    private static void setJvmInfo(Jvm jvm)
    {
        Properties props = System.getProperties();
        jvm.setTotal(Runtime.getRuntime().totalMemory());
        jvm.setMax(Runtime.getRuntime().maxMemory());
        jvm.setFree(Runtime.getRuntime().freeMemory());
        jvm.setVersion(props.getProperty("java.version"));
        jvm.setHome(props.getProperty("java.home"));
    }

    /**
     * 设置磁盘信息
     */
    private static void setSysFilesInfo(OperatingSystem os, List<SysFile> sysFiles)
    {
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray)
        {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            SysFile sysFile = new SysFile();
            sysFile.setDirName(fs.getMount());
            sysFile.setSysTypeName(fs.getType());
            sysFile.setTypeName(fs.getName());
            sysFile.setTotal(convertFileSize(total));
            sysFile.setFree(convertFileSize(free));
            sysFile.setUsed(convertFileSize(used));
            sysFile.setUsage(Arith.mul(Arith.div(used, total, 4), 100));
            sysFiles.add(sysFile);
        }
    }

    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
     */
    public static String convertFileSize(long size)
    {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb)
        {
            return String.format("%.1f GB", (float) size / gb);
        }
        else if (size >= mb)
        {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        }
        else if (size >= kb)
        {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        }
        else
        {
            return String.format("%d B", size);
        }
    }
}