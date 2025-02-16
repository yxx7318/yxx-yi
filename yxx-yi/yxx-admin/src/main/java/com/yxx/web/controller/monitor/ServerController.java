package com.yxx.web.controller.monitor;

import com.yxx.common.core.domain.R;
import com.yxx.common.yxx.utils.SerializationUtils;
import com.yxx.framework.yxx.utils.ServerUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yxx.framework.web.domain.Server;

/**
 * 服务器监控
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/server")
public class ServerController
{
    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @GetMapping()
    public R<Server> getInfo() throws Exception
    {
        Server server = new Server();
        ServerUtils.copyTo(server);
        return R.ok(server);
    }

    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @GetMapping("/byte")
    public R<byte[]> getInfoByte() throws Exception
    {
        Server server = new Server();
        ServerUtils.copyTo(server);
        return R.ok(SerializationUtils.serializeToByteArray(server));
    }
}
