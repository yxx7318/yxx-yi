package com.yxx.framework.web.domain;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import com.yxx.framework.web.domain.server.Cpu;
import com.yxx.framework.web.domain.server.Jvm;
import com.yxx.framework.web.domain.server.Mem;
import com.yxx.framework.web.domain.server.Sys;
import com.yxx.framework.web.domain.server.SysFile;

/**
 * 服务器相关信息
 *
 * @author ruoyi
 */
public class Server implements Serializable
{

    private static final long serialVersionUID = 1L;

    /**
     * CPU相关信息
     */
    private Cpu cpu = new Cpu();

    /**
     * 內存相关信息
     */
    private Mem mem = new Mem();

    /**
     * JVM相关信息
     */
    private Jvm jvm = new Jvm();

    /**
     * 服务器相关信息
     */
    private Sys sys = new Sys();

    /**
     * 磁盘相关信息
     */
    private List<SysFile> sysFiles = new LinkedList<SysFile>();

    public Cpu getCpu()
    {
        return cpu;
    }

    public void setCpu(Cpu cpu)
    {
        this.cpu = cpu;
    }

    public Mem getMem()
    {
        return mem;
    }

    public void setMem(Mem mem)
    {
        this.mem = mem;
    }

    public Jvm getJvm()
    {
        return jvm;
    }

    public void setJvm(Jvm jvm)
    {
        this.jvm = jvm;
    }

    public Sys getSys()
    {
        return sys;
    }

    public void setSys(Sys sys)
    {
        this.sys = sys;
    }

    public List<SysFile> getSysFiles()
    {
        return sysFiles;
    }

    public void setSysFiles(List<SysFile> sysFiles)
    {
        this.sysFiles = sysFiles;
    }
}
